package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Story(name: String)

object Story {

  // TODO Test
  def all(): List[Story] = {
    List(Story("myStory1"), Story("myStory2"))
  }

  // TODO Test
  val story = {
    get[String]("name") map {
      name => Story(name)
    }
  }

}
