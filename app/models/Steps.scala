package models

import models.jbehave.JBehaveSteps

class Steps extends JBehaveSteps {}

object Steps {

  def apply(): Steps = { new Steps }

}
