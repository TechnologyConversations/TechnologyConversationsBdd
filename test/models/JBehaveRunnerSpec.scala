package models

import org.specs2.mutable.Specification
import java.io.File
import java.util

class JBehaveRunnerSpec extends Specification {

  val storiesDirPath = "test/stories"

  "JBehaveRunner#storyPaths" should {

    "return all stories" in {
      val runner = new JBehaveRunner(s"$storiesDirPath/**/*.story", new util.ArrayList[String]())
      val expectedSize = new File(storiesDirPath).list.filter(_.endsWith(".story")).size
      runner.storyPaths should have size expectedSize
    }

  }

  "JBehaveRunner#setStepsInstancesFromNames" should {

    "return array of instances" in {
      val steps = new util.ArrayList[String]()
      steps.add("com.technologyconversations.bdd.steps.WebSteps")
      val runner = new JBehaveRunner(s"$storiesDirPath/**/*.story", steps)
      runner.getStepsInstances should have size 1
    }

    "throw exception is class does NOT exist" in {
      val steps = new util.ArrayList[String]()
      steps.add("com.technologyconversations.bdd.steps.NonExistentSteps")
      new JBehaveRunner(s"$storiesDirPath/**/*.story", steps) should throwA[Exception]
    }

  }

}
