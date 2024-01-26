import java.io.IOException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
// ----------------------------------------
// 1. Treating failed futures
// ----------------------------------------

// failed
val failedFut = Future(10 / 0)
failedFut.value

val expectedFailureFut = failedFut.failed
expectedFailureFut.value

val succFut: Future[Int] = Future.successful { 1 + 1 }
succFut.value

val unexpectedSuccessFut = succFut.failed
unexpectedSuccessFut.value

// fallback
val fallbackFut = Future(1 / 0).fallbackTo(Future.successful(Int.MaxValue))
fallbackFut.value

Future(1 / 0).fallbackTo { Future { val res = 2; require(res > 10); res } }

// recover
Future(1 / 0).recover { case _: ArithmeticException => Int.MaxValue }
Future(10 / 2).recover { case _: ArithmeticException => Int.MaxValue }
Future(10 / 0).recover { case _: IOException => Int.MaxValue }

// recover with

Future(1 / 0).recoverWith { case _: ArithmeticException => Future(Int.MaxValue) }

Thread.sleep(200)
