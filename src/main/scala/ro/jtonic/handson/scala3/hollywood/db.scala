package ro.jtonic.handson.scala3.hollywood

package db:
  import model._

  val actors = List(
    Actor("Brad Pitt", 57),
    Actor("Edward Norton", 51),
    Actor("Morgan Freeman", 84),
    Actor("Cate Blanchett", 52),
  )

  val movies = List(
    Movie("Fight Club", 1999, 9, actors = List("Brad Pitt", "Edward Norton")),
    Movie("Seven", 1995, 8, actors = List("Brad Pitt", "Morgan Freeman")),
    Movie("The Curious Case of Benjamin Button", 2008, 7, actors = List("Brad Pitt", "Cate Blanchett")),
  )
