// implement a graph with adjacency list
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

  override def toString: String =
    val sb = new StringBuilder
    sb.append(s"Graph\n\tisDirected: $isDirected\n\tisWeighted: $isWeighted\n")
    sb.append("Nodes:\n")
    nodes.foreach(n => sb.append(s"\t$n\n"))
    sb.append("Edges:\n")
    getEdges.foreach(e => sb.append(s"\t$e\n"))
    sb.toString


val graph = Graph[Int](isDirected = false, isWeighted = false)
val n1 = graph += 1
val n2 = graph += 2
val n3 = graph.addNode(3)
val n4 = graph.addNode(4)
val n5 = graph.addNode(5)
val n6 = graph.addNode(6)
val n7 = graph.addNode(7)
val n8 = graph.addNode(8)


graph += (n1, n2)
graph += (n1, n3)
graph.addEdge(n2, n4)
graph.addEdge(n3, n4)
graph.addEdge(n4, n5)
graph.addEdge(n5, n6)
graph.addEdge(n5, n7)
graph.addEdge(n5, n8)
graph.addEdge(n6, n7)
graph.addEdge(n7, n8)

println(graph)
