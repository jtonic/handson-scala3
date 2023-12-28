import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.LinearSeq
import scala.collection.mutable

mutable.Buffer(1, 2, 3)
IndexedSeq(10, 20, 30)
LinearSeq(100, 200, 300)

// ----------------------------------------------
// iterable iterating methods
// ----------------------------------------------
val numbers = List(1, 2, 3, 4, 5)
val groups = numbers.grouped(3)
groups.next()
groups.next()

val slides = numbers.sliding(3)
slides.next()
slides.next()
slides.next()

val it = numbers.iterator
it.next()
it.next()
it.next()
it.next()
it.next()
it.hasNext

// ----------------------------------------------
// collection size related methods
// ----------------------------------------------
numbers.isEmpty
numbers.nonEmpty
numbers.size
numbers.sizeIs == 5
numbers.sizeIs >= 2
numbers.sizeCompare(5)

// ----------------------------------------------
// collection transformation methods
// ----------------------------------------------
numbers.reduce(_ + _)
numbers.foldLeft(0)(_ + _)
numbers.collect{ case x if x % 2 == 0 => x * 10 }

// ----------------------------------------------
// collection element extract methods
// ----------------------------------------------
val emptyList = List[Int]()
emptyList.headOption
emptyList.lastOption

numbers
numbers.init
numbers.slice(1, 3)
numbers.take(3)
numbers.takeRight(3)
numbers.filter(_ % 2 != 0)
numbers.filterNot(_ % 2 != 0)

numbers.splitAt(2)
numbers.span(_ % 2 != 0)

import ro.jtonic.handson.scala3.util.*
using(numbers.partition(_ % 2 == 0)) { case (even, odd) =>
  println(even)
  println(odd)
}

numbers.groupBy(_ % 3)
numbers.groupMap(_ % 3)(_ * 10)


val otherNumbersBuf = mutable.ListBuffer(1, 2, 3, 4, 5).execute { it =>
   it += 10
}

val otherNumbersSeq: Seq[Int] = otherNumbersBuf.toSeq
otherNumbersSeq.isDefinedAt(5)
otherNumbersSeq.length
0 +: otherNumbersSeq
otherNumbersBuf :+ 100

otherNumbersBuf.padTo(10, 0)
otherNumbersSeq.containsSlice(Seq(2, 3, 4))
