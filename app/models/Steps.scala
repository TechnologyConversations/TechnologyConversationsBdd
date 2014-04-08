package models

import models.jbehave.JBehaveSteps

class Steps(stepsDir: String = "steps", composites: List[String] = List.empty[String]) extends JBehaveSteps(stepsDir, composites) {}

object Steps {

  def apply(): Steps = { new Steps }
  def apply(stepsDir: String = "steps", composites: List[String] = List.empty[String]): Steps = { new Steps(stepsDir, composites) }

}
