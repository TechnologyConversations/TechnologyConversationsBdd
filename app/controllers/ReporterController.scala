package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import models.Reporter
import play.api.Play

object ReporterController extends Controller {

  val reportsPath = Play.current.configuration.getString("reports.root.dir").getOrElse("public/jbehave")

  def list(id: String): Action[AnyContent] = Action {
    val json = new Reporter().listJson(reportsPath, id)
    if (json.isEmpty) result(paramIncorrectMap("ID"))
    else Ok(json.get)
  }

}
