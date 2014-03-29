package models.jbehave

import org.specs2.mutable.Specification
import play.api.libs.json.{JsValue, Json}
import org.specs2.matcher.JsonMatchers
import java.io.File

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

  "JBehaveComposites#classesToJson" should {

    val classPackage = "package1"
    val className = "Class1"
    val json = jBehaveComposites.classesToJson(List(
      classPackage + File.separator + className + ".java",
      "package2" + File.separator + "Class2.java"
    ))
    val jsonString = json.toString()

    "return JsValue" in {
      json must beAnInstanceOf[JsValue]
    }

    "have package" in {
      jsonString must */("package" -> classPackage)
    }

    "have class" in {
      jsonString must */("class" -> className)
    }

  }

  "JBehaveComposites#classToText" should {

    "return jBehaveComposites view" in {
      jBehaveComposites.classToText(json) must beEqualTo(text)
    }

  }

  "JBehaveComposites#classToJson" should {

    val json = jBehaveComposites.classToJson(s"$compositePackage.$compositeClass")
    val jsonString = json.toString()

    "have package" in {
      jsonString must /("package" -> compositePackage)
    }

    "have class" in {
      jsonString must /("class" -> compositeClass)
    }

    "have composites > composite > stepText" in {
      jsonString must /("composites") */("stepText" -> "(Given|When|Then).*".r)
    }

    "have composites > composite > compositeSteps > step" in {
      jsonString must /("composites") */"compositeSteps" */("step" -> "(Given|When|Then).*".r)
    }

  }

}
