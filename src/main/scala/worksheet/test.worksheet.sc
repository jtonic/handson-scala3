import ro.jtonic.handson.scala3.util.toRight
import scala.util.Either
import scala.util.Try
import scala.util.{Success, Failure}

val anOption: Some[Int] = Some(1)
val anEither: Either[Nothing, Int]  = Right(1)
val aTry: Try[Int] = Try(1)

def validate[T <: Int](opt: Option[T], either: Either[String, T], tried: Try[T]) = {
  for {
    option <- opt.toRight("Empty")
    either <- either
    tried <- tried.toRight(_.getMessage)
  } yield option + either + tried
}

val response1 = validate(anOption, anEither, aTry)
response1 match
  case Right(value) => println(value)
  case Left(value) => println(value)

val response2 = validate(anOption, anEither, Try(throw new Exception("Boom!")))
response2 match
  case Right(value) => println(value)
  case Left(value) => println(value)
