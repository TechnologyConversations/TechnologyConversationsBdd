package models.jbehave

import org.specs2.mutable.Specification
import java.io.File
import play.api.libs.json.JsValue
import org.jbehave.core.steps.{StepCandidate, Steps}
import org.specs2.matcher.JsonMatchers
import com.technologyconversations.bdd.steps.WebSteps
import com.technologyconversations.bdd.steps.Dummy

class JBehaveStepsSpec extends Specification with JsonMatchers {
  
  val className = classOf[WebSteps].getName
  val noBddClassName = classOf[Dummy].getName

  "JBehaveSteps#stepsJars" should {

    "return a list of all JARs" in {
      val list = JBehaveSteps().stepsJars
      list must beAnInstanceOf[List[File]]
      list must not be empty
      list(0) must beAnInstanceOf[File]
    }

  }

  "JBehaveSteps#classes" should {

    val list = JBehaveSteps().classes

    "return a list of all classes" in {
      list.size must beGreaterThan(0)
    }

    "return classes that have at least one method annotated with Given, When or Then" in {
      list must containPattern(".*WebSteps")
    }

    "return NOT return classes that do not have at least one method annotated with Given, When or Then" in {
      list must not containPattern ".*Dummy"
    }

  }

  "JBehaveSteps#hasSteps" should {

    "return true if class has at least one Given, When or Then annotation" in {
      JBehaveSteps().hasSteps(className) must beTrue
    }

    "return false if class does NOT have at least one Given, When or Then annotation" in {
      JBehaveSteps().hasSteps(noBddClassName) must beFalse
    }

  }
  
  "JBehaveSteps#classParams" should {
    
    "return List[String]" in {
      JBehaveSteps().classParams(className) must beAnInstanceOf[List[String]]
    }

    "return empty list if class does not have BddParams annotation" in {
      JBehaveSteps().classParams(noBddClassName) must have size 0
    }

    "return list of parameters if class has BddParams annotation" in {
      val list = JBehaveSteps().classParams(className)
      list must not be empty
      list must contain("webDriver")
    }
    
  }

  "JBehaveSteps#classParamsMap" should {

    val list = JBehaveSteps().classParamsMap(className)

    "return List[Map]" in {
      list must beAnInstanceOf[List[Map[String, String]]]
      list(0) must beAnInstanceOf[Map[String, String]]
    }

    "return List with Map containing key value" in {
      list(0) must havePair("key" -> "webDriver")
    }


  }

  "JBehaveSteps#steps" should {

    "return a list of all steps" in {
      val list = JBehaveSteps().steps
      list must beAnInstanceOf[List[Steps]]
      list.size must beGreaterThan(0)
      list(0) must beAnInstanceOf[Steps]
    }

  }

  "JBehaveSteps#stepsCandidates" should {

    "return a list of all steps candidates" in {
      val list = JBehaveSteps().stepsCandidates
      list must beAnInstanceOf[List[Steps]]
      list.size must beGreaterThan(0)
      list(0) must beAnInstanceOf[StepCandidate]
    }

  }


  "JBehaveSteps#stepsToJson" should {

    val json = JBehaveSteps().stepsToJson
    val jsonString = json.toString()

    "return JSON" in {
      json must beAnInstanceOf[JsValue]
    }

    "return JSON with type nodes" in {
      jsonString must /("steps") */("type" -> "GIVEN")
      jsonString must /("steps") */("type" -> "WHEN")
      jsonString must /("steps") */("type" -> "THEN")
    }

    "return JSON with step nodes" in {
      jsonString must /("steps") */("step" -> "Given .*".r)
      jsonString must /("steps") */("step" -> "When .*".r)
      jsonString must /("steps") */("step" -> "Then .*".r)
    }

  }

  "JBehaveSteps#classesToJson" should {

    val json = JBehaveSteps().classesToJson
    val jsonString = json.toString()

    "return JSON" in {
      json must beAnInstanceOf[JsValue]
    }

    "return JSON with name node" in {
      jsonString must /("classes") */("name" -> "WebSteps")
    }

    "return JSON with fullName node" in {
      jsonString must /("classes") */("fullName" -> endWith("WebSteps"))
    }

    "return JSON with params node" in {
      jsonString must /("classes") */("params") */("key" -> "webDriver")
    }

  }

}
