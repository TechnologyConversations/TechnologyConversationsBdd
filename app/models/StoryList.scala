package models

import java.io.File
import play.api.libs.json.Json

class StoryList(path: String) {

  def stories: List[String] = {
    dir.list.filter(_.endsWith(".story")).map( file => file.split('.').init.mkString(".")).toList
  }

  def dirs: List[String] = {
    dir.listFiles.filter(_.isDirectory).map( file => file.getName).toList
  }

  private def dir = {
    val dir = new File(path)
    if (!dir.exists) dir.mkdir
    dir
  }

  def json = {
    val storiesData = stories.map(name => Json.toJson(Map("name" -> name)))
    val dirsData = dirs.map(name => Json.toJson(Map("name" -> name)))
    Json.toJson(Map("stories" -> storiesData, "dirs" -> dirsData))
  }

}

object StoryList {

  def apply(path: String) = new StoryList(path)

}