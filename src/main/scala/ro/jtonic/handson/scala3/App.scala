package ro.jtonic.handson.scala3

import scala.io.StdIn.readLine

object App:

  def main(args: Array[String]): Unit =
    println("What's your name?")
    println("How are you?")
    readLine() match
      case null => println("No name provided!")
      case name => println(s"Hello, $name!")
