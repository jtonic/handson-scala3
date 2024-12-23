import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

val resultF = Future{
  Thread.sleep(1_000)
  println("Hello, world! I wake up after 1 second.")
  2 + 2
}

resultF.isCompleted
resultF.value

resultF.onComplete{
  case Success(value) => println(s"Got the callback, meaning = $value")
  case Failure(e) => e.printStackTrace
}

Thread.sleep(1_500)
