import $ivy.`dev.zio::zio:2.1.1`

import zio._
import zio.Runtime

val myApp = ZIO.succeed("a")

val runtime = Runtime.default

val appResult = Unsafe.unsafe { implicit unsafe =>
      Runtime.default.unsafe.run(
        myApp
      ).getOrThrowFiberFailure()
  }

appResult
