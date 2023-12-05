package ro.jtonic.handson.scala3.hollywood

import java.time.LocalDate
import slick.jdbc.PostgresProfile
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContextExecutor
import os.read

package DbConnection:
  import slick.jdbc.PostgresProfile.api._
  val db = Database.forConfig("postgres")

package DbModel:
  final case class Movie(
    id: Long = 0,
    name: String,
    releaseDate: LocalDate,
    lengthInMin: Int
    )

package DbExecutionContext:
  implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

package DbTables:
  import slick.jdbc.PostgresProfile.api._
  import DbModel._
  class MovieTable(tag: Tag) extends Table[Movie](tag, Some("movies"), "Movie") {
    def id = column[Long]("movie_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def releaseDate = column[LocalDate]("release_date")
    def lengthInMin = column[Int]("length_in_min")
    def * = (id, name, releaseDate, lengthInMin).mapTo[Movie]

  }
  // API entry point
  lazy val movieTable = TableQuery[MovieTable]

package DbOperations:

  def readAllMovies() =
    import slick.jdbc.PostgresProfile.api._
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    print("Reading all movies...")
    val result: Future[Seq[Movie]] = db.run(movieTable.result)
    result.onComplete{
      case Success(movies) => println(s"Movies: ${movies}")
      case Failure(ex) => println(s"Error: ${ex}")
    }
    Thread.sleep(5000)

  def insertMovies() =
    import slick.jdbc.PostgresProfile.api._
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
    Thread.sleep(5000)

  def findMovieByName(name: String) =
    import slick.jdbc.PostgresProfile.api._
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    println(s"Finding movie by name: ${name}...")
    val query = movieTable.filter(_.name === name)
    val result = db.run(query.result)
    result.onComplete{
      case Success(movies) => println(s"Movie: ${movies}")
      case Failure(ex) => println(s"Error: ${ex}")
    }
    Thread.sleep(5000)

  def findMovieByPartialName(partialName: String) =
    import slick.jdbc.PostgresProfile.api._
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    println(s"Find movie by partial name: ${partialName}")
    val query = movieTable.filter(_.name like s"%${partialName}%")
    val result = db.run(query.result)
    result.onComplete{
      case Success(movies) => println(s"$partialName movies: ${movies}")
      case Failure(ex) => println(s"Error: ${ex}")
    }
    Thread.sleep(5000)

  def deleteMovieByName(name: String) =
    import slick.jdbc.PostgresProfile.api._
    import DbModel._
    import DbTables._
    import DbConnection._
    import DbExecutionContext._

    println(s"Deleting movie by name: ${name}...")
    val query = movieTable.filter(_.name === name)
    val result = db.run(query.delete)
    result.onComplete{
      case Success(deletedRows) => println(s"Deleted rows: ${deletedRows}")
      case Failure(ex) => println(s"Error: ${ex}")
    }
    Thread.sleep(5000)

  def deleteAllMovies() =
    import slick.jdbc.PostgresProfile.api._
    import DbConnection._
    import DbExecutionContext._

    println(s"Deleting all movies...")
    val deleteQuery = db.run(DbTables.movieTable.delete)
    deleteQuery.onComplete:
      case Success(deletedRows) => println(s"Deleted rows: ${deletedRows}")
      case Failure(ex) => println(s"Error: ${ex}")
    Thread.sleep(5000)

@main def main() = {
  import DbConnection._
  import DbOperations._

  println("Start...")
  deleteAllMovies()
  insertMovies()
  findMovieByPartialName("Matrix")
/*
  findMovieByName("The Matrix")
  deleteMovieByName("The Matrix")
*/
  readAllMovies()
  db.close()
  println("Finished!")
}
