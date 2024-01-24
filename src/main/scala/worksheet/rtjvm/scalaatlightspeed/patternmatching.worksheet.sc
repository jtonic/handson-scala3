val anInteger = 10
val order = anInteger match
  case 1 => "first"
  case 2 => "second"
  case 3 => "third"
  case _ => s"${anInteger}th"

case class Person(name: String, age: Int)

val bob = Person("Bob", 60)

val greeting = bob match
  case Person(n, a) => s"Hello $n, your age is $a"

// val aTuple = ("Metallica", "Rock")
val aTuple: (String, String) = null
aTuple match
  case (band, genre) => s"$band belongs to the the $genre genre"
  case null          => "Missing band and genre information"

// val aList: List[Int] = null
val aList = List(1, 2, 3, 4)
aList match
  case _ :: 2 :: _ => "List containing 2 on its 2nd position"
  case null        => "Null list"
  case _           => "Unhandled list"
