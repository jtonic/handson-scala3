import scala.util.Either
import scala.util.Try
import scala.util.{Success, Failure}

val anOption: Some[Int] = Some(1)
val anEither: Either[Nothing, Int]  = Right(1)
val aTry: Try[Int] = Try(1)

extension [T] (t: Try[T])
  def toRight[L](tr: Throwable => L): Either[L, T] = t match
    case Success(value) => Right(value)
    case Failure(exception) => Left(tr(exception))

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


import cats.data.EitherT
import cats.syntax.all._

val o1: Option[Int] = None
val o2: Option[Int] = Some(10)
val o1ET = EitherT.fromOption[List](o1, "Answer not known.")
val o2ET = EitherT.fromOption[List](o2, "Answer not known.")

val result = for {
  a <- o1ET
  b <- o2ET
} yield a + b

result
