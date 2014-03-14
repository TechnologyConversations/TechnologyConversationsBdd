package models

import scala.collection.JavaConversions._
import models.jbehave.JBehaveRunner

class Runner(storyPathValue: String,
             runnerClasses: List[RunnerClass],
             reportsPath: String)
  extends JBehaveRunner(storyPathValue, runnerClasses, reportsPath) {

}

