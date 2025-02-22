package ro.jtonic.handson.scala3.hollywood

import slick.jdbc.PostgresProfile.api._
import java.time.LocalDate
import slick.jdbc.PostgresProfile
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContextExecutor
import os.read
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger
import cats.effect.unsafe.implicits.global
import cats.syntax.flatMap

implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

package config:
  case class ServerMetadata(name: String, value: String, description: Option[String], valueType: String)
  case class ServerSettings(
    hostName: String, port: Int, tls: Option[Boolean],
    aliases: List[String], info: Map[String, String], metadata: List[ServerMetadata])

  def fetchServerSettings() =
    import io.circe.generic.auto._
    import io.circe.config.syntax._
    import io.circe.config.parser
    import cats.effect.IO
    import cats.effect.unsafe.implicits.global

    parser.decodePathF[IO, ServerSettings](path = "serverSettings")

package exceptions:
  final case class ValidationException(msg: String)
      extends RuntimeException(msg)

package DbExecutionContext:
  implicit val ec: ExecutionContextExecutor =
    scala.concurrent.ExecutionContext.global

package DbConnection:
  import cats.effect.IO
  val db = Database.forConfig("postgres")
  def closeDbConnection(): IO[Unit] = IO(db.close())

package DbModel:
  final case class Movie(
      id: Long = 0,
      name: String,
      releaseDate: LocalDate,
      lengthInMin: Int
  )

package DbTables:
  import DbModel._
  class MovieTable(tag: Tag) extends Table[Movie](tag, Some("movies"), "Movie"):
    def id = column[Long]("movie_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def releaseDate = column[LocalDate]("release_date")
    def lengthInMin = column[Int]("length_in_min")
    def * = (id, name, releaseDate, lengthInMin).mapTo[Movie]
  // API entry point
  lazy val movieTable = TableQuery[MovieTable]

package DbOperations:
  import DbModel._

  def getAllMovies(): Future[Seq[Movie]] =
    import slick.jdbc.PostgresProfile.api._
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    logger.info("Reading all movies...")
    db.run(movieTable.result)

  def insertMovies(): Future[Option[Int]] =
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    val godFather =
      Movie(name = "The Godfather", releaseDate = LocalDate.of(1972, 3, 24), lengthInMin = 175)
    val matrix = Movie(name = "The Matrix", releaseDate = LocalDate.of(1999, 3, 31), lengthInMin = 136)
    val matrixReloaded =
      Movie(name = "The Matrix Reloaded", releaseDate = LocalDate.of(2003, 5, 7), lengthInMin = 138)
    val matrixRevolutions =
      Movie(name = "The Matrix Revolutions", releaseDate = LocalDate.of(2003, 10, 27), lengthInMin = 129)
    val matrixResurrections =
      Movie(name = "The Matrix Resurrections", releaseDate = LocalDate.of(2021, 12, 22), lengthInMin = 148)
    val troy = Movie(name = "Troy", releaseDate = LocalDate.of(2004, 5, 14), lengthInMin = 163)
    val movies = Seq(
      godFather,
      matrix,
      matrixReloaded,
      matrixRevolutions,
      matrixResurrections,
      troy
    )

    logger.info(s"Inserting movies: ${movies}...").unsafeToFuture()
    val insertQuery = DbTables.movieTable ++= movies
    db.run(insertQuery)

  def findMovieByName(name: String): Future[Seq[Movie]] =
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    logger.info(s"Finding movie by name: ${name}...").unsafeToFuture()
    val query = movieTable.filter(_.name === name)
    db.run(query.result)

  def findMovieByPartialName(partialName: String): Future[Seq[Movie]] =
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    logger
      .info(s"Finding movie by partial name: ${partialName} ...")
      .unsafeToFuture()
    val query = movieTable.filter(_.name like s"%${partialName}%")
    db.run(query.result)

  def deleteMovieByName(name: String): Future[Int] =
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    logger.info(s"Deleting movie by name: ${name}...")
    val query = movieTable.filter(_.name === name)
    db.run(query.delete)

  def deleteAllMovies(): Future[Int] =
    import slick.jdbc.PostgresProfile.api._
    import DbConnection._
    import DbExecutionContext._

    logger.info(s"Deleting all movies...").unsafeToFuture()
    db.run(DbTables.movieTable.delete)

package DbPreconditions:
  import cats.effect.IO
  import exceptions._
  import cats.data.{ValidatedNec, ValidatedNel}
  import cats.implicits._

  def validatePositive(a: Int): IO[Int] =
    if a > 0 then IO.pure(a) else IO.raiseError(ValidationException(s"Value '$a' is not positive!"))

  def validateNotBlank(a: String): ValidatedNel[String, String] =
    if a.isBlank() then s"Value '$a' is empty".invalidNel else a.validNel

  def validatedInput(input: Set[String]): IO[Set[String]] =
    input.toList.traverse(validateNotBlank).map(_.toSet).toEither match
      case Left(errors) =>
        IO.raiseError(
          ValidationException(s"Invalid input: ${errors.toNonEmptyList.toList.mkString(", ")}")
        )
      case Right(validatedInput) => IO.pure(validatedInput)


package fp:
  extension [F[_], A](fa: F[A])
    // flatMap
    def >>-[B](f: A => F[B])(using F: cats.FlatMap[F]): F[B] = F.flatMap(fa)(f)
    // flatTap
    def >>>-[B](f: A => F[B])(using F: cats.FlatMap[F]): F[A] = F.flatTap(fa)(f)

// Consider this
// https://http4s.org/v1/docs/dsl.html#handling-query-parameters

import cats.effect.{IOApp, IO}

object DbApp extends IOApp.Simple:

  override def run: IO[Unit] =
    import slick.jdbc.PostgresProfile.api._
    import config._
    import fp._
    import exceptions._
    import DbPreconditions._
    import DbConnection._
    import DbModel._
    import DbOperations._
    import concurrent.duration.DurationInt

    val program: IO[Unit] = for {
        _ <- logger.info("Start...")

        serverSettings <- fetchServerSettings() >>>- { it => logger.info(s"Server settings: $it") }

        // validate the input
        inputInt <- validatePositive(10) >>>- { it => logger.info(s"Input $it!") }
        _ <- validatedInput(Set("Irina", "Tony", "Roxana"))

        //delete all movies
        _ <- IO.fromFuture(IO(deleteAllMovies())) <* logger.info("All movies were deleted!")

        // insert movies
        _ <- IO.fromFuture(IO(insertMovies())) >>- { it => logger.info(s"Movies were inserted! Inserted movies: $it") }

        // find movies by name
        matrixMovies <- IO.fromFuture(IO(findMovieByPartialName("Matrix")))
        _ <- if matrixMovies.size != 4 then IO.raiseError(ValidationException("There are 5 parts in Matrix series!")) else IO.unit
        _ <- logger.info(s"Matrix movies: ${matrixMovies.mkString(", ")}")

        // find all movies
        allMovies <- IO.fromFuture(IO(getAllMovies())) >>- { movies =>
          if movies.size < 5 then
            IO.raiseError(ValidationException("There are less than 10 movies in db!"))
          else IO.pure(movies)
        }

        _ <- logger.info(s"All movies: ${allMovies.mkString(", ")}")

        // close the db connection
        _ <- logger.info("Closing the db connection...")
        _ <- closeDbConnection()

        _ <- logger.info("Finished!")
      } yield ()

      program.handleError(e => logger.error(e)(s"An error occurred: ${e.getMessage}").unsafeRunAndForget())
