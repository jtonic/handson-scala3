import scala.collection.mutable.ListBuffer as MList
import scala.collection.mutable.ArrayBuffer

val nodes = MList[String]()
nodes += "1"
nodes += "2"
nodes ++= List("3", "4", "5")
nodes -= "3"

nodes(1)
