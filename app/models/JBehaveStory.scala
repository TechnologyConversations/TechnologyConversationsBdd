package models

import org.jbehave.core.parsers.RegexStoryParser
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import org.jbehave.core.model.{ExamplesTable, Meta}
import scala.util.parsing.json.JSONArray

trait JBehaveStory {

  def name: String

  def content: String

  private def jBehaveStory =   new RegexStoryParser().parseStory(content)

  def json: play.api.libs.json.JsValue = {
    val narrative = Map(
      "inOrderTo" -> jBehaveStory.getNarrative.inOrderTo,
      "asA" -> jBehaveStory.getNarrative.asA(),
      "iWantTo" -> jBehaveStory.getNarrative.iWantTo())
    val lifecycle = Map(
      "before" -> jBehaveStory.getLifecycle.getBeforeSteps.toList,
      "after" -> jBehaveStory.getLifecycle.getAfterSteps.toList
    )
    val scenarios = jBehaveStory.getScenarios.map(scenario =>
      Map(
        "title" -> Json.toJson(scenario.getTitle),
        "meta" -> Json.toJson(metaJson(scenario.getMeta)),
        "steps" -> Json.toJson(scenario.getSteps.toList),
        "examplesTable" -> Json.toJson(scenario.getExamplesTable.asString)
      )
    )
//    TODO
//    Rewrite examples table to lists
//    !--
    Json.toJson(Map(
      "name" -> Json.toJson(name),
      "description" -> Json.toJson(jBehaveStory.getDescription.asString),
      "meta" -> Json.toJson(metaJson(jBehaveStory.getMeta)),
      "givenStories" -> Json.toJson(jBehaveStory.getGivenStories.getPaths.toList),
      "narrative" -> Json.toJson(narrative),
      "lifecycle" -> Json.toJson(lifecycle),
      "scenarios" -> Json.toJson(scenarios)
    ))
  }

  def metaJson(meta: Meta) = {
    meta.getPropertyNames.map(name => (name + " " + meta.getProperty(name)).trim).toList
  }

}
