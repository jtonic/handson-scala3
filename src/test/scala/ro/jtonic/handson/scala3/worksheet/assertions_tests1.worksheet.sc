import org.scalatest.tools.Runner
import org.scalatest.*
import org.scalatest.funsuite.AnyFunSuite

class ElementSuite extends AnyFunSuite {
  test("Non empty list should not be empty") {
    val numbers = List(1, 2, 3)
    assert(numbers.nonEmpty)
  }
}

def runTests() = {
  val testSuite = new ElementSuite
  val testResults = Runner.run(Array("-s", classOf[ElementSuite].getName))
  println(testResults)
}

classOf[ElementSuite].getName

(new ElementSuite).execute()
