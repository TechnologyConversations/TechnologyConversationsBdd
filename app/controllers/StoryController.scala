package controllers

import play.api.mvc._
import models.{Story, StoryList}
import play.api.Play
import play.api.libs.json.{JsValue, Json}

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
    val jsonOption = request.body.asJson
    lazy val json = jsonOption.get
    lazy val nameOption = (json \ "name").asOpt[String]
    if (jsonOption.isEmpty) {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "JSON was not found in the request body"}"""))
    } else if (nameOption.isEmpty) {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "name was not found"}"""))
    } else {
      val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
      val name = nameOption.get
      val success = Story(s"$dir/$name.story").post(Story().toText(json))
      if (success) {
        Ok(Json.toJson("{status: 'OK'}"))
      } else {
        BadRequest(Json.parse("""{"status": "ERROR", "message": "Story already exists. Use PUT to update."}"""))
      }
    }
  }


}