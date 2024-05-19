import org.scalatest.tools.Runner
import org.scalatest.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers as ShouldMatchers

class ElementSuite extends AnyFunSuite with ShouldMatchers{
  test("Non empty list should not be empty") {
    val numbers = List(1, 2, 3)
    assert(numbers.nonEmpty)
    numbers.size.shouldBe(3)
  }
}

def runTests() = {
  val testSuite = new ElementSuite
  val testResults = Runner.run(Array("-s", classOf[ElementSuite].getName))
  println(testResults)
}

classOf[ElementSuite].getName

(new ElementSuite).execute()
