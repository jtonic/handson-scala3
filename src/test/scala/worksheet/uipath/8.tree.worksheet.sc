import scala.collection.mutable.ArrayBuffer
import org.scalatest.matchers.should.Matchers.*

object Model

  trait Tree[T](val value: T):
    var parent: Node[T] = null
    def depth: Int = _depth
    protected var _depth = 0
    override def toString() =
      s"value: $value, depth: ${depth}, parent: [$parent]"


  case class Leaf[T] private(_value: T) extends Tree[T](_value)

  case class Node[T] private (_value: T) extends Tree[T](_value):
    private val _children: ArrayBuffer[Tree[T]] = ArrayBuffer.empty
    def children: List[Tree[T]] = _children.toList
    def addNode(node: Tree[T]): Unit =
      if _children.isEmpty then _depth += 1
      this._children.addOne(node)
      node.parent = this
    override def toString(): String =
      s"""
      ${super.toString()}, children: ${this._children.size}
      """.stripLeading()

  object Leaf:
    def apply[T](value: T): Tree[T] = new Leaf(value)

  object Node:
    def apply[T](value: T, children: Tree[T]*): Node[T] =
      val n: Node[T] = new Node(value)
      n._children.addAll(children.toList)
      children.foreach(_.parent = n)
      n

object Algebra
  import Model.*

  extension [T](self: Tree[T])
    def traverse() =
      ???

    def getCommonParent(a: Tree[T]): Tree[T] =
      ???

object Data
  import Model.*, Algebra.*

  val tree = Node(10,
    Node(3,
      Leaf(2)
    ),
    Node(7,
      Leaf(5),
      Leaf(8),
      Leaf(9),
    ),
    Node(13,
      Node(20,
        Leaf(35),
        Node(33,
          Node(18),
          Node(20)
        )
      )
    ),
    Node(100,
      Leaf(17)
    ),
  )

val n1 = Node(1)
n1.addNode(Leaf(2))
n1.addNode(Leaf(3))
n1.addNode(Leaf(4))
n1

val n2 = Node(10)
n2.addNode(n1)
n2.addNode(Leaf(11))

n1
n2
n1.depth shouldBe 1

val leaf2 = tree.children(0).asInstanceOf[Node[Int]].children(0)
leaf2.value
leaf2.parent.value shouldBe 3
leaf2.depth shouldBe 2

val leaf35 = tree.children(2).asInstanceOf[Node[Int]].children(0).asInstanceOf[Node[Int]].children(0)
leaf35 shouldBe Leaf(35)
leaf35.parent.value shouldBe 20
