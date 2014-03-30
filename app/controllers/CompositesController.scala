package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.Composites
import play.api.Play
import play.api.libs.json.Json

object CompositesController extends Controller {

  val dir = Play.current.configuration.getString("composites.root.dir").getOrElse("stories")

  def classesToJson: Action[AnyContent] = Action {
    val composites = Composites(dir)
    Ok(composites.classesToJson(composites.list()))
  }

  def classToJson(className: String): Action[AnyContent] = Action {
    try {
      Ok(Composites(dir).classToJson(className))
    } catch {
      case _: ClassNotFoundException => paramIncorrect("className")
    }
  }

  def putClass: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    if (jsonOption.isEmpty) {
      noJsonResult
    } else {
      try {
        val composites = Composites(dir)
        val classText = composites.classToText(jsonOption.get)
//        composites.save(classText, overwrite = true)
        okJson("Class was saved successfully")
      } catch {
        case e: Exception => errorJson(e.getMessage)
      }
    }
  }

}
