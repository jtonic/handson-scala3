
// 1. scala functions are simply calls to apply methons on FunctionX instances

val aFun: Function[Int, Int] = new Function1[Int, Int]:
  def apply(x: Int): Int = x + 1

aFun.apply(10)
aFun(20)

// 2. syntax sugar

val doubler = (x: Int) => x * 2
doubler(10)

// 3. HOF (Higher order functions)

val aList = List(1, 2, 3).map(x => x + 1)
val a2ndList = List(1, 2, 3).flatMap: x =>
  List(x, x * 2)

aList
a2ndList

// zipping two lists
val pairs = List(1, 2, 3).flatMap(number => List('a', 'b', 'c').map(letter => s"$number-$letter"))
pairs

// for comprehension
val pairsVariant2 = for
  number <- List(1, 2, 3)
  char <- List('a', 'b', 'c')
yield s"$number-$char"

pairsVariant2

// collections

val numbers = List(1, 2, 3, 4, 5)
0 :: numbers
0 +: numbers :+ 6

val aSeq = Seq(1, 2, 3, 4, 5)
aSeq(4)

val aVector = Vector(1, 2, 3, 4, 5)
aVector(4)

val aSet = Set(1, 2, 3, 4, 5)
aSet.contains(3)
aSet.contains(10)

val aRange = 1 to 10
aRange.map(_ * 2)
val aSecondRange = 1 until 10
aSecondRange.map(_ * 3)

val phoneBook = Map(
  ("Tony", 1234),
  "Irina" -> 23456
)
