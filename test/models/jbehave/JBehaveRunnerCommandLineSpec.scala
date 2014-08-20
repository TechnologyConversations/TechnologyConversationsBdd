package models.jbehave

import com.technologyconversations.bdd.steps.{CommonSteps, FileSteps, WebSteps}
import models.RunnerClass
import org.specs2.mutable.Specification
import scala.collection.JavaConversions._
import java.io.File

class JBehaveRunnerCommandLineSpec extends Specification {

  val runner = new JBehaveRunnerCommandLine()
  val storyPath1 = "path/to/my.story"
  val storyPath2 = "path/to/my/story/with/ant/pattern/**/*.story"
  val storyPaths = s"$storyPath1,$storyPath2"
  val reportsPath = "path/to/reports"
  val compositePath1 = "composites/TcBddComposites.groovy"

  "JBehaveRunnerCommandLine#getStoryPaths" should {

    "return the list of value" in {
      val storyPathsArray = Array(storyPath1, storyPath2)
      val storyPathsList = runner.getStoryPaths(storyPathsArray)
      storyPathsList must beAnInstanceOf[java.util.List[String]]
      storyPathsList.toList must containTheSameElementsAs(Seq(storyPath1, storyPath2))
    }

    "return the list with data/stories/**/*.story when the array is null" in {
      val storyPathsList = runner.getStoryPaths(null)
      storyPathsList must beAnInstanceOf[java.util.List[String]]
      storyPathsList.toList must containTheSameElementsAs(Seq("data/stories/**/*.story"))
    }

  }

  "JBehaveRunnerCommandLine#getStepClasses" should {

    val class1Name = classOf[WebSteps].getName
    val class2Name = classOf[FileSteps].getName
    val defaultParamsMap = Map("key1" -> "value1", "key2" -> "value2")
    val paramsMap = Map("key1" -> "value1", "key2" -> "value2")

    "return the list of value" in {
      val stepClassesArray = Array(class1Name, class2Name)
      val expected = List(
        RunnerClass(class1Name, defaultParamsMap),
        RunnerClass(class2Name, defaultParamsMap)
      )
      val stepsClassesList = runner.getStepClasses(stepClassesArray, paramsMap)
      stepsClassesList must beAnInstanceOf[java.util.List[String]]
      stepsClassesList.toList must containTheSameElementsAs(expected)
    }

    "return the list with default steps classes when the array is null" in {
      val expected = List(
        RunnerClass(classOf[CommonSteps].getName(), defaultParamsMap),
        RunnerClass(classOf[WebSteps].getName(), defaultParamsMap),
        RunnerClass(classOf[FileSteps].getName(), defaultParamsMap)
      )
      val stepsClassesList = runner.getStepClasses(null, paramsMap)
      stepsClassesList must beAnInstanceOf[java.util.List[String]]
      stepsClassesList.toList must containTheSameElementsAs(expected)
    }

    "set parameters to all steps classes" in {
      val stepClassesArray = Array(class1Name, class2Name)
      val expected = List(
        RunnerClass(class1Name, defaultParamsMap),
        RunnerClass(class2Name, defaultParamsMap)
      )
      val stepsClassesList = runner.getStepClasses(stepClassesArray, paramsMap)
      stepsClassesList must beAnInstanceOf[java.util.List[String]]
      stepsClassesList.toList must containTheSameElementsAs(expected)
    }

    "set parameters as empty map when null" in {
      val stepClassesArray = Array(class1Name, class2Name)
      val expected = List(
        RunnerClass(class1Name, Map()),
        RunnerClass(class2Name, Map())
      )
      val stepsClassesList = runner.getStepClasses(stepClassesArray, null)
      stepsClassesList must beAnInstanceOf[java.util.List[String]]
      stepsClassesList.toList must containTheSameElementsAs(expected)
    }

  }

  "JBehaveRunnerCommandLine#getCompositesPaths" should {

    "return the list of values" in {
      val storyPathsArray = Array(compositePath1)
      val storyPathsList = runner.getCompositesPaths(storyPathsArray)
      storyPathsList must beAnInstanceOf[java.util.List[String]]
      storyPathsList.toList must containTheSameElementsAs(Seq(compositePath1))
    }

    "return the empty list when the array is empty" in {
      val storyPathsList = runner.getCompositesPaths(null)
      storyPathsList must beAnInstanceOf[java.util.List[String]]
      storyPathsList.toList must containTheSameElementsAs(Seq())
    }

  }

