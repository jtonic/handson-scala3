package ro.jtonic.handson.scala3.algorithms.datastructure

import scala.collection.mutable.ListBuffer as BList
import scala.collection.mutable.Queue as MQueue

package tree.model:

  sealed trait Tree[A]

  case class Leaf[A](value: A) extends Tree[A]

  case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

package tree.operations:

    import tree.model.*

    object Search:

      /**
       * search a a tree using recursion and depth-first search
       */
      def dfs[A](tree: Tree[A], value: A): Boolean =
        tree match
          case Leaf(v) => v == value
          case Node(v, left, right) => v == value || dfs(left, value) || dfs(right, value)

      /**
       * search a tree using breadth-first search
       */
      def bfs[A](tree: Tree[A], value: A): Boolean =
        @annotation.tailrec
        def loop(queue: List[Tree[A]]): Boolean =
          queue match
            case Nil => false
            case Leaf(v) :: rest => v == value || loop(rest)
            case Node(v, left, right) :: rest => v == value || loop(rest ++ List(left, right))
        loop(List(tree))

package graph.model:

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

        def getNodeByIndex: (Int) => Option[Node[A]] = (idx) =>
          if idx >= nodes.size then None else Some(nodes(idx))

        def getNode = getNodeByIndex

        def apply(idx: Int): Option[Node[A]] = getNodeByIndex(idx)

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

package graph:
  import model.*

  object Traverse:

    extension [A](graph: Graph[A])

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

  object MST:

      import model.*

      def kruskal[A](graph: Graph[A]): List[Edge[A]] =
        val edges = graph.getEdges.sortBy(_.weight)
        val result = BList.empty[Edge[A]]
        val parents = new Array[Int](graph.nodes.size)
        for i <- 0 until parents.size do parents(i) = i

        def findParent(node: Node[A]): Int =
          if parents(node.index) == node.index then node.index else findParent(graph.getNode(parents(node.index)).get)

        def union(from: Node[A], to: Node[A]): Unit =
          val fromP = findParent(from)
          val toP = findParent(to)
          parents(fromP) = toP

        for edge <- edges do
          val fromP = findParent(edge.from)
          val toP = findParent(edge.to)
          if fromP != toP then
            result += edge
            union(edge.from, edge.to)

        result.toList
