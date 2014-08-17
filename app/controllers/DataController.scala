package controllers

import java.nio.file.{Files, Paths}

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.io.Source

object DataController extends Controller {

  def get(id: String): Action[AnyContent] = Action { implicit request =>
    val path = s"public/data/$id.json"
    if (Files.exists(Paths.get(path))) {
      val jsonString = Source.fromFile(path).mkString
      Ok(toJson(data = Option(Json.parse(jsonString))))
    } else {
      NotFound(toJson(error = Option(s"ID $id could not be found")))
    }
  }

}

