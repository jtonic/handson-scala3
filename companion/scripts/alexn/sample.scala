
//> using dep "ch.qos.logback:logback-classic:1.4.6"
//> using dep "co.fs2::fs2-core::3.6.1"
//> using dep "co.fs2::fs2-reactive-streams::3.6.1"
//> using dep "com.typesafe.akka::akka-actor-typed::2.6.20"
//> using dep "com.typesafe.akka::akka-actor::2.6.20"
//> using dep "com.typesafe.akka::akka-stream-typed::2.6.20"
//> using dep "com.typesafe.akka::akka-stream::2.6.20"
//> using dep "org.typelevel::cats-effect::3.4.9"

import akka.actor.typed.ActorSystem
import akka.actor.typed.{ActorSystem => TypedActorSystem}
import akka.actor.{CoordinatedShutdown, ActorSystem => UntypedActorSystem}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ClosedShape, KillSwitches, SharedKillSwitch}
import akka.{Done, NotUsed}
import cats.effect.kernel.Resource
import cats.effect.std.Dispatcher
import cats.effect.{Deferred, ExitCode, IO, IOApp}
import cats.syntax.all._
import com.typesafe.config.Config
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import org.reactivestreams.{Publisher, Subscriber, Subscription}
import org.slf4j.LoggerFactory
import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.concurrent.{Future, TimeoutException}

object Main extends ProcessorApp {
  import AkkaUtils._

  override def startProcessor(killSwitch: SharedKillSwitch)(implicit
    system: ActorSystem[_],
    dispatcher: Dispatcher[IO]
  ): Resource[IO, IO[Done]] = {
    Resource.eval {
      for {
        mySource <- IO {
          val counter = new AtomicInteger(0)
          val tryPoll = IO {
            val cnt = counter.incrementAndGet()
            if (cnt % 2 == 0) Some(cnt) else None
          }
          poll0(tryPoll, 1.second)
            // Installing killSwitch on this source
            .via(killSwitch.flow)
        }
        awaitDone <- IO {
          val sink = ignoreSink[Any]
          val graph = RunnableGraph.fromGraph(
            GraphDSL.createGraph(sink) { implicit builder => s =>
              import GraphDSL.Implicits._

              val ints = builder.add(mySource)
              val logEvents = builder.add(
                Flow[Int].map(i => logger.info(s"Received event: $i"))
              )

              // w00t, here's our very complicated graph!
              ints ~> logEvents ~> s
              ClosedShape
            }
          )
          graph.run()
        }
      } yield awaitDone
    }
  }
}

trait ProcessorApp extends IOApp {
  import AkkaUtils._

  /** Abstract method to implement... */
  def startProcessor(killSwitch: SharedKillSwitch)(implicit
    system: ActorSystem[_],
    dispatcher: Dispatcher[IO]
  ): Resource[IO, IO[Done]]

  override final def run(args: List[String]): IO[ExitCode] = {
    val startWithResources = for {
      d <- Dispatcher.parallel[IO]
      system <- startActorSystemTyped(
        systemName = "my-actor-system",
        config = None,
        useIOExecutionContext = true,
        timeoutAwaitCatsEffect = 10.seconds,
        timeoutAwaitAkkaTermination = 10.seconds,
      )
      killSwitch <- sharedKillSwitch("my-kill-switch")
      awaitDone <- startProcessor(killSwitch)(system, d)
      _ <- resourceFinalizer(
        for {
          // Kill all inputs
          _ <- IO(killSwitch.shutdown())
          // Waits the for processor to stop before proceeding;
          // Timeout is required, or this could be non-terminating
          _ <- awaitDone.timeoutAndForget(10.seconds).attempt.void
          _ <- IO(logger.info(
            "Awaited processor to stop, proceeding with shutdown..."
          ))
        } yield ()
      )
    } yield awaitDone

    startWithResources.use { awaitDone =>
      // Blocking on `awaitDone` makes sense, as the processor
      // could finish without the app receiving a termination signal
      awaitDone.as(ExitCode.Success)
    }
  }