  "JBehaveRunnerCommandLine#getUniqueReportsPath" should {

    "add unique directory to the path" in {
      val actual = runner.getUniqueReportsPath(reportsPath)
      new File(actual).getParentFile must be equalTo new File(reportsPath)
    }

    "use public/jbehave when reportsPath is empty" in {
      val expected = "public/jbehave"
      val actual = runner.getUniqueReportsPath("")
      new File(actual).getParentFile must be equalTo new File(expected)
    }

    "use public/jbehave when reportsPath is null" in {
      val expected = "public/jbehave"
      val actual = runner.getUniqueReportsPath(null)
      new File(actual).getParentFile must be equalTo new File(expected)
    }

  }

  "JBehaveRunner#getOptions" should {

    "have story_paths option" in {
      val option = runner.getOptions.getOption("s")
      option.getLongOpt must be equalTo "story_path"
      option.hasArg must be equalTo true
    }

    "have steps_classes option" in {
      val option = runner.getOptions.getOption("c")
      option.getLongOpt must be equalTo "steps_class"
      option.hasArg must be equalTo true
    }

    "have steps_params option" in {
      val option = runner.getOptions.getOption("P")
      option.getArgs must be equalTo 2
      option.getValueSeparator must be equalTo '='
      option.hasArg must be equalTo true
    }

    "have composites_paths option" in {
      val option = runner.getOptions.getOption("o")
      option.getLongOpt must be equalTo "composites_path"
      option.hasArg must be equalTo true
    }

    "have reports_path option" in {
      val option = runner.getOptions.getOption("r")
      option.getLongOpt must be equalTo "reports_path"
      option.hasArg must be equalTo true
    }

    "have help option" in {
      val option = runner.getOptions.getOption("h")
      option.getLongOpt must be equalTo "help"
      option.hasArg must be equalTo false
    }

  }

  "JBehaveRunnerCommandLine#getRunner" should {

    val webSteps = classOf[WebSteps].getName
    val args = Array(
      "--story_path", storyPath1, "--story_path", storyPath2,
      "--steps_class", webSteps,
      "-P", "key1=value1", "-P", "key2=value2",
      "-composites_path", compositePath1,
      "-reports_path" , reportsPath)
    val jBehaveRunner = runner.getRunner(args)

    "should return an new instance of JBehaveRunner" in {
      runner.getRunner(args) must beAnInstanceOf[JBehaveRunner]
    }

    "should set storyPaths" in {
      jBehaveRunner.getStoryPaths must beAnInstanceOf[java.util.List[String]]
      val storyPathsList = jBehaveRunner.getStoryPaths.toList
      storyPathsList must haveSize(2)
      storyPathsList must contain(storyPath1)
      storyPathsList must contain(storyPath2)
    }

    "should set classes in stepsClasses" in {
      jBehaveRunner.getStepsInstances must beAnInstanceOf[java.util.List[RunnerClass]]
      jBehaveRunner.getStepsInstances.toList must haveSize(1)
    }

    "should set parameters in stepsClasses" in {
      val stepsInstance = jBehaveRunner.getStepsInstances.get(0).asInstanceOf[WebSteps]
      val params = stepsInstance.getParams.toMap
      params must haveSize(2)
      params must havePair("key1" -> "value1")
      params must havePair("key2" -> "value2")
    }

    "should set compositePaths" in {
      jBehaveRunner.getCompositePaths must beAnInstanceOf[java.util.List[String]]
      val compositesPath = jBehaveRunner.getCompositePaths.toList
      compositesPath must haveSize(1)
      compositesPath must contain(compositePath1)
    }

    "should set uniqueReportsPath" in {
      val path = jBehaveRunner.getReportsPath
      path must beAnInstanceOf[String]
      path must startWith(reportsPath)
    }

  }

  "JBehaveRunnerCommandLine#isHelp" should {

    "return true when help argument is used" in {
      val args = Array("--story_path", "my.story", "--help")
      runner.isHelp(args) must be equalTo true
    }

    "return false when help argument is NOT user" in {
      val args = Array("--story_path", "my.story")
      runner.isHelp(args) must be equalTo false
    }

  }

}

