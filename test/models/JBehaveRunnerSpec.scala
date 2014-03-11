package models

import org.specs2.mutable.Specification
import java.io.File
import java.util
import org.jbehave.core.reporters.Format
import scala.collection.JavaConversions._

class JBehaveRunnerSpec extends Specification {

  val storiesDirPath = "test/stories"
  val storiesPath = s"$storiesDirPath/**/*.story"
  val reportsPath = "/test/jbehave/"
  val steps = new util.ArrayList[String]()
  steps.add("com.technologyconversations.bdd.steps.WebSteps")
  val runner = new JBehaveRunner(storiesPath, steps, reportsPath)

  "JBehaveRunner#newInstance" should {

    "store storyPath" in {
      runner.getStoryPath must be equalTo storiesPath
    }

    "store stepsInstanceNames" in {
      runner.getStepsInstances must have size 1
    }

    "store reportsPath" in {
      runner.getReportsPath must be equalTo reportsPath
    }

  }

  "JBehaveRunner#storyPaths" should {

    "return all stories" in {
      val expectedSize = new File(storiesDirPath).list.count(_.endsWith(".story"))
      runner.storyPaths must have size expectedSize
    }

  }

  "JBehaveRunner#setStepsInstancesFromNames" should {

    "return array of instances" in {
      runner.getStepsInstances must have size 1
    }

    "throw exception if class does NOT exist" in {
      val testSteps = new util.ArrayList[String]()
      testSteps.add("com.technologyconversations.bdd.steps.NonExistentSteps")
      new JBehaveRunner(storiesPath, testSteps, reportsPath) should throwA[Exception]
    }

  }

  "JbehaveRunner#configuration" should {

    val reporter = runner.configuration().storyReporterBuilder()

    "use reportsPath" in {
      reporter.relativeDirectory() must be equalTo reportsPath
    }

    "use console, html and xml formats" in {
      reporter.formats().toList must contain(Format.CONSOLE, Format.HTML, Format.XML)
    }

  }

}