  protected lazy val logger =
    LoggerFactory.getLogger(getClass)

  // It's a good idea to set a timeout on shutdown;
  // we need to take faulty cancellation logic into account
  override protected def runtimeConfig =
    super.runtimeConfig.copy(
      shutdownHookTimeout = 30.seconds
    )

  // We want to log uncaught exceptions in the thread pool,
  // via slf4j, otherwise they'll go to STDERR
  override protected def reportFailure(err: Throwable) =
    IO(logger.error("Unexpected error in thread-pool", err))
}

object AkkaUtils {
  /** Starts an (untyped) Akka actor system in the
    * context of a Cats-Effect `Resource`, and integrating
    * with its cancellation abilities.
    *
    * HINT: for apps (in `main`), it's best if
    * `akka.coordinated-shutdown.exit-jvm` is set to `on`,
    * because Akka can decide to shutdown on its own. And
    * having this setting interacts well with Cats-Effect.
    *
    * @param systemName is the identifying name of the system.
    * @param config is an optional, parsed HOCON configuration;
    *        if None, then Akka will read its own, possibly
    *        from `application.conf`; this parameter is
    *        provided in order to control the source of
    *        the application's configuration.
    * @param useIOExecutionContext if true, then Cats-Effect's
    *        default thread-pool will get used by Akka, as well.
    *        This is needed in order to avoid having too many
    *        thread-pools.
    * @param timeoutAwaitCatsEffect is the maximum amount of time
    *        Akka's coordinated-shutdown is allowed to wait for
    *        Cats-Effect to finish. This is needed, as Cats-Effect
    *        could have a faulty stack of disposables, or because
    *        Akka could decide to shutdown on its own.
    * @param timeoutAwaitAkkaTermination is the maximum amount of
    *        time to wait for the actor system to terminate, after
    *        `terminate()` was called. We need the timeout, because
    *        `terminate()` proved to return a `Future` that never
    *        completes in certain scenarios (could be a bug, or a
    *        race condition).
    */
  def startActorSystemUntyped(
    systemName: String,
    config: Option[Config],
    useIOExecutionContext: Boolean,
    timeoutAwaitCatsEffect: Duration,
    timeoutAwaitAkkaTermination: Duration,
  ): Resource[IO, UntypedActorSystem] = {
    // Needed to turn IO into Future
    // https://typelevel.org/cats-effect/docs/std/dispatcher
    Dispatcher.parallel[IO](await = true).flatMap { dispatcher =>
      Resource[IO, UntypedActorSystem](
        for {
          // Fishing IO's `ExecutionContext`
          ec <- Option
            .when(useIOExecutionContext)(IO.executionContext)
            .sequence
          // For synchronizing Cats-Effect with Akka
          awaitCancel <- Deferred[IO, Unit]
          // For awaiting termination, as `terminate()` is unreliable
          awaitTermination <- Deferred[IO, Unit]
          logger = LoggerFactory.getLogger(getClass)
          system <- IO {
            logger.info("Creating actor system...")
            val system = UntypedActorSystem(
              systemName.trim.replaceAll("\\W+", "-"),
              config = config,
              defaultExecutionContext = ec,
            )
            // Registering task in Akka's CoordinatedShutdown
            // that will wait for Cats-Effect to catch up,
            // blocking Akka from terminating, see:
            // https://doc.akka.io/docs/akka/current/coordinated-shutdown.html
            CoordinatedShutdown(system).addTask(
              CoordinatedShutdown.PhaseBeforeServiceUnbind,
              "sync-with-cats-effect",
            ) { () =>
              dispatcher.unsafeToFuture(
                // WARN: this may not happen, if Akka decided
                // to terminate, and `coordinated-shutdown.exit-jvm`
                // isn't `on`, hence the timeout:
                awaitCancel.get
                  .timeout(timeoutAwaitCatsEffect)
                  .recoverWith {
                    case ex: TimeoutException =>
                      IO(logger.error(
                        "Timed out waiting for Cats-Effect to catch up! " +
                          "This might indicate either a non-terminating " +
                          "cancellation logic, or a misconfiguration of Akka."
                      ))
                  }
                  .as(Done)
              )
            }
            CoordinatedShutdown(system).addTask(
              CoordinatedShutdown.PhaseActorSystemTerminate,
              "signal-terminated",
            ) { () =>
              dispatcher.unsafeToFuture(
                awaitTermination.complete(()).as(Done)
              )
            }
            system
          }
        } yield {
          val cancel =
            for {
              // Signals that Cats-Effect has caught up with Akka
              _ <- awaitCancel.complete(())
              _ <- IO(logger.warn("Shutting down actor system!"))
              // Shuts down Akka, and waits for its termination
              // Here, system.terminate() returns a `Future[Terminated]`,
              // but we are ignoring it, as it could be non-terminating
              _ <- IO(system.terminate())
              // Waiting for Akka to terminate via its CoordinatedShutdown
              _ <- awaitTermination.get
              // WARN: `whenTerminated` is unreliable
              _ <- IO.fromFuture(IO(system.whenTerminated)).void
                .timeoutAndForget(timeoutAwaitAkkaTermination)
                .handleErrorWith(_ =>
                  IO(logger.warn(
                    "Timed-out waiting for Akka to terminate!"
                  ))
                )
            } yield ()
          (system, cancel)
        }
      )
    }
  }

