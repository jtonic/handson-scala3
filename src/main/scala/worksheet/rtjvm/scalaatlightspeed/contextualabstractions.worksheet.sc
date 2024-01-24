import shapeless.ops.hlist.Combinations
// ------------------------------------------------------------------------
// 1. Context Parameters, Arguments
// ------------------------------------------------------------------------

List(1, 2, 3, 5, 100, 12, 10, 55).sorted

given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

trait Combinator[T]:
  def combine(a: T, b: T): T

def combineAll[A](lst: List[A])(using combinator: Combinator[A]): A =
  lst.reduce((a, b) => combinator.combine(a, b))

val c1 = combineAll(List(1, 2, 3, 4))(using new Combinator[Int]:
  def combine(a: Int, b: Int): Int = a + b
)
c1

given Combinator[Int] with
  def combine(a: Int, b: Int): Int = a + b

combineAll(List(10, 20, 30, 40))

given strCombinator: Combinator[String] = new Combinator[String]:
  def combine(a: String, b: String): String = a + b

combineAll(List("I", " love", " Scala 3"))

// ------------------------------------------------------------------------
// 2. Extension Methods
// ------------------------------------------------------------------------

extension (str: String)
  def greeting() = s"Hello $str"

"Tony".greeting()

extension [A](lst: List[A])
  def combineAllValues(using combinator: Combinator[A]) = lst.reduce(combinator.combine)

List(100, 200, 300, 400).combineAllValues
