import cats.conversions.all
val nums = 1 :: 2 :: 3 :: 4 :: Nil
val fruits = "apple" :: "banana" :: "orange" :: Nil
val otherFruits = List("pears", "grapes", "plums", "peaches")
val someFurtherFruits = List("kiwi", "pineapple", "mango", "papaya")
val allFruits = fruits ::: otherFruits ::: someFurtherFruits


val diag3: List[List[Int]] =
  (1 :: 0 :: 0 :: Nil) ::
  (0 :: 1 :: 0 :: Nil) ::
  (0 :: 0 :: 1 :: Nil) :: Nil

val diagg3 =
  List(
    List(1, 0, 0),
    List(0, 1, 0),
    List(0, 0, 1)
  )

val empty: List[Nothing] = Nil

empty.isEmpty
nums.head
nums.tail

fruits.isEmpty
fruits.head
fruits.tail
fruits.init
fruits.last
diag3.head.head


// List deconstruction
val List(a, b, c) = fruits
val d :: rest = fruits : @unchecked


// List concatenation
def concatenation [T] (xs: List[T], ys: List[T]): List[T] =
  xs match
    case List() => ys
    case x :: xs1 => x :: concatenation(xs1, ys)

concatenation(fruits, otherFruits)

// List length
def length [T] (xs: List[T]): Int = xs match
  case List() => 0
  case x :: xs1 => 1 + length(xs1)

length(fruits)

// reverse


fruits.head == fruits.reverse.last
fruits.last == fruits.reverse.head
fruits.reverse.tail == fruits.init.reverse
fruits.reverse.init == fruits.tail.reverse

def rev [T] (xs: List[T]): List[T] = xs match
  case Nil => xs
  case x :: xs1 => rev(xs1) ::: List(x)

rev(fruits)

fruits.drop(1) == fruits.tail
fruits.take(1) == fruits.head :: Nil
fruits.splitAt(2)

fruits(2)

fruits.drop(2).head
fruits.indices

fruits.map(_.toList).flatten

fruits.indices.zip(fruits)
fruits.zipWithIndex.unzip
fruits.mkString(", ")
fruits.partition(_.startsWith("a"))
fruits.find(_.startsWith("straw"))
fruits.find(_.startsWith("ban"))


allFruits
allFruits.takeWhile(_.length() >= 5)
allFruits.dropWhile(fruit => fruit.startsWith("a") || fruit.startsWith("o"))


List.empty[Int]
List.range(1, 5)
List.fill(5)("hello")
List.tabulate(5)(n => n * n)
