val a: Int | Null = null

a match
  case _: Int => println("Int")
  case _      => println("null")

extension [T](x: T | Null)
   inline def nn: T =
     assert(x != null)
     x.asInstanceOf[T]

val alice: String | Null = "Alice"
val noName: String | Null = null

alice.nn
noName.nn
