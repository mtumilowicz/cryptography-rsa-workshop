import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class A extends AnyFeatureSpec with GivenWhenThen {

  Feature("solve the riddle") {
    Scenario("3 policemen, 3 thieves") {
      Given("left bank state")

      When("solve")

      Then("is solvable")
      1 shouldBe 1
    }
  }

}
