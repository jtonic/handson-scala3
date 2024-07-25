package ro.jtonic.handson.scala3

import scala.io.StdIn.readLine

object App:

  def main(args: Array[String]): Unit =
    println("How are you?")
    println("What's your name?")
    readLine() match
      case null => println("No name provided!")
      case name => println(s"Hello, $name!")
