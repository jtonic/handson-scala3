import scala.concurrent.Future
import scala.util.{Try, Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*


val numbersFt = Future.successful(1)
val stringsFt = Future.successful("One")

numbersFt.zipWith(stringsFt): (i, s) =>
  s"$i maps to $s"
  // but what if you have some conditions. See below with pattern match for function types

numbersFt.zipWith(stringsFt):
  case (i, _) if i < 0 => s"negative $i maps to 'negative'"
  case (i, s) => s"$i maps to $s"

Thread.sleep(1.seconds.toMillis)

implicit def durationToMillis(duration: Duration): Long = duration.toMillis

Thread.sleep(1.seconds)
