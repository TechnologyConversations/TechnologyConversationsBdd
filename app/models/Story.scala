package models

import anorm.SqlParser._
import java.io.File

case class Story(fileName: String) {
  def name = fileName.split('.').init.mkString(".")
}

object Story {

  def all(path: String = "stories"): List[Story] = {
    new File(path).list.filter(_.endsWith(".story")).map( file => Story(file)
    ).toList
  }

  // TODO Test
  val story = {
    get[String]("name") map {
      name => Story(name)
    }
  }

}
