package models

import org.specs2.mutable.Specification
import java.io.File
import org.clapper.classutil.ClassInfo
import play.api.libs.json.JsValue
import org.jbehave.core.steps.{StepCandidate, Steps}
import org.specs2.matcher.JsonMatchers

class JBehaveStepsSpec extends Specification with JsonMatchers {

  "JBehaveSteps#stepsJars" should {

    "return a list of all JARs" in {
      val list = JBehaveSteps().stepsJars
      list must beAnInstanceOf[List[File]]
      list.size must beGreaterThan(0)
      list(0) must beAnInstanceOf[File]
    }

  }

  "JBehaveSteps#stepsClasses" should {

    "return a list of all classes" in {
      val list = JBehaveSteps().stepsClasses
      list must beAnInstanceOf[List[ClassInfo]]
      list.size must beGreaterThan(0)
      list(0) must beAnInstanceOf[ClassInfo]
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


  "JBehaveSteps#toJson" should {

    val json = JBehaveSteps().toJson
    val jsonString = json.toString()

    "return JSON" in {
      json must beAnInstanceOf[JsValue]
    }

    "return JSON with the correct format" in {
      jsonString must /("steps") */("step" -> "GIVEN .*".r)
      jsonString must /("steps") */("step" -> "WHEN .*".r)
      jsonString must /("steps") */("step" -> "THEN .*".r)
    }

  }
}
