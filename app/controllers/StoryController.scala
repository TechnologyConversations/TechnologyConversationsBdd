package controllers

import play.api.mvc._
import models.{Story, StoryList}
import play.api.Play
import play.api.libs.json.{JsValue, Json}

object StoryController extends Controller {

  def index(path: String): Action[AnyContent] = Action {
    Ok(scala.io.Source.fromFile("public/html/index.html").mkString).as("text/html")
  }

  def allJson(path: String): Action[AnyContent] = Action {
    val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
    Ok(StoryList(s"$dir/$path").json)
  }

  def storyJson(storyPath: String): Action[AnyContent] = Action {
    if (storyPath.isEmpty) {
      Ok(Story().toJson)
    } else {
      val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
      Ok(Story(dir + "/" + storyPath).toJson)
    }
  }

  def postStoryJson: Action[AnyContent] = Action { implicit request =>
    val overwrite = false
    saveStoryJson(request.body.asJson, overwrite)
  }

  def putStoryJson: Action[AnyContent] = Action { implicit request =>
    val overwrite = true
    val jsonOption = request.body.asJson
    if (renameStoryJson(jsonOption)) {
      saveStoryJson(jsonOption, overwrite)
    } else {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be renamed"}"""))
    }
  }

  private def renameStoryJson(jsonOption: Option[JsValue]) = {
    if (jsonOption.isEmpty) {
      false
    } else {
      val json = jsonOption.get
      val name = (json \ "name").asOpt[String].getOrElse("")
      val originalName = (json \ "originalName").asOpt[String].getOrElse("")
      if (originalName != "" && originalName != name) {
        val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
        Story(s"$dir/$name.story").rename(s"$dir/$originalName.story")
      } else {
        true
      }
    }
  }

  private def saveStoryJson(jsonOption: Option[JsValue], put: Boolean): Result = {
    lazy val json = jsonOption.get
    lazy val nameOption = (json \ "name").asOpt[String]
    if (jsonOption.isEmpty) {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "JSON was not found in the request body"}"""))
    } else if (nameOption.isEmpty) {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "name was not found"}"""))
    } else {
      val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
      val name = nameOption.get
      val success = Story(s"$dir/$name.story").save(Story().toText(json), put)
      if (success) {
        Ok(Json.toJson("{status: 'OK'}"))
      } else {
        BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be saved."}"""))
      }
    }
  }

}
