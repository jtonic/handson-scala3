// ------------------------------------------------------------------------
// collections view
// ------------------------------------------------------------------------

// eager
val numbers = Vector((1 to 10)*)
numbers.map(_ + 1).map(_ * 10)

// lazy
val numbersView = numbers.view
val lazyResult = numbersView.map(_ + 1).map(_ * 10)
lazyResult
lazyResult.toVector

// ------------------------------------------------------------------------
// iterator
// ------------------------------------------------------------------------

val numbersIt = List(1, 2, 3, 4, 5).iterator
for i <- numbersIt do print(i)
numbersIt.foreach(print) // empty because the iterator is already consumed

val numbersIt2 = List(1, 2, 3, 4, 5).iterator
numbersIt2.size //size of the iterator
numbersIt2.foreach(print) // empty because the iterator is already consumed

val numbersIt3 = List(1, 2, 3, 4, 5).iterator.buffered
numbersIt3.head
numbersIt3.next
numbersIt3.next
numbersIt3.next

// iterate and unfold methods
List.iterate(10, 10)(_ + 1)
List.unfold(10)(s => if s < 20 then Some(s, s + 1) else None)

// ------------------------------------------------------------------------
// conversion from scala to java and vice versa
// ------------------------------------------------------------------------
import scala.jdk.CollectionConverters._

val initialJavaList = List(1, 2, 3)
val javaList = initialJavaList.asJava
val scalaList = javaList.asScala

initialJavaList eq scalaList
initialJavaList == scalaList

// create a list of names and filter the ones that start with 'A' and count them
val names = List("Alice", "Bob", "Charlie", "David", "Eve")
names.filter(_.startsWith("A")).size

// create a case class Person with first  name, last name and age
// create a extension method to get the full named of the person
case class Person(firstName: String, lastName: String, age: Int)

extension (p: Person) def fullName = s"${p.firstName} ${p.lastName}"

val tony = Person("Tony", "Stark", 54)
tony.fullName
