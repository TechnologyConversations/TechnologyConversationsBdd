package models

import java.io.File

case class Story(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

object Story {

  def all(path: String): List[Story] = {
    dir(path).list.filter(_.endsWith(".story")).map( file => Story(file)).toList
  }

  def dirs(path: String): List[String] = {
    dir(path).listFiles.filter(_.isDirectory).map( file => file.getName).toList
  }

  def dir(path: String) = {
    val dir = new File(path)
    if (!dir.exists) dir.mkdir
    dir
  }


}
