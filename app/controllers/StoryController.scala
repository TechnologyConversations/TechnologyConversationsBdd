package controllers

import models.db.BddDb
import models.file.BddFile
import models.Story
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import java.io.File

import scala.io.Source

class StoryController extends Controller {

  val bddDb = if (mongoEnabled) Option(BddDb(mongoIp, mongoPort, mongoDb)) else Option.empty
  val bddFile = Option(BddFile())
  val story = Story(bddFile = bddFile, bddDb = bddDb)

  def index(path: String): Action[AnyContent] = Action {
    Ok(Source.fromFile("public/html/index.html").mkString).as("text/html")
  }

  def listJson(storyPath: String): Action[AnyContent] = Action {
    val fullStoryPath = s"$storiesDir/$storyPath"
    val json = story.findStories(new File(fullStoryPath), storyPath)
    if (json.isDefined) {
      Ok(json.get)
    } else {
      BadRequest(toJson(message = Option(s"Could not load directories and stories from $storyPath")))
    }
  }

  def storyJson(storyPath: String): Action[AnyContent] = Action {
    val fullStoryPath = s"$storiesDir/$storyPath"
    val json = story.findStory(new File(fullStoryPath), storyPath)
    if (json.isDefined) {
      Ok(json.get)
    } else {
      BadRequest(toJson(message = Option(s"Could not load $storyPath")))
    }
  }

  def createDirectoryJson: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    lazy val json = jsonOption.get
    lazy val pathOption = (json \ "path").asOpt[String]
    if (jsonOption.isEmpty) {
      BadRequest(toJson(message = Option("JSON was not found in the request body")))
    } else if (pathOption.isEmpty) {
      BadRequest(toJson(message = Option("path was not found")))
    } else {
      val path = pathOption.get
      bddFile.get.createDirectory(new File(s"$storiesDir/$path"))
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
      BadRequest(toJson(message = Option("JSON was not found in the request body")))
    } else if (renameStoryJson(jsonOption)) {
      saveStoryJson(jsonOption, overwrite)
    } else {
      BadRequest(toJson(message = Option("Story could not be renamed")))
    }
  }

  def deleteStoryJson(path: String): Action[AnyContent] = Action { implicit request =>
    story.removeStory(new File(s"$storiesDir/$path"), path)
    Ok(toJson(message = Option(s"Story $storiesDir/$path has been deleted")))
  }

  def storiesFromFileToMongoDb(): Action[AnyContent] = Action { implicit request =>
    val result = story.storiesFromFileToMongoDb(new File(storiesDir))
    if (result) Ok(toJson(message = Option("OK")))
    else BadRequest(toJson(message = Option("Failed to read story files or access MongoDB")))
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
      BadRequest(toJson(error = Option("JSON was not found"), message = Option("JSON was not found in the request body")))
    } else if (pathOption.isEmpty) {
      BadRequest(toJson(error = Option("Path was not found"), message = Option("Path can not be empty")))
    } else {
      val path = pathOption.get
      val success = story.saveStory(new File(s"$storiesDir/$path"), json, put)
      if (success) {
        Ok(Json.toJson("{status: 'OK'}"))
      } else {
        BadRequest(Json.parse("""{"status": "ERROR", "message": "Story could not be saved."}"""))
      }
    }
  }

}

object StoryController extends controllers.StoryController