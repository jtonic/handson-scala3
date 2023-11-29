package ro.jtonic.handson.scala3

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.IOApp

def miscsRoutes[F[_] : Monad]: HttpRoutes[F] =
  val dsl = new Http4sDsl[F]{}
  import dsl._
  HttpRoutes.of[F]:
    case GET -> Root / "keepalive" => Ok("I'm alive!")

def moviesRoutes[F[_] : Monad]: HttpRoutes[F] =
  val dsl = new Http4sDsl[F]{}
  import dsl._

  case class Movie(title: String, year: Int, rating: Double)
  case class Actor(name: String, age: Int)

  object ActorQueryParamMatcher extends QueryParamDecoderMatcher[String]("actor")

  HttpRoutes.of[F]:
    case req @ GET -> Root / "actor"/*  :? actor */  =>
      import io.circe.syntax._
      import io.circe.generic.auto._
      import org.http4s.circe._
      Ok(Actor("Brad Pitt", 57).asJson)

def allRoutes[F[_]: Monad]: HttpRoutes[F] =
  import cats.syntax.semigroupk._
  (miscsRoutes[F] <+> moviesRoutes[F])

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
            .withHttpApp(allRoutes[IO].orNotFound)
            .resource
            .use(_ => IO.never)
            .as(ExitCode.Success)
