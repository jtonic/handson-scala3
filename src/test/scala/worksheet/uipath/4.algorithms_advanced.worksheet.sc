import java.util.concurrent.TimeUnit
import ro.jtonic.handson.scala3.util.Benchmark.{time, timed}
import scala.collection.mutable.ListBuffer
import cats.instances.double
import scala.collection.immutable.List
import org.scalatest.matchers.should.Matchers.*

// ------------------------------------------------------------------------------
// 1. minimum coin change (greedy algorithm)
// ------------------------------------------------------------------------------

val den = List(1, 2, 5, 10, 20, 50, 100, 200, 500) // cents

def getCoins(amount: Int): List[Int] =
  val result = ListBuffer.empty[Int]
  var _amount = amount
  for i <- (den.size - 1 to 0) by -1 do
    while den(i) <= _amount do
      _amount -= den(i)
      result += den(i)
  result.toList


getCoins(158) shouldBe List(100, 50, 5, 2, 1)

getCoins(278) shouldBe List(200, 50, 20, 5, 2, 1)


// ------------------------------------------------------------------------------
// 2. closest pair of points (divide-and-conquer algorithm)
// ------------------------------------------------------------------------------
object PairOfPoints:

  case class Point(val x: Int, val y: Int):
    def getDistanceTo(to: Point): Double =
      Math.sqrt(Math.pow(x - to.x, 2) + Math.pow(y - to.y, 2))

  case class Result(p1: Point, p2: Point, distance: Double):
    override def toString(): String =
      f"p1 = $p1, p2 = $p2, distance = $distance%.2f"

  def closest(points: List[Point]): Result =
    var result = Result(points(0), points(0), Double.MaxValue)
    for i <- 0 until points.size do
      for j <- i + 1 until points.size do
        val distance = points(i) getDistanceTo points(j)
        if distance < result.distance then result = Result(points(i), points(j), distance)
    result

  object Case1:

    def findClosestPair(points: List[Point]): Option[Result] =
      if points.size <=1 then None
      else Some(closest(points))

  object Case2:

    def closer(r1: Result, r2: Result): Result = if r1.distance < r2.distance then r1 else r2

    def findClosestPair(points: List[Point]): Option[Result] =
      if points.size <= 1 then None
      if points.size <= 3 then Some(closest(points))
      else
        val middle = points.size / 2
        val r = closer(
          findClosestPair(points.take(middle)).get,
          findClosestPair(points.drop(middle)).get
        )
        val strip = points.filter(p => Math.abs(p.x - points(middle).x) < r.distance)
        Some(closer(r, closest(strip)))

  object Feed:
    val points = List(
      Point(6, 45),   // A
      Point(12, 8),   // B
      Point(14, 31),  // C
      Point(24, 18),  // D
      Point(32, 26),  // E
      Point(40, 41),  // F
      Point(44, 6),   // G
      Point(57, 20),  // H
      Point(60, 35),  // I
      Point(72, 9),   // J
      Point(73, 41),  // K
      Point(85, 25),  // L
      Point(92, 8),   // M
      Point(93, 43)   // N
    ).sortBy(_.x)
// sort point by x

import PairOfPoints.Feed.*
import PairOfPoints.*
// usage
val result1 = timed(TimeUnit.MILLISECONDS):
  for pair <- Case1.findClosestPair(points) yield pair

println(s"The closes pair is ${result1.result}. Calculated in ${result1.duration}")

// more performant (divide-and-conquer)

val result2 = timed(TimeUnit.MILLISECONDS):
  for pair <- Case2.findClosestPair(points) yield pair

println(s"The closes pair is ${result2.result}. Calculated in ${result2.duration}")

// ------------------------------------------------------------------------------
// 3. rat in a maze
// ------------------------------------------------------------------------------

// ------------------------------------------------------------------------------
// 4. sudoku puzzle
// ------------------------------------------------------------------------------

// ------------------------------------------------------------------------------
// 5. Title guess
// ------------------------------------------------------------------------------

// ------------------------------------------------------------------------------
// 6. Password guess
// ------------------------------------------------------------------------------
