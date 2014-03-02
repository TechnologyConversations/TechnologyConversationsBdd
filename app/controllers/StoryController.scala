package controllers

import play.api.mvc._
import models.{Story, StoryList}
import play.api.Play
import play.api.libs.json.{JsValue, Json}

object StoryController extends Controller {

  val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")

  def index(path: String): Action[AnyContent] = Action {
    Ok(scala.io.Source.fromFile("public/html/index.html").mkString).as("text/html")
  }

  def allJson(path: String): Action[AnyContent] = Action {
    Ok(StoryList(s"$dir/$path").json)
  }

  def storyJson(storyPath: String): Action[AnyContent] = Action {
    if (storyPath.isEmpty) {
      Ok(Story(dir, "").toJson)
    } else {
      Ok(Story(dir, storyPath).toJson)
    }
  }

  def createDirectoryJson: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    lazy val json = jsonOption.get
    lazy val pathOption = (json \ "path").asOpt[String]
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (pathOption.isEmpty) {
      noPathResult
    } else {
      val path = pathOption.get
      Story(dir, path).createDirectory()
      Ok(Json.toJson("""{"status": "OK"}"""))
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

  def deleteStoryJson(path: String): Action[AnyContent] = Action { implicit request =>
    Story(dir, path).delete
    Ok(Json.toJson("{status: 'OK'}"))
  }

  private def renameStoryJson(jsonOption: Option[JsValue]) = {
    if (jsonOption.isEmpty) {
      false
    } else {
      val json = jsonOption.get
      val path = (json \ "path").asOpt[String].getOrElse("")
      val originalPath = (json \ "originalPath").asOpt[String].getOrElse("")
      if (originalPath != "" && originalPath != path) {
        Story(dir, path).renameFrom(originalPath)
      } else {
        true
      }
    }
  }

  private def saveStoryJson(jsonOption: Option[JsValue], put: Boolean): Result = {
    lazy val json = jsonOption.get
    lazy val pathOption = (json \ "path").asOpt[String]
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (pathOption.isEmpty) {
      noPathResult
    } else {
      val path = pathOption.get
      val story = Story(dir, path)
      val success = story.save(story.toText(json), put)
      if (success) {
        Ok(Json.toJson("{status: 'OK'}"))
      } else {
        BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be saved."}"""))
      }
    }
  }

  private def noJsonResult: Result = {
    BadRequest(Json.parse("""{"status": "ERROR", "message": "JSON was not found in the request body"}"""))
  }

  private def noPathResult: Result = {
    BadRequest(Json.parse("""{"status": "ERROR", "message": "path was not found"}"""))
  }

}
