package controllers

import java.io.File

import play.api.Play
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.io.Source

object DataController extends Controller {

  val dataRootDir = Play.current.configuration.getString("data.root.dir").getOrElse("public/data")

  def get(id: String): Action[AnyContent] = Action { implicit request =>
    val dataSuffix = System.getProperty("dataSuffix", "")
    val path = s"$dataRootDir/$id$dataSuffix.json"
    val file = new File(path)
    if (file.exists()) {
      val jsonString = Source.fromFile(path).mkString
      Ok(toJson(data = Option(Json.parse(jsonString))))
    } else {
      val absPath = file.getAbsolutePath
      NotFound(toJson(
        error = Option(s"ID $id could not be found"),
        message = Option(s"Could not find $absPath")))
    }
  }

}

