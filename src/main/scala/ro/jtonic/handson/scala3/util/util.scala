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

object  Benchmark:

  import java.util.concurrent.TimeUnit
  import scala.concurrent.duration.Duration

  def time(tu: TimeUnit = TimeUnit.NANOSECONDS)(block: => Unit) =
    val start = System.nanoTime()
    block
    val dur = Duration(System.nanoTime() - start, TimeUnit.NANOSECONDS).toUnit(tu)
    println(s"Elapsed time: $dur [${tu.toString()}]")
