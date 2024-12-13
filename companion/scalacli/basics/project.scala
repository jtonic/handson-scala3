//> using jvm 21
//> using scala 3.6.2
//> using files ./file1.scala, ./file2.scala

@main def main =
  import Business1.exec as exec1
  import Business2.exec as exec2
  val a = 10
  val b = 20

  val c = a + b

  println(s"The sum of $a and $b is $c")
  exec1()
  exec2()
  println("From main!!!")
