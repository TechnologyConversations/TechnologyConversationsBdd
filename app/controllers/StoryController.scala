package controllers

import play.api.mvc._
import models.{Story, StoryList}
import play.api.Play
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object StoryController extends Controller {

  val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")

  def index(path: String): Action[AnyContent] = Action {
    Ok(Source.fromFile("public/html/index.html").mkString).as("text/html")
  }

  def listJson(path: String): Action[AnyContent] = Action {
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
      noResult("path")
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
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (renameStoryJson(jsonOption)) {
      saveStoryJson(jsonOption, overwrite)
    } else {
      BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be renamed"}"""))
    }
  }

  def deleteStoryJson(path: String): Action[AnyContent] = Action { implicit request =>
    Story(dir, path).delete(s"$dir/$path")
    okJson(s"Story $dir/$path has been deleted")
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
      noResult("path")
    } else {
      val path = pathOption.get
      val story = Story(dir, path)
      val success = story.save(s"$dir/$path", story.toText(json), put)
      if (success) {
        Ok(Json.toJson("{status: 'OK'}"))
      } else {
        BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be saved."}"""))
      }
    }
  }

}
