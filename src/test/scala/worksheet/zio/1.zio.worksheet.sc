import $ivy.`dev.zio::zio:2.1.1`

import zio._

// kkmk

object Business

val app =
  for
    age <- Console.readLine("Enter your age?")
    _ <- Console.printLine("Hello ZIO")
    _ <- ZIO.log("Starting ZIO app")
    a <- ZIO.succeed("a")
    _ <- ZIO.log("Ending ZIO app")
  yield a

object Environment

val addSimpleLogger: ZLayer[Any, Nothing, Unit] =
  Runtime.addLogger((_, _, _, message: () => Any, _, _, _, _) =>
    println(message())
  )

val layer: ZLayer[Any, Nothing, Unit] =
  Runtime.removeDefaultLoggers ++ addSimpleLogger

val runtime: Runtime[Any] =
  Unsafe.unsafe { implicit unsafe =>
    Runtime.unsafe.fromLayer(layer)
  }

def zioApp(): String =
  val appResult = Unsafe.unsafe{ implicit unsafe =>
    runtime.unsafe
      .run(app)
      .getOrThrowFiberFailure()
  }
  appResult

zioApp()
