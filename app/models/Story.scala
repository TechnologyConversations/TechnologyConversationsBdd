package models

import java.io.File
import play.api.libs.json.Json
import org.joda.time._
import scala.io.Source
import org.jbehave.core.parsers._

case class Story(fileName: String, content: String) {
  def name: String = fileName.split('.').init.mkString(".")
  def narrative: String = {
    val narrative = new RegexStoryParser().parseStory(content).getNarrative
    if (!narrative.isEmpty)
      "Narrative:\nIn order to " + narrative.inOrderTo + "\nAs a " + narrative.asA + "\nI want to " + narrative.iWantTo
    else ""
  }
}


case class StoryList(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

class StoryUtil() {

  def story(path: String):Story = {
    val file = new File(path)
    Story(file.getName, fileSource(path))
  }

  def fileSource(path: String):String = {
    Source.fromFile(path).mkString
  }

  def stories(path: String): List[StoryList] = {
    dir(path).list.filter(_.endsWith(".story")).map( file => StoryList(file)).toList
  }

  def dirs(path: String): List[String] = {
    dir(path).listFiles.filter(_.isDirectory).map( file => file.getName).toList
  }

  private def dir(path: String) = {
    val dir = new File(path)
    if (!dir.exists) dir.mkdir
    dir
  }

  def allJson(path: String) = {
    val storiesData = stories(path).map(story => Json.toJson(Map("name" -> story.name)))
    val dirsData = dirs(path).map(name => Json.toJson(Map("name" -> name)))
    Json.toJson(Map("stories" -> storiesData, "dirs" -> dirsData))
  }

}

object StoryUtil {

  def apply() = new StoryUtil()

}
