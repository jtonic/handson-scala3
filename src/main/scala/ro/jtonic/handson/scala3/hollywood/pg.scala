package ro.jtonic.handson.scala3.hollywood

import slick.jdbc.PostgresProfile.api._
import java.time.LocalDate
import slick.jdbc.PostgresProfile
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContextExecutor
import os.read

package DbExecutionContext:
  implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

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

    println("Reading all movies...")
    db.run(movieTable.result)

  def insertMovies(): Future[Option[Int]] =
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    val godFather = Movie(name = "The Godfather", LocalDate.of(1972, 3, 24), 175)
    val matrix = Movie(name = "The Matrix", LocalDate.of(1999, 3, 31), 136)
    val matrixReloaded = Movie(name = "The Matrix Reloaded", LocalDate.of(2003, 5, 7), 138)
    val matrixRevolutions = Movie(name = "The Matrix Revolutions", LocalDate.of(2003, 10, 27), 129)
    val troy = Movie(name = "Troy", LocalDate.of(2004, 5, 14), 163)
    val movies = Seq(godFather, matrix, matrixReloaded, matrixRevolutions, troy)

    println(s"Inserting movies: ${movies}...")
    val insertQuery = DbTables.movieTable ++= movies
    db.run(insertQuery)

  def findMovieByName(name: String): Future[Seq[Movie]] =
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    println(s"Finding movie by name: ${name}...")
    val query = movieTable.filter(_.name === name)
    db.run(query.result)

  def findMovieByPartialName(partialName: String): Future[Seq[Movie]] =
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    println(s"Finding movie by partial name: ${partialName} ...")
    val query = movieTable.filter(_.name like s"%${partialName}%")
    db.run(query.result)

  def deleteMovieByName(name: String): Future[Int] =
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    println(s"Deleting movie by name: ${name}...")
    val query = movieTable.filter(_.name === name)
    db.run(query.delete)

  def deleteAllMovies(): Future[Int] =
    import slick.jdbc.PostgresProfile.api._
    import DbConnection._
    import DbExecutionContext._

    println(s"Deleting all movies...")
    db.run(DbTables.movieTable.delete)

import cats.effect.{IOApp, IO}

object DbApp extends IOApp.Simple:

  override def run: IO[Unit] =
    import slick.jdbc.PostgresProfile.api._
    import DbConnection._
    import DbModel._
    import DbOperations._
    import concurrent.duration.DurationInt

    for {
      _ <- IO(println("Start..."))
      _ <- IO.fromFuture(IO(deleteAllMovies()))
      _ <- IO(println(s"All movies were deleted!"))
      insertedMovies <- IO.fromFuture(IO(insertMovies()))
      _ <- IO(println(s"Movies were inserted! Inserted movies: $insertedMovies"))
      matrixMovies <- IO.fromFuture(IO(findMovieByPartialName("Matrix")))
      _ <- IO(println(s"Matrix movies: ${matrixMovies.mkString(", ")}"))
      allMovies <- IO.fromFuture(IO(getAllMovies()))
      _ <- IO(println(s"All movies: ${allMovies.mkString(", ")}"))
      _ <- IO(println("Closing the db connection..."))
      _ <- closeDbConnection()
      _ <- IO(println("Finished!"))
    } yield ()
