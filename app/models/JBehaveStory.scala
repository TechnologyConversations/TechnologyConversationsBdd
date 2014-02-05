package models

import org.jbehave.core.parsers.RegexStoryParser
import play.api.libs.json.Json

trait JBehaveStory {

  def name: String

  def content: String

  def jBehaveStory = new RegexStoryParser().parseStory(content)

  def json: play.api.libs.json.JsValue = {
    val narrative = jBehaveStory.getNarrative
    val narrativeJson = Json.toJson(Map(
      "inOrderTo" -> narrative.inOrderTo,
      "asA" -> narrative.asA(),
      "iWantTo" -> narrative.iWantTo()))
    Json.toJson(Map(
      "name" -> Json.toJson(name),
      "narrative" -> narrativeJson
    ))
  }

}
