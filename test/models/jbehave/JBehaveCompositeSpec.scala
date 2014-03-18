package models.jbehave

import org.specs2.mutable.Specification

class JBehaveCompositeSpec extends Specification {

  val stepWithDoubleQuote = """Given "something" is going on"""
  val stepWithParams = """When there are <param0>, <param1> and <param2> params"""
  val composite = JBehaveComposite("Given this is my composite with <param0>", List(stepWithDoubleQuote, stepWithParams))

  "JBehaveComposite#type" should {

    "return step type in form of Given, When or Then" in {
      composite.stepType must equalTo("Given")
    }

  }

  "JBehaveComposite#strippedStepText" should {

    "return step without type" in {
      composite.strippedFormattedStepText must equalTo("this is my composite with <param0>")
    }

    "return step with double quotes escaped" in {
      JBehaveComposite("""Given this is "my" composite""", List()).strippedFormattedStepText must equalTo("""this is \"my\" composite""")
    }

  }

  "JBehaveComposite#formattedCompositeSteps" should {

    "return steps with double quotes escaped" in {
      composite.formattedCompositeSteps must contain(stepWithDoubleQuote.replace("\"", "\\\""))
    }

  }

  "JBehaveComposite#params" should {
    val params = composite.params

    "return unique elements" in {
      params must beAnInstanceOf[Set[String]]
    }

    "return params from the step" in {
      params must contain("param0")
    }

    "return params from the composite steps" in {
      params must contain("param1")
    }

    "return params with < and > stripped" in {
      params must not containPattern "<.*"
      params must not containPattern ".*>"
    }

  }

}
