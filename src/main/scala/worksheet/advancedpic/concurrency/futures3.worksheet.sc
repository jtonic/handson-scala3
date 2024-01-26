import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
// ----------------------------------------
// 1. Transforming Futures
// ----------------------------------------

val transfFut = Future(1 + 1).transform(
  value => value * -1,
  exc => Exception("Exception was thrown", exc)
)

transfFut.value

val transfFut2 = Future(1 / 0).transform(
  value => value * -1,
  exception => Exception("An exception was thrown", exception)
)

Future(1 / 0).transform{
  case Success(value) => Success(value * -1)
  case Failure(exc) => Failure(Exception("Another exception was thrown", exc))
}

// transform a failure into an success
Future(1 / 0).transform {
  case Success(value) => Success(value)
  case Failure(exception) => Success(Int.MaxValue)
}

Thread.sleep(200)
