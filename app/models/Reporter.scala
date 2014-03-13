package models

import models.jbehave.JBehaveReporter

class Reporter extends JBehaveReporter { }
object Reporter {
  def apply: Reporter = { new Reporter() }
}
