package models.jbehave

import org.specs2.mutable.Specification
import play.api.libs.json.Json

class JBehaveCompositesSpec extends Specification {

  val compositePackage = "com.technologyconversations.test"
  val compositeClass = "MyComposites"
  val steps = List("Given something", "When else", "Then OK")
  val composite = JBehaveComposite("Given this is my composite", steps)
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

  "JBehaveComposites#toText" should {

    "return jBehaveComposites view" in {
      jBehaveComposites.toText(json) must beEqualTo(text)
    }

  }

//  "JBehaveComposites#toJson" should {
//
//    "return "
//
//  }

}
