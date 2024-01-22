package ro.jtonic.handson.scala3

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.matcher.MustMatchers

@RunWith(classOf[JUnitRunner])
class MySpec extends Specification with MustMatchers {
  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must haveSize(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}
