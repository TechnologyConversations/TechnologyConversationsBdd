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
      case cnfe: ClassNotFoundException => paramIncorrect("className")
    }
  }

}
