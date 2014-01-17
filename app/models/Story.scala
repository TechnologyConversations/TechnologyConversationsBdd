package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Story(id: Long, name: String) {}

object Story {

  def all(): List[Story] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM story").as(story *)
  }


  def create(name: String) {
    DB.withConnection { implicit c =>
      SQL("insert into story (name) VALUES ({name})").on(
        'name -> name
      ).executeUpdate
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from story where id = {id}").on(
        'id -> id
      ).executeUpdate
    }
  }

  val story = {
    get[Long]("id") ~ get[String]("name") map {
      case id~name => Story(id, name)
    }
  }

}
