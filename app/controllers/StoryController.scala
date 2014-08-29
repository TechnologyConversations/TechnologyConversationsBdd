package controllers

import play.api.mvc.Results._
import play.api.mvc._
import models.{Story, StoryList}
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object StoryController extends Controller {

  def index(path: String): Action[AnyContent] = Action {
    Ok(Source.fromFile("public/html/index.html").mkString).as("text/html")
  }

  def listJson(path: String): Action[AnyContent] = Action {
    Ok(StoryList(s"$storiesDir/$path").json)
  }

  def storyJson(storyPath: String): Action[AnyContent] = Action {
    if (storyPath.isEmpty) {
      Ok(Story(storiesDir, "").toJson)
    } else {
      Ok(Story(storiesDir, storyPath).toJson)
    }
  }

  def createDirectoryJson: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    lazy val json = jsonOption.get
    lazy val pathOption = (json \ "path").asOpt[String]
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (pathOption.isEmpty) {
      noResult("path")
    } else {
      val path = pathOption.get
      Story(storiesDir, path).createDirectory()
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
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (renameStoryJson(jsonOption)) {
      saveStoryJson(jsonOption, overwrite)
    } else {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be renamed"}"""))
    }
  }

  def deleteStoryJson(path: String): Action[AnyContent] = Action { implicit request =>
    Story(storiesDir, path).delete(s"$storiesDir/$path")
    val json = toJson(message = Option(s"Story $storiesDir/$path has been deleted"))
    Ok(json)
  }

  private def renameStoryJson(jsonOption: Option[JsValue]) = {
    if (jsonOption.isEmpty) {
      false
    } else {
      val json = jsonOption.get
      val path = (json \ "path").asOpt[String].getOrElse("")
      val originalPath = (json \ "originalPath").asOpt[String].getOrElse("")
      if (originalPath != "" && originalPath != path) {
        Story(storiesDir, path).renameFrom(originalPath)
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
      BadRequest(toJson(error = Option("Path was not found"), message = Option("Path can not be empty")))
    } else {
      val path = pathOption.get
      val story = Story(storiesDir, path)
      val success = story.save(s"$storiesDir/$path", story.toText(json), put)
      if (success) {
        Ok(Json.toJson("{status: 'OK'}"))
      } else {
        BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be saved."}"""))
      }
    }
  }

}