  /** Starts a (typed) Akka actor system.
    *
    * @see [[startActorSystemUntyped]] for more details on params.
    */
  def startActorSystemTyped(
    systemName: String,
    config: Option[Config],
    useIOExecutionContext: Boolean,
    timeoutAwaitCatsEffect: FiniteDuration,
    timeoutAwaitAkkaTermination: FiniteDuration,
  ): Resource[IO, TypedActorSystem[Nothing]] =
    startActorSystemUntyped(
      systemName,
      config,
      useIOExecutionContext,
      timeoutAwaitCatsEffect,
      timeoutAwaitAkkaTermination,
    ).map { system =>
      import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
      system.toTyped
    }

  /** Converts a Cats-Effect `IO` into a Reactive Streams `Publisher`.
    *
    * [[https://github.com/reactive-streams/reactive-streams-jvm]]
    */
  def toPublisher[A](io: IO[A])(implicit d: Dispatcher[IO]): Publisher[A] =
    (s: Subscriber[_ >: A]) => s.onSubscribe(new Subscription {
      type CancelToken = () => Future[Unit]

      private[this] val NOT_STARTED: Null = null
      private[this] val LOCKED = Left(false)
      private[this] val COMPLETED = Left(true)

      // State machine for managing the active subscription
      private[this] val ref =
        new AtomicReference[Either[Boolean, CancelToken]](NOT_STARTED)

      override def request(n: Long): Unit =
        ref.get() match {
          case NOT_STARTED =>
            if (n <= 0) {
              if (ref.compareAndSet(NOT_STARTED, COMPLETED))
                s.onError(new IllegalArgumentException(
                  "non-positive request signals are illegal"
                ))
            } else {
              if (ref.compareAndSet(NOT_STARTED, LOCKED)) {
                val cancelToken = d.unsafeRunCancelable(
                  io.attempt.flatMap { r =>
                    IO {
                      r match {
                        case Right(value) =>
                          s.onNext(value)
                          s.onComplete()
                        case Left(e) =>
                          s.onError(e)
                      }
                      // GC purposes
                      ref.lazySet(COMPLETED)
                    }
                  }
                )
                // Race condition with lazySet(COMPLETED), but it's fine
                ref.set(Right(cancelToken))
              }
            }
          case Right(_) | Left(_) =>
            // Already active, or completed
            ()
        }

      @tailrec
      override def cancel(): Unit =
        ref.get() match {
          case NOT_STARTED =>
            if (!ref.compareAndSet(NOT_STARTED, COMPLETED))
              cancel() // retry
          case LOCKED =>
            Thread.onSpinWait()
            cancel() // retry
          case Left(_) =>
            ()
          case current@Right(token) =>
            // No retries necessary; if state changes from Right(token),
            // it means that the stream is already completed or canceled
            if (ref.compareAndSet(current, COMPLETED))
              token()
        }
    })

