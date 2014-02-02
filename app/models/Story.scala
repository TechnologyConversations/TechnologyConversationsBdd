package models

import java.io.File
import play.api.libs.json.Json

case class Story(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

class StoryUtil() {

  def stories(path: String): List[Story] = {
    dir(path).list.filter(_.endsWith(".story")).map( file => Story(file)).toList
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
