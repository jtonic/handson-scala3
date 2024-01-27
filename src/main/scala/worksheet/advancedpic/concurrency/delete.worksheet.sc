import scala.util.{Try, Success, Failure}

def foo(a: Try[Int]): Int =
  a match
    case Success(value) => value
    case Failure(exception) => 0

foo(Success(1))


def boo[T](in: Try[T])(f: Try[T] => T) =
  f(in).toString

val result: String = boo(Success("Success")):
  case Success(value) => value
  case Failure(ex) => ex.getMessage()

// versus more verbose:
val result2: String = boo(Success("Success")): in =>
  in match
    case Success(value) => value
    case Failure(ex) => ex.getMessage()
