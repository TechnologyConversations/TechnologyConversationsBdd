package models

import java.io.File

case class Story(fileName: String) {
  def name: String = fileName.split('.').init.mkString(".")
}

object Story {

  def all(path: String = "stories"): List[Story] = {
    new File(path).list.filter(_.endsWith(".story")).map( file => Story(file)
    ).toList
  }

}
