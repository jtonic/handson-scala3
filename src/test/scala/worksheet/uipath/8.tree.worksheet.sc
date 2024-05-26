import scala.collection.mutable.ArrayBuffer
import org.scalatest.matchers.should.Matchers.*

object Model

  trait Tree[T](val value: T):
    var parent: Node[T] = null

  case class Leaf[T] private(_value: T) extends Tree[T](_value)

  case class Node[T] private (_value: T) extends Tree[T](_value):
    private val _children: ArrayBuffer[Tree[T]] = ArrayBuffer.empty
    def children: List[Tree[T]] = _children.toList
    def addNode(node: Tree[T]): Unit =
      this._children.toBuffer.+=:(node)
      node.parent = this

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


val leaf2 = tree.children(0).asInstanceOf[Node[Int]].children(0)
leaf2.value
leaf2.parent.value shouldBe 3

val leaf35 = tree.children(2).asInstanceOf[Node[Int]].children(0).asInstanceOf[Node[Int]].children(0)
leaf35 shouldBe Leaf(35)
leaf35.parent.value shouldBe 20
