package models

import scala.collection.JavaConversions._
import models.jbehave.JBehaveRunner

class Runner(storyPathsValue: List[String],
             runnerClasses: List[RunnerClass],
             compositePath: List[String],
             reportsPath: String)
  extends JBehaveRunner(storyPathsValue, runnerClasses, compositePath, reportsPath) {

}

