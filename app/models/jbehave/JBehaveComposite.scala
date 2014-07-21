package models.jbehave

case class JBehaveComposite(stepText: String, compositeSteps: List[String]) {

  def stepType: String = {
    stepText.substring(0, stepText.indexOf(" "))
  }

  def strippedFormattedStepText: String = {
    stepText.substring(stepText.indexOf(" ") + 1).replace("\"", "\\\"")
  }

  def strippedFormattedGroovyStepText: String = {
    strippedFormattedStepText.replace("$", "\\$")
  }

  def formattedCompositeSteps: List[String] = {
    compositeSteps.map(_.replace("\"", "\\\""))
  }

  def formattedGroovyCompositeSteps: List[String] = {
    formattedCompositeSteps.map(_.replace("$", "\\$"))
  }

  def params: Set[String] = {
    "<.+?>".r
      .findAllIn(stepText + compositeSteps.toString)
      .toSet[String]
      .map(_.replaceAll("<|>", ""))
  }

}
