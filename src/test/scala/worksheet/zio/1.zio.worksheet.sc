import $ivy.`dev.zio::zio:2.1.1`

import zio._
import zio.Runtime

// kkmk


val app =
  for
    _ <- ZIO.log("Starting ZIO app")
    a <- ZIO.succeed("a")
    _ <- ZIO.log("Ending ZIO app")
  yield a

val addSimpleLogger: ZLayer[Any, Nothing, Unit] =
  Runtime.addLogger((_, _, _, message: () => Any, _, _, _, _) => println(message()))

val layer: ZLayer[Any, Nothing, Unit] =
  Runtime.removeDefaultLoggers ++ addSimpleLogger

val runtime: Runtime[Any] =
  Unsafe.unsafe { implicit unsafe =>
    Runtime.unsafe.fromLayer(layer)
  }

def zioApp(): String =
  val appResult = Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe
          .run(app)
          .getOrThrowFiberFailure()
    }
  appResult

zioApp()
