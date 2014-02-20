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

  def postStoryJson = Action { implicit request =>
//    val json = request.body.asJson.getOrElse(Json.toJson("{ERROR}"))
//    val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
//    val name = (json \ "name").as[String]
//    Story(s"$dir/$name.story").put(Story().toText(json))
    Ok(Json.toJson("{OK}"))
  }


}