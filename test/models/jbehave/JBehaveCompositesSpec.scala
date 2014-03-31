package models.jbehave

import org.specs2.mutable.Specification
import play.api.libs.json.{JsValue, Json}
import org.specs2.matcher.JsonMatchers
import java.io.File
import models.noNodeMessage
import models.nodeIsIncorrectMessage

class JBehaveCompositesSpec extends Specification with JsonMatchers {

  val compositePackage = "composites.com.technologyconversations.bdd.steps"
  val compositeClass = "WebStepsComposites"
  val steps = List("Given something", "When else", "Then OK")
  val stepText = "Given this is my composite"
  val composite = JBehaveComposite(stepText, steps)
  val composites = List(composite)
  def compositesJson(composites: List[JBehaveComposite] = composites) = {
    composites.map { composite =>
      Map(
          "stepText" -> Json.toJson(composite.stepText),
          "compositeSteps" -> Json.toJson(composite.compositeSteps.map { step =>
            Map(
              "step" -> step
            )
          })
      )
    }.toList
  }
  val jsonMap = Map(
    "package" -> Json.toJson(compositePackage),
    "class" -> Json.toJson(compositeClass),
    "composites" -> Json.toJson(compositesJson())
  )
  val json = Json.toJson(jsonMap)
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

    "throw exception if package is not present" in {
      val actualJson = Json.toJson(jsonMap.filterKeys(_ != "package"))
      jBehaveComposites.classToText(actualJson) must throwA[Exception](message = noNodeMessage("package"))
    }

    "throw exception if class is not present" in {
      val actualJson = Json.toJson(jsonMap.filterKeys(_ != "class"))
      jBehaveComposites.classToText(actualJson) must throwA[Exception](message = noNodeMessage("class"))
    }

    "throw exception if class starts with a number" in {
      val actualJson = Json.toJson(jsonMap.filterKeys(_ != "class") + ("class" -> Json.toJson("1abc")))
      jBehaveComposites.classToText(actualJson) must throwA[Exception](message = nodeIsIncorrectMessage("class"))
    }

    "throw exception if class uses any character other than letters, digits, underscores and dollar signs" in {
      val jsonWithPercentage = Json.toJson(jsonMap.filterKeys(_ != "class") + ("class" -> Json.toJson("abc%")))
      jBehaveComposites.classToText(jsonWithPercentage) must throwA[Exception](message = nodeIsIncorrectMessage("class"))
      val jsonWithSpace = Json.toJson(jsonMap.filterKeys(_ != "class") + ("class" -> Json.toJson("ab c")))
      jBehaveComposites.classToText(jsonWithSpace) must throwA[Exception](message = nodeIsIncorrectMessage("class"))
    }

    "throw exception if composites > stepText does not start with Given, When or Then" in {
      val composite = JBehaveComposite("Give me something not starting with Given, When or Then", steps)
      val composites = List(composite)
      val jsonMap = Map(
        "package" -> Json.toJson(compositePackage),
        "class" -> Json.toJson(compositeClass),
        "composites" -> Json.toJson(compositesJson(composites))
      )
      val actualJson = Json.toJson(jsonMap)
      jBehaveComposites.classToText(actualJson) must throwA[Exception](message = nodeIsIncorrectMessage("stepText"))
    }

    "throw exception if composites > compositeSteps > step does not start with Given, When or Then" in {
      val composite = JBehaveComposite(stepText, steps :+ "Give me something")
      val composites = List(composite)
      val jsonMap = Map(
        "package" -> Json.toJson(compositePackage),
        "class" -> Json.toJson(compositeClass),
        "composites" -> Json.toJson(compositesJson(composites))
      )
      val actualJson = Json.toJson(jsonMap)
      jBehaveComposites.classToText(actualJson) must throwA[Exception](message = nodeIsIncorrectMessage("step"))
    }


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
