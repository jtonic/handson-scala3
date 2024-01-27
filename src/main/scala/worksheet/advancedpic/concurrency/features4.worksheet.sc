import java.io.IOException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

// ----------------------------------------
// 1. Zip futures
// ----------------------------------------
Future { 1 + 1 } zip Future { 2 + 2 }
val fut1 = Future.successful { 1 + 1 } zip Future.fromTry(
  Failure(ArithmeticException("Something went wrong"))
)
fut1.onComplete {
  case Success(value) => println(s"Success: $value")
  case Failure(ex)    => println(s"Exception. message: ${ex.getMessage()}")
}

val aRes1 = {
  Future.failed(Exception("Boom!")) zip Future.failed {
    IOException("Cannot read file!")
  }
}.value

// ----------------------------------------
// 2. FoldLef futures
// ----------------------------------------

val sumFun = Future.foldLeft(List(Future { 1 + 1 }, Future { 2 + 2 }))(0) {
  _ + _
}

// ----------------------------------------
// 3. Reduce futures
// ----------------------------------------

val reduceSuccFut =
  Future.reduceLeft(List(Future.successful(1 + 1), Future.successful(2 + 2))) {
    _ + _
  }
val reduceFailedFut = Future.reduceLeft[Int, Int] {
  List(Future.failed(Exception("Boom 1")), Future.failed(Exception("Boom2!")))
} { _ + _ }

// ----------------------------------------
// 4. Traverse futures
// ----------------------------------------

val traversedFut = Future.traverse(List(1, 2))(i => Future.successful(i * 2))
traversedFut.value

// ----------------------------------------
// 5. Sequence futures
// ----------------------------------------

val sequencedFuts = Future.sequence(List(Future.successful(1 + 1), Future.successful(2 + 2)))
sequencedFuts.value

val failedSeqFuts = Future.sequence(List(Future.failed(Exception("Boom1!")), Future.failed(Exception("Boom2!"))))
failedSeqFuts.value

Thread.sleep(200)
