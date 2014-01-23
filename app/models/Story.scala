package models

import java.io.File

case class Story(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

object Story {

  // TODO Test what happens when stories dir is not present
  // TODO Move stories to the public directory
  // TODO Test stories directory against the distribution
  def all(path: String = "stories"): List[Story] = {
    new File(path).list.filter(_.endsWith(".story")).map( file => Story(file)).toList
  }

}
