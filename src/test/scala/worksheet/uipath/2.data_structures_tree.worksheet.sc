
sealed trait Tree[A]
case class Leaf[A](value: A) extends Tree[A]
case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

val tree = Node(1, Node(2, Leaf(3), Leaf(4)), Leaf(5))

// Search for a value in a tree using recursion and depth-first search
def searchTreeDFS[A](tree: Tree[A], value: A): Boolean = tree match {
  case Leaf(v) => v == value
  case Node(v, left, right) => v == value || searchTreeDFS(left, value) || searchTreeDFS(right, value)
}

searchTreeDFS(tree, 3)
searchTreeDFS(tree, 6)


// search tree using breadth-first search
def searchTreeBFS[A](tree: Tree[A], value: A): Boolean = {
  @annotation.tailrec
  def loop(queue: List[Tree[A]]): Boolean = queue match {
    case Nil => false
    case Leaf(v) :: rest => v == value || loop(rest)
    case Node(v, left, right) :: rest => v == value || loop(rest ++ List(left, right))
  }

  loop(List(tree))
}

searchTreeBFS(tree, 3)
searchTreeBFS(tree, 6)
