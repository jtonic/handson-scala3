import scala.collection.mutable.Queue as MQueue
import Model.Graph
import ro.jtonic.handson.scala3.dsl.DurationUtils.sleep.current
import scala.collection.mutable.ArrayBuffer
import ro.jtonic.handson.scala3.util.Benchmark.time
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.collection
import scala.collection.mutable.ListBuffer as BList

object Model:

  class Node[A](val data: A):

    var index: Int = -1
    var neighbors: BList[Node[A]] = BList.empty[Node[A]]
    var weights: BList[Int] = BList.empty[Int]

    override def toString: String = s"Index: $index. Node($data). Neighbors: ${neighbors.size}. Weights: ${weights.size}"


  class Edge[A](val from: Node[A], val to: Node[A], val weight: Int) :

    override def toString: String = s"Edge(${from.data}, ${to.data}, $weight)"


  class Graph[A](val isDirected: Boolean = false, val isWeighted: Boolean = false):

    private val _nodes: BList[Node[A]] = BList.empty

    def nodes: List[Node[A]] = _nodes.toList

    def addNode(data: A): Node[A] =
      val node = Node(data)
      _nodes += node
      updateIndices()
      node

    def += (data: A): Node[A] = addNode(data)

    def removeNode(node: Node[A]): Unit =
      _nodes -= node
      updateIndices()
      _nodes.foreach(n => removeEdge(n, node))

    def addEdge(from: Node[A], to: Node[A], weight: Int = 0): Unit =
      from.neighbors += to
      if isWeighted then from.weights += weight
      if !isDirected then
        to.neighbors += from
        if isWeighted then to.weights += weight

    // unrecommended
    def += (nodes: (Node[A], Node[A])): Unit = addEdge(nodes._1, nodes._2)

    def removeEdge(from: Node[A], to: Node[A]): Unit =
      val idxOpt = from.neighbors.zipWithIndex.find((n, idx) => n == to).map(_._2)
      if idxOpt.isEmpty then return
      val idx = idxOpt.get
      from.neighbors.remove(idx)
      if isWeighted then from.weights.remove(idx)
      if !isDirected then
        val idxOpt = to.neighbors.zipWithIndex.find((n, idx) => n == from).map(_._2)
        if idxOpt.isEmpty then return
        val idx = idxOpt.get
        to.neighbors.remove(idx)
        if isWeighted then to.neighbors.remove(idx)

    def getEdges: List[Edge[A]] =
      val edges = BList.empty[Edge[A]]
      for from <- _nodes do
        for ((_, idx) <- from.neighbors.zipWithIndex) do
          val weight = if idx < from.weights.size then from.weights(idx) else 0
          val edge = Edge(from = from, to = from.neighbors(idx), weight = weight)
          edges += edge
      edges.toList

    private def updateIndices(): Unit =
      var idx = 0
      _nodes.foreach(n => {
        n.index = idx; idx = idx + 1
      })

    override def toString: String =
      val sb = new StringBuilder
      sb.append(s"Graph\n\tisDirected: $isDirected\n\tisWeighted: $isWeighted\n")
      sb.append("Nodes:\n")
      _nodes.foreach(n => sb.append(s"\t$n\n"))
      sb.append("Edges:\n")
      getEdges.foreach(e => sb.append(s"\t$e\n"))
      sb.toString

object Extensions:

  import Model.*

  extension [A](graph: Graph[A])

    def getNodeByIndex: (Int) => Option[Node[A]] = (idx) =>
      if idx >= graph.nodes.size then None else Some(graph.nodes(idx))

    def getNode = graph.getNodeByIndex

    def apply(idx: Int): Option[Node[A]] = getNodeByIndex(idx)

    def dfs(): List[Node[A]] =
       def dfs(indices: Array[Boolean], currentNode: Node[A], traversed: BList[Node[A]]): Unit =
         traversed += currentNode
         indices(currentNode.index) = true
         for
           n <- currentNode.neighbors if !indices(n.index)
         do
           dfs(indices, n, traversed)

       val visited = new Array[Boolean](graph.nodes.size)
       val traversed = BList.empty[Node[A]]
       dfs(visited, graph.nodes(0), traversed)
       traversed.toList

    def bfs(): List[Node[A]] =
      def bfs(node: Node[A]): BList[Node[A]] =
        val visited = new Array[Boolean](graph.nodes.size)
        visited(node.index) = true
        val traversed = BList.empty[Node[A]]
        val queue = MQueue.empty[Node[A]]
        queue += node
        while queue.size > 0 do
          val next = queue.dequeue()
          traversed += next
          for
            n <- next.neighbors if !visited(n.index)
          do
            visited(n.index) = true
            queue += n
        traversed
      bfs(graph.nodes(0)).toList


import Model.*
import Extensions.*

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
