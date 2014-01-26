package models

import java.io.File
import play.api.Play

case class Story(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

object Story {

  val storiesPath = Play.current.configuration.getString("stories.root.dir").getOrElse(".")

  def all(path: String = storiesPath): List[Story] = {
    new File(path).list.filter(_.endsWith(".story")).map( file => Story(file)).toList
  }

  def dirs(path: String = storiesPath): List[String] = {
    new File(path).listFiles.filter(_.isDirectory).map( file => file.getName).toList
  }

}
