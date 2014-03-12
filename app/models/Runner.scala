package models

import scala.collection.JavaConversions._
import models.jbehave.JBehaveRunner

class Runner(storyPathValue: String, stepsInstancesNames: List[String], reportsPath: String)
  extends JBehaveRunner(storyPathValue, stepsInstancesNames, reportsPath) {

}

