import scala.annotation.tailrec
// -----------------------------------------------------------------------------------
// Compute the sum of two integers without using + or - operators only on each of them
// -----------------------------------------------------------------------------------
def sum(a: Int, b: Int): Int =
  if b == 0 then a else sum(a + 1, b - 1)

sum(10, 20)

// -----------------------------------------------------------------------------------
// Insertion Sort Algorithm
// -----------------------------------------------------------------------------------
def isort(xs: List[Int]): List[Int] =
  if xs.isEmpty then Nil
  else insert(xs.head, isort(xs.tail))

def insert(x: Int, xs: List[Int]): List[Int] =
  if xs.isEmpty || x <= xs.head then x :: xs
  else xs.head :: insert(x, xs.tail)

isort(List(5, 3, 12, 1, 2))

// -----------------------------------------------------------------------------------
// Greatest Common Divisor
// -----------------------------------------------------------------------------------
def gcd(a: Long, b: Long): Long =
  if b == 0 then a else gcd(b, a % b)

  gcd(10, 20)
  gcd(120, 30)
  gcd(100, 13)

// -----------------------------------------------------------------------------------
// Eliminate duplications from a list: List(1, 3, 4, 3) => List(1, 3, 4)
// Constraints:
//  - DON'T use any additional collection(s), or even any additional variable
//  - or any conversion API such as toSet (toMap), or any API do do this.
// -----------------------------------------------------------------------------------

import ListExtensions.*

val numbers = List(1, 3, 4, 3)
numbers.eliminateDuplications()


object ListExtensions:
  extension [T](lst: List[T])
    private def doEliminateDuplications(xs: List[T]): List[T] = xs match
      case List() => List()
      case head:: tail => head :: doEliminateDuplications(tail.filterNot( _ == head ))
    def eliminateDuplications(): List[T] = doEliminateDuplications(lst)
