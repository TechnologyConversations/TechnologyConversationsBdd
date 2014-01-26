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

  def all(path: String) = {
    dir(path).listFiles.filter(file => file.isDirectory || file.getName.endsWith(".story")).map( file => file.getName).toList
  }
  
  def jsTree(path: String) = {
    val data = all(path).map(file => Json.toJson(Map("text" -> file)))
    Json.toJson(data)
  }

}

object StoryUtil {

  def apply() = new StoryUtil()

}
