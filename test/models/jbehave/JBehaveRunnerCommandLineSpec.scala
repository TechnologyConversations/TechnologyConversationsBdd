package models.jbehave

import com.technologyconversations.bdd.steps.{FileSteps, WebSteps}
import models.RunnerClass
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scala.collection.JavaConversions._
import java.io.File

class JBehaveRunnerCommandLineSpec extends Specification with Mockito {

  val runner = new JBehaveRunnerCommandLine();
  val storyPath1 = "path/to/my.story"
  val storyPath2 = "path/to/my/story/with/ant/pattern/**/*.story"
  val storyPaths = s"$storyPath1,$storyPath2"
  val reportsPath = "path/to/reports"

  "JBehaveRunnerCommandLine#verifyArguments" should {

    val args = Array(storyPaths, "STEPS_CLASSES", "STEPS_PARAMS", "COMPOSITES_PATH", "REPORTS_PATH")

    "throw an exception when there are NO arguments" in {
      val args: Array[String] = Array()
      runner.verifyArguments(args) must throwA[IllegalArgumentException]
    }

    "throw an exception when there are NOT at least five arguments" in {
      val args: Array[String] = Array("STORY_PATHS", "STEPS_CLASSES", "STEPS_PARAMS", "COMPOSITES_PATH")
      runner.verifyArguments(args) must throwA[IllegalArgumentException]
    }

    "NOT throw an exception when there are at least five argument" in {
      runner.verifyArguments(args) must not(throwA[IllegalArgumentException])
    }

  }

  "JBehaveRunnerCommandLine#argToList" should {

    val element1 = "public/stories/**/*.story"
    val element2 = "public/stories/some_other.story"
    val defaultValue = s"$element1,$element2"

    "return the list with the default value when the argument is empty" in {
      val expected = List(element1, element2)
      val actual = runner.argToList("", defaultValue).toList
      actual must containTheSameElementsAs(expected)
    }

    "return the list with the default value when the argument is null" in {
      val expected = List(element1, element2)
      val actual = runner.argToList(null, defaultValue).toList
      actual must containTheSameElementsAs(expected)
    }

    "return the list of paths" in {
      val expected = List(storyPath1, storyPath2)
      val actual = runner.argToList(storyPaths, defaultValue).toList
      actual must containTheSameElementsAs(expected)
    }

  }

  "JBehaveRunnerCommandLine#argToMap" should {

    "return the empty Map when the argument is empty" in {
      val expected = Map()
      val actual = runner.argToMap("").toMap
      actual must beEmpty
    }

    "return the empty Map when the argument is null" in {
      val actual = runner.argToMap(null).toMap
      actual must beEmpty
    }

    "return the Map of parameters" in {
      val actual = runner.argToMap("key1=value1,key2=value2").toMap
      actual must havePairs("key1" -> "value1", "key2" -> "value2")
    }

  }

  "JBehaveRunnerCommandLine#getStoryPaths" should {

    "call the argToList method with default value public/stories/**/*.story" in {
      val defaultValue = "public/stories/**/*.story"
      val s = spy(new JBehaveRunnerCommandLine)
      val arg = "ARG"
      val expected = List(defaultValue)
      s.argToList(arg, defaultValue) returns expected
      val actual = s.getStoryPaths(arg).toList
      actual must containTheSameElementsAs(expected)
    }

  }

  "JBehaveRunnerCommandLine#getStepClasses" should {

    val class1Name = classOf[WebSteps].getName
    val class2Name = classOf[FileSteps].getName
    val defaultClassesList = List(class1Name, class2Name)
    val defaultClasses = defaultClassesList.mkString(",")
    val defaultParamsMap = Map("key1" -> "value1", "key2" -> "value2")
    val defaultParams = "key1=value1,key2=value2"

    "return the List of RunnerClass" in {
      val s = spy(new JBehaveRunnerCommandLine)
      val arg1 = "ARG1"
      val arg2 = "KEY=VALUE"
      s.argToList(arg1, defaultClasses) returns defaultClassesList
      s.argToMap(arg2) returns defaultParamsMap
      val expected = List(
        RunnerClass(class1Name, defaultParamsMap),
        RunnerClass(class2Name, defaultParamsMap)
      )
      val actual = s.getStepClasses(arg1, arg2).toList
      actual must containTheSameElementsAs(expected)
    }

  }

  "JBehaveRunnerCommandLine#getCompositesPaths" should {

    "call the argToList method with default value composites/**/*.groovy" in {
      val defaultValue = "composites/**/*.groovy"
      val s = spy(new JBehaveRunnerCommandLine)
      val arg = "ARG"
      val expected = List(defaultValue)
      s.argToList(arg, defaultValue) returns expected
      val actual = s.getCompositesPaths(arg).toList
      actual must containTheSameElementsAs(expected)
    }

  }

  "JBehaveRunnerCommandLine#getUniqueReportsPath" should {

    "add unique directory to the path" in {
      val actual = runner.getUniqueReportsPath(reportsPath)
      new File(actual).getParentFile must be equalTo(new File(reportsPath))
    }

    "use public/jbehave when reportsPath is empty" in {
      val expected = "public/jbehave"
      val actual = runner.getUniqueReportsPath("")
      new File(actual).getParentFile must be equalTo(new File(expected))
    }

    "use public/jbehave when reportsPath is null" in {
      val expected = "public/jbehave"
      val actual = runner.getUniqueReportsPath(null)
      new File(actual).getParentFile must be equalTo(new File(expected))
    }

  }

  "JBehaveRunnerCommandLine#getRunner" should {

    "should return an new instance of JBehaveRunner" in {
      val args = Array("", "", "", "", "")
      runner.getRunner(args) must beAnInstanceOf[JBehaveRunner]
    }

//    "should assign "

  }

}

