package models

import org.jbehave.core.parsers.RegexStoryParser
import play.api.libs.json.{JsValue, Json}
import scala.collection.JavaConversions._
import org.jbehave.core.model._

trait JBehaveStory {

  def name: String

  def content: String

  def jBehaveStory = new RegexStoryParser().parseStory(content)

  def json: JsValue = Json.toJson(rootCollection)

  def rootCollection = {
    Map(
      "name" -> Json.toJson(name),
      "description" -> Json.toJson(jBehaveStory.getDescription.asString),
      "meta" -> Json.toJson(metaCollection(jBehaveStory.getMeta)),
      "givenStories" -> Json.toJson(givenStoriesCollection(jBehaveStory.getGivenStories.getPaths.toList)),
      "narrative" -> Json.toJson(narrativeCollection(jBehaveStory.getNarrative)),
      "lifecycle" -> Json.toJson(lifecycleCollection(jBehaveStory.getLifecycle)),
      "scenarios" -> Json.toJson(scenariosCollection(jBehaveStory.getScenarios.toList))
    )
  }

  def metaCollection(meta: Meta) = {
    meta.getPropertyNames.map(name => Map("element" -> (name + " " + meta.getProperty(name)).trim))
  }

  def givenStoriesCollection(givenStories: List[String]) = {
    givenStories.map(story => Map("story" -> story))
  }

  def narrativeCollection(narrative: Narrative) = {
    Map(
      "inOrderTo" -> narrative.inOrderTo,
      "asA" -> narrative.asA,
      "iWantTo" -> narrative.iWantTo
    )
  }

  def lifecycleCollection(lifecycle: Lifecycle) = {
    Map(
      "before" -> stepsCollection(lifecycle.getBeforeSteps.toList),
      "after" -> stepsCollection(lifecycle.getAfterSteps.toList)
    )
  }

  def scenariosCollection(scenarios: List[Scenario]) = {
    scenarios.map(scenario =>
      Map(
        "title" -> Json.toJson(scenario.getTitle),
        "meta" -> Json.toJson(metaCollection(scenario.getMeta)),
        "steps" -> Json.toJson(stepsCollection(scenario.getSteps.toList)),
        "examplesTable" -> Json.toJson(scenario.getExamplesTable.asString)
      )
    )
  }

  def stepsCollection(steps: List[String]) = {
    steps.map(step => Map("step" -> step))
  }

}
