import scala.concurrent.Await
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*

// ----------------------------------------
// 1. Side effects with futures
// ----------------------------------------

println(s"[Thread][Main] ${Thread.currentThread()}")

// ----------------------------------------
// 1.1. Side effects with onComplete
// ----------------------------------------
val ft1 = Future:
  Thread.sleep(500.milli.toMillis)
  10 + 10

ft1.onComplete:
  case Success(value) => println(s"0th value: $value")
  case Failure(exception) => println(s"0th exception message: ${exception.getMessage()}")

extension [T](future: Future[T]) {
  def await(duration: Duration): T = Await.result(future, duration)
}

// Await.result(fut1, 2.seconds)
ft1.await(2.seconds)

// ----------------------------------------
// 1.2. Side effects with foreach
// ----------------------------------------
Future {
  Thread.sleep(100)
  1 + 1
}.foreach { i => println(s"1st value: $i") }

Future {
  Thread.sleep(50)
  println(s"[Thread][2nd] ${Thread.currentThread()}")
  throw RuntimeException("Boom!")
}.foreach { i => println(s"2nd value: $i") }

for fut1 <- Future { 10 / 2 } do println(s"3rd value: $fut1")
for fut2 <- Future { 10 / 0 } do println(s"4th value: $fut2")

// ----------------------------------------
// 1.3. Side effects with andThen
// ----------------------------------------

Future:
  Thread.sleep(100)
  6
.andThen:
  case Success(value) => println(s"6th value: $value")
  case Failure(exception) =>  println(s"6th exception: ${exception.getMessage()}")
.andThen:
  case Success(value) => println(s"6th value: $value")
  case Failure(exception) =>  println(s"7th exception: ${exception.getMessage()}")

  Thread.sleep(500)
