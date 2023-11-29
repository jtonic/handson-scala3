package ro.jtonic.handson.scala3.hollywood

package model:
  case class Movie(title: String, year: Int, rating: Int, actors: List[String] = Nil)
  case class Actor(name: String, age: Int)
