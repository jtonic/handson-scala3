package ro.jtonic.handson.scala3.util

import scala.util.{Try, Failure, Success}

extension [A](x: A)
  def |>[B](f: A => B): B = f(x)

extension [A, B](f: A => B)
  def >>[C](g: B => C): A => C = f.andThen(g)
  def <<[C](g: C => A): C => B = g.andThen(f)
  def <|(a: A): B = f(a)

extension [T] (t: Try[T])
  def toRight[L](tr: Throwable => L): Either[L, T] = t match
    case Success(value) => Right(value)
    case Failure(exception) => Left(tr(exception))

def using[T](t: T)(f: T => Unit): Unit = f(t)

extension [T](x: T)
  def execute(f: T => Unit): T =
    f(x)
    x

extension [A](self: List[A])
  def sumBy[B](f: A => B)(using num: Numeric[B]): B =
    self.map(f).sum

object  Benchmark:

  import java.util.concurrent.TimeUnit
  import scala.concurrent.duration.Duration

  case class DurationResult[T](result: T, duration: Double)

  def time[A](tu: TimeUnit)(block: => A): A =
    val start = System.nanoTime()
    val result = block
    val dur = Duration(System.nanoTime() - start, TimeUnit.NANOSECONDS).toUnit(tu)
    println(f"Elapsed time: $dur%.2f [${tu.toString()}]")
    result

  def timed[A](tu: TimeUnit)(block: => A): DurationResult[A] =
    val start = System.nanoTime()
    val result = block
    val dur = Duration(System.nanoTime() - start, TimeUnit.NANOSECONDS).toUnit(tu)
    DurationResult(result, dur)

  def time(tu: TimeUnit = TimeUnit.NANOSECONDS)(block: => Unit): Unit =
    val start = System.nanoTime()
    block
    val dur = Duration(System.nanoTime() - start, TimeUnit.NANOSECONDS).toUnit(tu)
    println(f"Elapsed time: $dur%.2f [${tu.toString()}]")
