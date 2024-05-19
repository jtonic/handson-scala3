package ro.jtonic.handson.scala3.algorithms

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import ro.jtonic.handson.scala3.algorithms.datastructure.graph.model.{Graph, Node, Edge}

class Alg2GraphsSuite extends AnyFunSuite with Matchers:

  def createNdNwGraph() =
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

  val ndNwGraph = createNdNwGraph()

  test("Create a non directed and non weighted graph"):
    println(ndNwGraph)
    ndNwGraph.nodes.size shouldBe 8
    ndNwGraph.nodes.foreach(println)

  test("Find the node by index"):
    ndNwGraph.getNodeByIndex(7)
    ndNwGraph.getNode(5)
    val node11 = ndNwGraph(10)
    val node4 = ndNwGraph(3)
    node11.orNull

    val a: Int = node4.fold[Int](-1)(_.data)
