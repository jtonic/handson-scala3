//> using scala 3.5.1
//> using dep "dev.zio::zio:2.1.9"

import zio._

object ZioApp extends ZIOAppDefault {
  def run =
    for {
      _ <- ZIO.debug(s"Application started!")
      _ <- ZIO.never.onInterrupt(_ => ZIO.debug("Interrupted!")).fork
      _ <- ZIO.debug("Application stopped!")
    } yield ()
}
