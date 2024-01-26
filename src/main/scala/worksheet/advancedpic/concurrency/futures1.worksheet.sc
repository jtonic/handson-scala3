import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

// ----------------------------------------
// 1. Futures
// ----------------------------------------
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


// ----------------------------------------
// 2. Composing Futures with for-comprehension
// ----------------------------------------
val futureCompound = for
  res1 <- future1
  res2 <- future2
yield res1 + res2

futureCompound.onComplete:
  case Success(value) => println(s"Future Compound Executed. Result = $value")
  case Failure(e) => println(e.getMessage())

val futureSuccess = Future{
  Thread.sleep(500)
  1 + 1
}

val futSuccess1 = futureSuccess.map(_ * 2)
val futureExc: Future[Int] = Future{
  throw new Exception("Oops!")
}

futureExc.onComplete:
  case Success(value) => println(s"Got the callback, meaning = $value")
  case Failure(e) => println(e.getMessage())

val futCompound2 = for
  res1 <- futureSuccess
  res2 <- futSuccess1
  res3 <- futureExc
yield res1 + res2

// ----------------------------------------
// 3. Already completed Futures
// ----------------------------------------
Future.successful(10 + 10)
Future.failed(Exception("Oops!"))
Future.fromTry(Success(10 + 10))
Future.fromTry(Failure(Exception("Oops!")))

Thread.sleep(1_500)

// ----------------------------------------
// 4. Filtering Futures
// ----------------------------------------
val futListSuccess = Future(List(1, 2, 3, 4, 5, 6))
val filteredFut = futListSuccess.map(_.filter(_ > 3))

// filter futures with for-comprehension
val filteredFut1 = for
    res <- futListSuccess
    res1 = res.filter(_ > 3) if res1.length > 2
  yield res1

// collect
futSuccess1.collect{ case res if res > 0 => res + 10 }
futSuccess1.collect{ case res => res + 100 }
futSuccess1.collect{ case res if res < 0 => res - 100 }
