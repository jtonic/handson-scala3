// implement a graph with adjacency list
import ro.jtonic.handson.scala3.util.Benchmark.time
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.collection
import scala.collection.mutable.ListBuffer as MList

class Node[A](val data: A):

  var index: Int = -1
  var neighbors: MList[Node[A]] = MList.empty[Node[A]]
  var weights: MList[Int] = MList.empty[Int]

  override def toString: String = s"Index: $index. Node($data). Neighbors: ${neighbors.size}. Weights: ${weights.size}"


class Edge[A](val from: Node[A], val to: Node[A], val weight: Int) :

  override def toString: String = s"Edge(${from.data}, ${to.data}, $weight)"


class Graph[A](val isDirected: Boolean = false, val isWeighted: Boolean = false):

  private val nodes: MList[Node[A]] = MList.empty

  def addNode(data: A): Node[A] =
    val node = Node(data)
    nodes += node
    updateIndexes()
    node

  def += (data: A): Node[A] = addNode(data)

  def removeNode(node: Node[A]): Unit =
    nodes -= node
    updateIndexes()
    nodes.foreach(n => removeEdge(n, node))

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
    val edges = MList.empty[Edge[A]]
    for from <- nodes do
      for ((_, idx) <- from.neighbors.zipWithIndex) do
        val weight = if idx < from.weights.size then from.weights(idx) else 0
        val edge = Edge(from = from, to = from.neighbors(idx), weight = weight)
        edges += edge
    edges.toList

  private def updateIndexes(): Unit =
    var idx = 0
    nodes.foreach(n => {
      n.index = idx; idx = idx + 1
    })

  // def getNodeByIndex(idx: Int): Option[Node[A]] =
  //   if idx >= nodes.size then None else Some(nodes(idx))

  val getNodeByIndex: (Int) => Option[Node[A]] = (idx) =>
    if idx >= nodes.size then None else Some(nodes(idx))

  def getNode = getNodeByIndex

  def apply(idx: Int): Option[Node[A]] = getNodeByIndex(idx)

  override def toString: String =
    val sb = new StringBuilder
    sb.append(s"Graph\n\tisDirected: $isDirected\n\tisWeighted: $isWeighted\n")
    sb.append("Nodes:\n")
    nodes.foreach(n => sb.append(s"\t$n\n"))
    sb.append("Edges:\n")
    getEdges.foreach(e => sb.append(s"\t$e\n"))
    sb.toString


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

//
// Find nodes by index
dwGraph.getNodeByIndex(7)
dwGraph.getNode(5)
val node11 = dwGraph(10)
val node4 = dwGraph(3)
node11.orNull

val a: Int = node4.fold[Int](-1)(_.data)


// -------------------------------------------------------
// Benchmar

time(TimeUnit.NANOSECONDS):
  (1 to 1000).toList.find(_ == 777)

time(TimeUnit.MILLISECONDS):
  (1 to 1000).toList.find(_ == 777)
