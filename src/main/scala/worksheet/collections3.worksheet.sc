import cats.instances.arraySeq
import scala.collection.immutable.LinearSeq
import scala.util.Try.*

// ------------------------------------------
// Lazy List
// ------------------------------------------

val numbersLazyList = 1 #:: 2 #:: 3 #:: LazyList.empty

def fibonacci(a: Int, b: Int): LazyList[Int] = a #:: fibonacci(b, a + b)

val fibs = fibonacci(1, 1).take(7)
fibs.toList

// ------------------------------------------
// Vector
// ------------------------------------------

val numbersVector = 1 +: 2 +: 3 +: 4 +: Vector.empty
numbersVector.updated(2, 30)

// ------------------------------------------
// Immutable Queue
// ------------------------------------------
import scala.collection.immutable.Queue

val emptyQ = Queue.empty
val q1 = emptyQ.enqueue(1)
val q123 = q1.enqueueAll(List(2, 3))
val q12 = q123.dequeue._1

// ------------------------------------------
// Mutable Queue
// ------------------------------------------
import scala.collection.mutable

val numbersMQ = mutable.Queue.empty[Int]
numbersMQ += 1
numbersMQ ++= List(2, 3)
numbersMQ.dequeue
numbersMQ

// ------------------------------------------
// Stack
// ------------------------------------------

val numbersStack = mutable.Stack.empty[Int]
numbersStack.push(1)
numbersStack.push(2)
numbersStack.push(3)
numbersStack.pop
numbersStack

// ------------------------------------------
// Array
// ------------------------------------------

val numbersArray = Array(1, 2, 3, 4, 5)
numbersArray.update(2, 30)
numbersArray
  .filter(_ % 2 == 0)
  .map(_ * 10)
  .toList
numbersArray.toList
val numbersArraySeq: mutable.ArraySeq[Int] = numbersArray

numbersArraySeq.toArray == numbersArray
numbersArraySeq.toArray eq numbersArray

import scala.reflect.ClassTag
def evenElems[T: ClassTag](xs: Vector[T]): Array[T] =
  val arr = new Array[T]((xs.length + 1) / 2)
  for i <- 0 until xs.length by 2 do
    arr(i / 2) = xs(i)
  arr

evenElems(Vector(1, 2, 3, 4, 5))
evenElems(Vector("this", "is", "a", "test", "run"))

// ------------------------------------------
// Range
// ------------------------------------------

val digits = 1 to 9
val digits2 = 1 until 10
val oddDigits = 1 to 9 by 2
val evenDigits = 2 to 10 by 2

digits.toList
digits2.toList
oddDigits.toList
evenDigits.toList

// ------------------------------------------
// String Builder
// ------------------------------------------

val sb = new StringBuilder
sb += 'a'
sb ++= "bcdef"
sb.toString

// ------------------------------------------
// String
// ------------------------------------------
val s = "Hello, World!"
s.filter(_ != 'l')
  .map(_.toUpper)
  .slice(0, 7)
  .reverse
val sSeq: Seq[Char] = s

// ------------------------------------------
// Collections equality
// ------------------------------------------
import scala.collection.*
val numbersLst = List(1, 2, 3, 4, 5)
val numbersVec = Vector(1, 2, 3, 4, 5)
val numbersSet = Set(1, 2, 3, 4, 5)
val numbersArr = Array(1, 2, 3, 4, 5)
numbersLst == numbersVec
numbersLst eq numbersVec
// numbersArr == numbersLst //does not compile
// numbersLst == numbersSet //does not compile

val numbersSSet = SortedSet(1, 2, 3, 5, 4)
val numbersTreeSet = mutable.TreeSet(1, 2, 5, 3, 4)
val numbersMutSet = mutable.Set(1, 5, 2, 3, 4)
numbersSSet == numbersSet
numbersTreeSet == numbersSet
numbersMutSet == numbersSet