  def toSource[A](io: IO[A])(implicit d: Dispatcher[IO]): Source[A, NotUsed] =
    Source.fromPublisher(toPublisher(io))

  def uncancelableIOToFlow[A, B](parallelism: Int)(
    f: A => IO[B]
  )(implicit d: Dispatcher[IO]): Flow[A, B, NotUsed] =
    Flow[A].mapAsync(parallelism)(a => d.unsafeToFuture(f(a)))


  def cancelableIOToFlow[A, B](parallelism: Int)(
    f: A => IO[B]
  )(implicit d: Dispatcher[IO]): Flow[A, B, NotUsed] =
    Flow[A].flatMapMerge(
      breadth = parallelism,
      a => toSource(f(a))
    )

  def poll0[A](
    tryPoll: IO[Option[A]],
    interval: FiniteDuration,
  )(implicit d: Dispatcher[IO]): Source[A, NotUsed] = {
    val logger = LoggerFactory.getLogger(getClass)
    Source.repeat(())
      .via(cancelableIOToFlow(1) { _ =>
        tryPoll.handleError { e =>
          logger.error("Unhandled error in poll", e)
          None
        }.flatTap {
          case None => IO.sleep(interval)
          case Some(_) => IO.unit
        }
      })
      .collect { case Some(a) => a }
  }

  def poll1[A](
    tryPoll: IO[Option[A]],
    interval: FiniteDuration,
  )(implicit d: Dispatcher[IO]): Source[A, NotUsed] = {
    val logger = LoggerFactory.getLogger(getClass)
    // Notice the `takeWhile`. This is a child stream that gets
    // composed via `flatMapConcat`.
    val drain =
    Source.repeat(())
      .via(uncancelableIOToFlow(1) { _ =>
        tryPoll.handleError { e =>
          logger.error("Unhandled error in poll", e)
          None
        }
      })
      .takeWhile(_.nonEmpty)
      .collect { case Some(a) => a }

    Source
      .tick(initialDelay = Duration.Zero, interval = interval, tick = ())
      .flatMapConcat(_ => drain)
      .mapMaterializedValue(_ => NotUsed)
  }

  def poll2[A](
    tryPoll: IO[Option[A]],
    interval: FiniteDuration,
  )(implicit d: Dispatcher[IO]): Source[A, NotUsed] = {
    val logger = LoggerFactory.getLogger(getClass)
    val repeatedTask = tryPoll.handleError { e =>
      logger.error("Unhandled error in poll", e)
      None
    }.flatTap {
      case None => IO.sleep(interval)
      case Some(_) => IO.unit
    }

    val stream = fs2.Stream
      .repeatEval(repeatedTask)
      .collect { case Some(a) => a }

    import fs2.interop.reactivestreams._
    Source.fromPublisher(new StreamUnicastPublisher(stream, d))
  }

  def ignoreSink[A]: Sink[A, IO[Done]] =
    Sink.ignore.mapMaterializedValue(f => IO.fromFuture(IO.pure(f)))

  def sharedKillSwitch(name: String): Resource[IO, SharedKillSwitch] =
    Resource(IO {
      val ks = KillSwitches.shared(name)
      (ks, IO(ks.shutdown()))
    })

  def resourceFinalizer(effect: IO[Unit]): Resource[IO, Unit] =
    Resource(IO {
      ((), effect)
    })
}
