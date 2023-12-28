import scala.reflect.ClassTag

def printType[T](x: T)(using ct: ClassTag[T]): Unit =
  println(s"The type of $x is ${ct.runtimeClass}")

printType(1)

printType("abc")

printType(List(1, 2, 3))
printType(Array.empty[Int])
printType(Vector(1, 2, 3))

printType(1 -> "abc")
