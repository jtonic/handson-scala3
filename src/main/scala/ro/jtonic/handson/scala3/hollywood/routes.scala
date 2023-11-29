package ro.jtonic.handson.scala3.hollywood

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.IOApp

package routes:
  import model._
  import db._

  def miscsRoutes[F[_] : Monad]: HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F]:
      case GET -> Root / "keepalive" => Ok("I'm alive!")

  def moviesRoutes[F[_] : Monad]: HttpRoutes[F] =
    import io.circe.syntax._
    import io.circe.generic.auto._
    import org.http4s.circe._
    val dsl = new Http4sDsl[F]{}
    import dsl._

    object ActorQueryParamMatcher extends QueryParamDecoderMatcher[String]("actor")

    HttpRoutes.of[F]:
      case GET -> Root / "actor" :? ActorQueryParamMatcher(actorName)  =>
        actors.find(_.name == actorName) match
          case None => NotFound(s"No actor found with the name $actorName.")
          case Some(actor) => Ok(actor.asJson)
      case GET -> Root / "movies" :? ActorQueryParamMatcher(actorName) =>
        movies.filter(_.actors.contains(actorName)) match
          case Nil => NotFound(s"No movies found the actor $actorName played in.")
          case actorMovies => Ok(actorMovies.asJson)

  def allRoutes[F[_]: Monad]: HttpRoutes[F] =
    import cats.syntax.semigroupk._
    (miscsRoutes[F] <+> moviesRoutes[F])
