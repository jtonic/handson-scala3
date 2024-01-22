package ro.jtonic.handson.scala3

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.GivenWhenThen

class TVSet extends AnyFeatureSpec with Matchers with GivenWhenThen {
  Feature("TV power button") {
    Scenario("User presses power button when TV is off") {
      Given("a TV set that is switched off")
        val a = 10
      When("the power button is pressed")
        val b = 20
      Then("the TV should switch on")
        val c = a + b
        c mustBe (30)
    }
  }
}
