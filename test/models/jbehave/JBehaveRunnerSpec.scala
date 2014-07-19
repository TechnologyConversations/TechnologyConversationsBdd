package models.jbehave

import org.specs2.matcher.{PathMatchers, FileMatchers}
import org.specs2.mutable.Specification
import java.io.File
import org.jbehave.core.reporters.Format
import scala.collection.JavaConversions._
import com.technologyconversations.bdd.steps.WebSteps
import models.RunnerClass

class JBehaveRunnerSpec extends Specification with PathMatchers with FileMatchers {

  val storiesDirPath = "test/stories"
  val storyPaths = List(s"$storiesDirPath/**/*.story")
  val reportsPath = "/test/jbehave/"
  val params = Map("webDriver" -> "firefox", "webUrl" -> "http://www.technologyconversations.com")
  val steps = List(RunnerClass("com.technologyconversations.bdd.steps.WebSteps", params))
  val runner = new JBehaveRunner(storyPaths, steps, List(), reportsPath)

  "JBehaveRunner#newInstance" should {

    "store storyPaths" in {
      runner.getStoryPaths must be equalTo storyPaths
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

  "JBehaveRunner#setStepsInstances" should {

    "return array of instances" in {
      runner.getStepsInstances must have size 1
    }

    "throw exception if class does NOT exist" in {
      val testSteps = List(
        RunnerClass("com.technologyconversations.bdd.steps.NonExistentSteps", Map())
      )
      new JBehaveRunner(storyPaths, testSteps, List(), reportsPath) should throwA[Exception]
    }

    "have all params set" in {
      val webSteps = runner.getStepsInstances.toList(0).asInstanceOf[WebSteps]
      webSteps.getParams.toMap must havePair("webDriver" -> "firefox")
    }

  }

  "JBehaveRunner#configuration" should {

    val reporter = runner.configuration().storyReporterBuilder()

    "use reportsPath" in {
      reporter.relativeDirectory() must be equalTo reportsPath
    }

    "use console, html and xml formats" in {
      reporter.formats().toList must contain(Format.CONSOLE, Format.HTML, Format.XML)
    }

  }

  "JBehaveRunner#getSourceDir" should {

    "return File with target directory prefixed to the path" in {
      val path = "some/path"
      val sourceDir = runner.getSourceDir(path)
      val expected = new File(s"target/$path").getAbsolutePath
      sourceDir.getPath must beEqualToIgnoringSep(expected)
    }

  }

  "JBehaveRunner#getDestinationDir" should {

    "return File created from the path" in {
      val path = "some/path"
      val destinationDir = runner.getDestinationDir(path)
      val expected = new File(path).getAbsolutePath
      destinationDir.getPath must beEqualToIgnoringSep(expected)
    }

    "return File with target/universal/stage removed" in {
      val destinationDir = runner.getDestinationDir("some/target/universal/stage/path")
      val expected = new File("some/path").getAbsolutePath
      destinationDir.getPath must beEqualToIgnoringSep(expected)
    }

  }

}

