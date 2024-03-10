//> using jvm 21
//> using scala 3.3.3

//> using files ./file1.scala, ./file2.scala

@main def main =
  import Business1.exec as exec1
  import Business2.exec as exec2
  exec1()
  exec2()
  println("From main!!!")
