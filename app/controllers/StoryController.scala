package controllers

import play.api.mvc._
import models.{Story, StoryList}
import play.api.Play
import play.api.libs.json.Json

object StoryController extends Controller {

  def index(path: String) = Action {
    Ok(scala.io.Source.fromFile("public/html/index.html").mkString).as("text/html")
  }

  def allJson = Action {
    val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
    Ok(StoryList(dir).json)
  }

  def storyJson(storyPath: String) = Action {
    if (storyPath.isEmpty) {
      Ok(Story().toJson)
    } else {
      val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
      Ok(Story(dir + "/" + storyPath).toJson)
    }
  }

  def putStoryJson = Action { implicit request =>
    Ok(Json.toJson("{OK}"))
  }

}