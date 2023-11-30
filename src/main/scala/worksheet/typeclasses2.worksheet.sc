trait Numeric[T]:
  def add(a: T, b: T): T
  def multiply(a: T, b: T): T

implicit val intNumeric: Numeric[Int] =
  new Numeric[Int]:
    def add(a: Int, b: Int): Int = a + b
    def multiply(a: Int, b: Int): Int = a * b

val a = 1
val b = 2

val sum = intNumeric.add(a, b)
val mult = intNumeric.multiply(a, b)

sum
mult

object ops:
  implicit class NumericOp[T](a: T)(implicit num: Numeric[T]):
    def add(b: T) = num.add(a, b)
    def multiply(b: T) = num.multiply(a, b)
    def +(b: T) = add(b)
    def *(b: T) = multiply(b)

val list = List(1, 2, 3, 4)

def sumAll[T](lst: List[T])(implicit num: Numeric[T]): T =
  val a = lst.reduce((a, b) => num.add(a, b))
  a

sumAll(list)

import ops.*

val x: Int = 10
val y: Int = 20
x.add(y)
x + y
x * y
