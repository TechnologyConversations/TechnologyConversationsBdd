package models

import org.jbehave.core.parsers.RegexStoryParser
import play.api.libs.json.Json

trait JBehaveStory {

  def name: String

  def content: String

  private def jBehaveStory = new RegexStoryParser().parseStory(content)

  def json: play.api.libs.json.JsValue = {
    val narrative = jBehaveStory.getNarrative
    val narrativeJson = Json.toJson(Map(
      "inOrderTo" -> narrative.inOrderTo,
      "asA" -> narrative.asA(),
      "iWantTo" -> narrative.iWantTo()))
    val lifecycle = Json.toJson(Map(
      "before" -> jBehaveStory.getLifecycle.getBeforeSteps.get(0),
      "after" -> jBehaveStory.getLifecycle.getAfterSteps.get(0)
    ))
    Json.toJson(Map(
      "name" -> Json.toJson(name),
      "narrative" -> narrativeJson,
      "lifecycle" -> lifecycle
    ))
  }

}
