package ro.jtonic.handson.scala3.algorithms

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.annotation.tailrec
import ro.jtonic.handson.scala3.util.Benchmark.time
import java.util.concurrent.TimeUnit.*
import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap

class Alg1Suite extends AnyFunSuite with Matchers:

  ignore("!Fibonacci v1: recursion"):
    def fib(n: Int): Long =
      if n == 0 then 0
      else if n == 1 then 1
      else fib(n - 1) + fib(n - 2)
    time(MILLISECONDS) { fib(50) } shouldBe 12_586_269_025L

  test("Fibonacci v2: recursion tailrec"):
    def fib(n: Int): Long =
      @tailrec
      def loop(n: Long, prev: Long, curr: Long): Long =
        if n == 0 then prev
        else loop(n - 1, curr, prev + curr)
      loop(n, 0, 1)
    time(MILLISECONDS) { fib(50) } shouldBe 12_586_269_025L

  test("Fibonacci v3: memoisation - dynamic top-down"):
    val cache = HashMap.empty[Int, Long]
    def fib(n: Int): Long =
      if n == 0 then 0
      else if n == 1 then 1
      else if (cache.contains(n)) then cache(n)
      else
        val res = fib(n - 1) + fib(n - 2)
        cache.put(n, res)
        res
    time(MILLISECONDS) { fib(50) } shouldBe 12_586_269_025L

  test("Fibonacci v3: dynamic bottom-up"):
    def fib(n: Int): Long =
      var a: Long = 0
      var b: Long = 1
      if n == 0 then 0
      else if n == 1 then 1
      else
        for (i <- 2 to n)
          val res: Long = a + b
          a = b
          b = res
      b
    time(MILLISECONDS) { fib(50) } shouldBe 12_586_269_025L
