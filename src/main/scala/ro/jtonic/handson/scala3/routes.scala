package ro.jtonic.handson.scala3

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.IOApp

def movieRoutes[F[_] : Monad]: HttpRoutes[F] =
  val dsl = new Http4sDsl[F]{}
  import dsl._
  HttpRoutes.of[F]:
    case GET -> Root / "keepalive" => Ok("I'm alive!")

object Http4sService extends IOApp:
  import org.http4s.server.blaze.BlazeServerBuilder
  import org.http4s.implicits._
  import cats.implicits._
  import cats.effect._
  import cats.syntax.all._
  import scala.concurrent.ExecutionContext.global
  import org.typelevel.log4cats.slf4j.loggerFactoryforSync

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
            .bindHttp(8080, "localhost")
            .withHttpApp(movieRoutes[IO].orNotFound)
            .resource
            .use(_ => IO.never)
            .as(ExitCode.Success)
