// --------------------------------------------------
// Arrays
// --------------------------------------------------
val fiveZeroes = new Array[Int](5)

val fiveNumbers = Array(1, 2, 3, 4, 5)
fiveNumbers(0)

val otherFiveNumbers = Array(5, 4, 3, 2, 1)
otherFiveNumbers.toList

fiveNumbers(0) == otherFiveNumbers(4)


// --------------------------------------------------
// ListBuffer
// --------------------------------------------------
import scala.collection.mutable

val buf = new mutable.ListBuffer[Int]()
buf += 1
buf += 2
buf += 3
0 +=: buf
buf.toList

// --------------------------------------------------
// ArrayBuffer
// --------------------------------------------------
import scala.collection.mutable

val arrBuf = mutable.ArrayBuffer(1, 2, 3)
arrBuf += 4
0 +=: arrBuf
arrBuf.toList
arrBuf(0)


// --------------------------------------------------
// Sets
// --------------------------------------------------
import scala.collection.mutable

val aSet = Set(1, 2, 3)
aSet + 4
aSet

val anotherSet = mutable.Set(10, 20, 30)
anotherSet += 40
anotherSet ++= Set(50, 60)
anotherSet += 70
anotherSet -= 20
anotherSet --= Set(60, 70)
anotherSet

anotherSet & Set(30, 10, 200, 100)

// --------------------------------------------------
// Maps
// --------------------------------------------------

val phrase = "See Spot run! Run, Spot. Run!"

def wordsCount(phrase: String): Map[String, Int] =
  val wordsArray = phrase.split("[ !,.]+")
  val words = mutable.Map.empty[String, Int]
  for (word <- wordsArray) do
    val count = words.getOrElse(word.toLowerCase(), 0)
    words += (word.toLowerCase() -> (count + 1))
  words.toMap

wordsCount(phrase)

val numbers = Map("i" -> 1, "ii" -> 2, "iii" -> 3, "iv" -> 4, "v" -> 5, "vi" -> 6)
numbers + ("vii" -> 7)
numbers - "iii"
numbers
numbers ++ List("vii" -> 7, "viii" -> 8, "ix" -> 9)

numbers.isEmpty
numbers.keySet
numbers.keys
numbers.values

numbers.getClass()

Map.empty[String, Int].getClass()
Map(1 -> "one").getClass()
Map(2 -> "two").getClass()

import scala.collection.immutable

val otherNumbers = immutable.SortedSet(5, 4, 1, 10, 20, 100, 2, 3)
otherNumbers + 30
otherNumbers - 20

var names = Set("Tony", "Irina", "Roxana", "Magda", "Liviu")
names += "Mihai"
names -= "Irina"
names ++= List("George", "Valeriana")

names

// --------------------------------------------------
// tuples
// --------------------------------------------------
val t = (1, 3.14, "Maths")
t(0)
t(1)
t(2)

t._1
t._2
t._3


// --------------------------------------------------
// initializing collections
// --------------------------------------------------

val xs = List(3, 10, 20 , 100, 2, 3, 4)
val uniqueNumbers = xs to immutable.TreeSet
xs to Set
uniqueNumbers.toList
uniqueNumbers.toArray
