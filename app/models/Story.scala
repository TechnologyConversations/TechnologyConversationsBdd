package models

import java.io.File

case class Story(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

object Story {

  // TODO Move stories to the public directory
  // TODO Test stories directory against the distribution
  def all(path: String = "stories"): List[Story] = {
    val dir = new File(path)
    if (dir.exists()) dir.list.filter(_.endsWith(".story")).map( file => Story(file)).toList
    else List()
  }

}
