package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Story(name: String)

object Story {

  // TODO Test
  def all(): List[Story] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM story").as(story *)
  }

  // TODO Test
  def create(story: Story) {
    DB.withConnection { implicit c =>
      SQL("insert into story (name) VALUES ({name})").on(
        'name -> story.name
      ).executeUpdate
    }
  }

  // TODO Test
  def delete(name: String) {
    DB.withConnection { implicit c =>
      SQL("delete from story where name = {name}").on(
        'name -> name
      ).executeUpdate
    }
  }

  // TODO Test
  val story = {
    get[String]("name") map {
      name => Story(name)
    }
  }

}
