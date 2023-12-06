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
      Movie(name = "The Godfather", LocalDate.of(1972, 3, 24), 175)
    val matrix = Movie(name = "The Matrix", LocalDate.of(1999, 3, 31), 136)
    val matrixReloaded =
      Movie(name = "The Matrix Reloaded", LocalDate.of(2003, 5, 7), 138)
    val matrixRevolutions =
      Movie(name = "The Matrix Revolutions", LocalDate.of(2003, 10, 27), 129)
    val matrixResurrections =
      Movie(name = "The Matrix Resurrections", LocalDate.of(2021, 12, 22), 148)
    val troy = Movie(name = "Troy", LocalDate.of(2004, 5, 14), 163)
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

  def validate(a: Int): IO[Boolean] = IO(a > 0)

package fp:

  extension [F[_], A](fa: F[A])
    def >>-[B](f: A => F[B])(using F: cats.FlatMap[F]): F[B] = F.flatMap(fa)(f)

// Consider this
// https://http4s.org/v1/docs/dsl.html#handling-query-parameters

import cats.effect.{IOApp, IO}

object DbApp extends IOApp.Simple:

  override def run: IO[Unit] =
    import slick.jdbc.PostgresProfile.api._
    import fp._
    import exceptions._
    import DbPreconditions._
    import DbConnection._
    import DbModel._
    import DbOperations._
    import concurrent.duration.DurationInt

    val program: IO[Unit] = for {
        _ <- logger.info("Start...")

        // validate the input
        isValidationOk <- logger.info("Validating the input...") *> validate(10)
        _ <- if isValidationOk then IO.unit else IO.raiseError(ValidationException("Validation failed!"))

        //delete all movies
        _ <- IO.fromFuture(IO(deleteAllMovies())) <* logger.info(s"All movies were deleted!")

        // insert movies
        _ <- IO.fromFuture(IO(insertMovies())) >>- { insertedMovies => logger.info(s"Movies were inserted! Inserted movies: ${insertedMovies}") }

        // find movies by name
        matrixMovies <- IO.fromFuture(IO(findMovieByPartialName("Matrix")))
        _ <- if matrixMovies.size != 4 then IO.raiseError(ValidationException("There are 5 parts in Matrix series!")) else IO.unit
        _ <- logger.info(s"Matrix movies: ${matrixMovies.mkString(", ")}")

        // find all movies
        allMovies <- IO.fromFuture(IO(getAllMovies())) >>- { movies =>
          if movies.size < 10 then
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
