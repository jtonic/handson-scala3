package ro.jtonic.handson.scala3

import scala.io.StdIn.readLine

object App {

  def main(args : Array[String]): Unit =
    println( "What's your name?" )
    readLine() match {
      case null => println("You didn't enter a name")
      case name => println(s"Hello, $name!")
    }
}
