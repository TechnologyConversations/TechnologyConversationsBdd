package models

import java.io.File
import play.api.libs.json.Json
import org.joda.time._
import scala.io.Source
import org.jbehave.core.parsers._

class Story(fileName: String, val content: String) {

  def name: String = fileName.split('.').init.mkString(".")

  def jBehaveStory = new RegexStoryParser().parseStory(content)

  def jBehaveJson = {
    val narrative = jBehaveStory.getNarrative
    val narrativeJson = Json.toJson(Map(
      "inOrderTo" -> narrative.inOrderTo,
      "asA" -> narrative.asA(),
      "iWantTo" -> narrative.iWantTo()))
    Json.toJson(Map("narrative" -> narrativeJson))
  }

}
object Story {

  def apply(path: String) = {
    val file = new File(path)
    new Story(file.getName, Source.fromFile(path).mkString)
  }

}