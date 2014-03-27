package models.jbehave

import org.specs2.mutable.Specification
import play.api.libs.json.{JsValue, Json}
import org.specs2.matcher.JsonMatchers

class JBehaveCompositesSpec extends Specification with JsonMatchers {

  val compositePackage = "composites.com.technologyconversations.bdd.steps"
  val compositeClass = "WebStepsComposites"
  val steps = List("Given something", "When else", "Then OK")
  val stepText = "Given this is my composite"
  val composite = JBehaveComposite(stepText, steps)
  val composites = List(composite)
  val compositesJson = composites.map { composite =>
    Map(
      "composite" -> Map(
        "stepText" -> Json.toJson(composite.stepText),
        "compositeSteps" -> Json.toJson(composite.compositeSteps.map { step =>
          Map(
            "step" -> step
          )
        })
      )
    )
  }.toList
  val json = Json.toJson(Map(
    "package" -> Json.toJson(compositePackage),
    "class" -> Json.toJson(compositeClass),
    "composites" -> Json.toJson(compositesJson)
  ))
//  println("xxxxxxxxx" + json)
  val text = views.html.jBehaveComposites.render(
    compositePackage,
    compositeClass,
    composites
  ).toString().trim
  val jBehaveComposites = new JBehaveComposites {
    override def content = s"""package $compositePackage
                             |
                             |import org.jbehave.core.annotations.*;
                             |
                             |public class $compositeClass {
                             |
                             |        @Given("this is my composite")
                             |        @Composite(steps = {"Given something", "When else", "Then OK"})
                             |        public void compositeStep0() { }
                             |
                             |}""".stripMargin
  }

  "JBehaveComposites#toText" should {

    "return jBehaveComposites view" in {
      jBehaveComposites.toText(json) must beEqualTo(text)
    }

  }

  "JBehaveComposites#toJson" should {

    val json = jBehaveComposites.toJson(s"$compositePackage.$compositeClass")
    val jsonString = json.toString()

    "must have package" in {
      jsonString must /("package" -> compositePackage)
    }

    "must have class" in {
      jsonString must /("class" -> compositeClass)
    }

    "must have composites > composite > stepText" in {
      jsonString must /("composites") */"composite" */("stepText" -> "(Given|When|Then).*".r)
    }

    "must have composites > composite > compositeSteps > step" in {
      jsonString must /("composites") */"composite" */"compositeSteps" */("step" -> "(Given|When|Then).*".r)
    }

  }

}
