import ro.jtonic.handson.scala3.algorithms.datastructure.tree.model.{Leaf, Node, Tree}
import ro.jtonic.handson.scala3.algorithms.datastructure.tree.operations.Search.*
import org.scalatest.matchers.should.Matchers.*

val tree = Node(1, Node(2, Leaf(3), Leaf(4)), Leaf(5))

dfs(tree, 3) should be (true)
dfs(tree, 6) should be (false)

bfs(tree, 3) should be (true)
bfs(tree, 6) should be (false)
