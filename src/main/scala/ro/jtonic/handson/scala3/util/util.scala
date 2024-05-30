package ro.jtonic.handson.scala3.util

import scala.util.{Try, Failure, Success}
import java.io.File

extension [T](x: T)
  def execute(f: T => Unit): T =
    f(x)
    x

def using[T](t: T)(f: T => Unit): Unit = f(t)

package function:

  extension [A](x: A)
    def |>[B](f: A => B): B = f(x)

  extension [A, B](f: A => B)
    def <|(a: A): B = f(a)
    def >>[C](g: B => C): A => C = f andThen g
    def <<[C](g: C => A): C => B = g andThen f

extension [T] (t: Try[T])
  def toRight[L](tr: Throwable => L): Either[L, T] = t match
    case Success(value) => Right(value)
    case Failure(exception) => Left(tr(exception))

package math:
  extension [A](self: List[A])
    def sumBy[B](f: A => B)(using num: Numeric[B]): B =
      self.map(f).sum

package benchmark:
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
    println(f"Elapsed time: $dur%.2f [${tu.toString}]")

package string:
  extension (self: java.lang.StringBuilder)
    def clear(): Unit = self.setLength(0)

package array:

  type Matrix[Char] = Array[Array[Char]]

  def load(filePath: String, sep: Char = '\u0000'): Matrix[Char] =
    import scala.io.Source
    Source.fromFile(filePath).mkString.toArray(sep)

  extension (self: String)

    def toArray(sep: Char = '\u0000'): Matrix[Char] =
      val str = self.stripMargin('|').replaceFirst("\n", "")
      val lines = (if sep == '\u0000' then str else str.replace(sep.toString, "")).split("\n")
      val arr = Array.ofDim[Char](lines.size, lines(0).length)
      for
        (l, li) <- lines.zipWithIndex
        (c, ci) <- l.zipWithIndex
      do
        arr(li)(ci) = c
      arr

  extension (self: Matrix[Char])
    def print(): Unit =
      import scala.Console.print as cPrint
      for l <- self do
        for c <- l do
          cPrint(c)
        println()
