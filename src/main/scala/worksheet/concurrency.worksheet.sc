import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

val future1 = Future{
  Thread.sleep(1_000)
  println("Slept 1 sec!")
  2 + 2
}

val future2 = Future{
  Thread.sleep(500)
  println("Slept 500 ms!")
  1 + 1
}

future1.isCompleted
future1.value

future1.onComplete{
  case Success(value) => println(s"Got the callback, meaning = $value")
  case Failure(e) => e.printStackTrace
}

val futureCompound = for
  res1 <- future1
  res2 <- future2
yield res1 + res2

val futureExc = Future{
  throw new Exception("Oops!")
}

futureExc.onComplete:
  case Success(value) => println(s"Got the callback, meaning = $value")
  case Failure(e) => println(e.getMessage())

futureCompound.onComplete:
  case Success(value) => println(s"Future Compound Executed. Result = $value")
  case Failure(e) => println(e.getMessage())

// Already completed futures
Future.successful(10 + 10)
Future.failed(Exception("Oops!"))
Future.fromTry(Success(10 + 10))
Future.fromTry(Failure(Exception("Oops!")))

Thread.sleep(1_500)


// ----------------------------------------
// Promises
// ----------------------------------------

import scala.concurrent.Promise

val promise = Promise[Int]
val future = promise.future

promise.success(10)
future.value
