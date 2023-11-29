package ro.jtonic.handson.scala3

import cats.effect.{IO, IOApp}
import scala.io.StdIn._

object CatsApp extends IOApp.Simple:
  def run: IO[Unit] =
    for
      _ <- IO(println("What is your name?"))
      name <- IO(readLine())
      _ <- IO(println(s"Hello, $name!"))
    yield ()
