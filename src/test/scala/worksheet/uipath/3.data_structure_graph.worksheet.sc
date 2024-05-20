import ro.jtonic.handson.scala3.util.math.*
import ro.jtonic.handson.scala3.util.function.|>
import ro.jtonic.handson.scala3.algorithms.datastructure.graph.model.{Graph, Node, Edge}
import ro.jtonic.handson.scala3.algorithms.datastructure.graph.Traverse._
import ro.jtonic.handson.scala3.algorithms.datastructure.graph.MST

import ro.jtonic.handson.scala3.util.benchmark.time

val ndNwGraph = Graph[Int](isDirected = false, isWeighted = false)
val n11 = ndNwGraph += 1
val n12 = ndNwGraph += 2
val n13 = ndNwGraph.addNode(3)
val n14 = ndNwGraph.addNode(4)
val n15 = ndNwGraph.addNode(5)
val n16 = ndNwGraph.addNode(6)
val n17 = ndNwGraph.addNode(7)
val n18 = ndNwGraph.addNode(8)

ndNwGraph += (n11, n12)
ndNwGraph += (n11, n13)
ndNwGraph.addEdge(n12, n14)
ndNwGraph.addEdge(n13, n14)
ndNwGraph.addEdge(n14, n15)
ndNwGraph.addEdge(n15, n16)
ndNwGraph.addEdge(n15, n17)
ndNwGraph.addEdge(n15, n18)
ndNwGraph.addEdge(n16, n17)
ndNwGraph.addEdge(n17, n18)

ndNwGraph

println(ndNwGraph)

val dwGraph = Graph[Int](true, true)
val n21 = dwGraph += 1
val n22 = dwGraph += 2
val n23 = dwGraph += 3
val n24 = dwGraph += 4
val n25 = dwGraph += 5
val n26 = dwGraph += 6
val n27 = dwGraph += 7
val n28 = dwGraph += 8

dwGraph.addEdge(n21, n22, 9)
dwGraph.addEdge(n21, n23, 5)
dwGraph.addEdge(n22, n21, 3)
dwGraph.addEdge(n22, n24, 18)
dwGraph.addEdge(n23, n24, 12)
dwGraph.addEdge(n24, n22, 2)
dwGraph.addEdge(n24, n25, 9)
dwGraph.addEdge(n24, n28, 8)
dwGraph.addEdge(n25, n24, 9)
dwGraph.addEdge(n25, n26, 2)
dwGraph.addEdge(n25, n27, 5)
dwGraph.addEdge(n25, n28, 3)
dwGraph.addEdge(n26, n27, 1)
dwGraph.addEdge(n27, n25, 4)
dwGraph.addEdge(n27, n28, 6)
dwGraph.addEdge(n28, n25, 3)

dwGraph

// Find nodes by index
dwGraph.getNodeByIndex(7)
dwGraph.getNode(5)
val node11 = dwGraph(10)
val node4 = dwGraph(3)
node11.orNull

val a: Int = node4.fold[Int](-1)(_.data)

// traverse using DFS
time():
  val traversed = dwGraph.dfs()
  traversed.foreach(println)

time():
  val traversed = dwGraph.bfs()
  traversed.foreach(println)

// Minimum Spanning Tree with Kruskal algorithm

def feedGraphKruskal(): Graph[Int] =
  val graph = Graph[Int](true, true)
  val n1 = graph += 1
  val n2 = graph += 2
  val n3 = graph += 3
  val n4 = graph += 4
  val n5 = graph += 5
  val n6 = graph += 6
  val n7 = graph += 7
  val n8 = graph += 8

  graph.addEdge(n1, n2, 3)
  graph.addEdge(n1, n3, 5)
  graph.addEdge(n2, n4, 4)
  graph.addEdge(n3, n4, 12)
  graph.addEdge(n4, n5, 9)
  graph.addEdge(n4, n8, 8)
  graph.addEdge(n5, n6, 4)
  graph.addEdge(n5, n7, 5)
  graph.addEdge(n5, n8, 1)
  graph.addEdge(n6, n7, 6)
  graph.addEdge(n7, n8, 20)
  graph

val feed = feedGraphKruskal()
val mst = feed |> MST.kruskal
mst.foreach(println)
println(s"Total weight: ${mst.sumBy(_.weight)}")

// delete from here
