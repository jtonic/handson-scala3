package ro.jtonic.handson.scala3

import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.Await
import org.scalatest.funsuite.AsyncFunSuite

class FutureSuite extends AnyFunSuite with Matchers:

  test("Future with await") {
    val ft1 = Future {
      1.seconds.toSleepCurrentThread
      // Thread.sleep(2.seconds.toMillis)
      1 + 1
    }
    ft1.await(2.seconds) should be(2)
  }

class FutureAsyncSuite extends AsyncFunSuite with Matchers:

  test("Future async test"):
    Future:
      2.seconds.toSleepCurrentThread
      2 + 2
    .map: i =>
      i should be >= 0
      i should be (4)

extension [T](ft: Future[T])
  def await(duration: Duration) = Await.result(ft, duration)

extension [T <: Duration](duration: T)
  def toSleepCurrentThread = Thread.sleep(duration.toMillis)
