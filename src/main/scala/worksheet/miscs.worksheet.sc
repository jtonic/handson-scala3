val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9)

numbers map (_ * 2)

numbers partition (_ % 2 == 0)

val strings = List("a", "aa", "aaa", "aaaa")

strings groupBy (_.length)


numbers foreach println

// require(numbers.length >= 20, "The list must have more than 20 elements")

println("Hello world!")

1 to 10 by 2

val a: String = null
a
