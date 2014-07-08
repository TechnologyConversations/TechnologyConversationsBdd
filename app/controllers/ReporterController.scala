package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import models.Reporter
import play.api.Play

object ReporterController extends Controller {

  def list(id: String): Action[AnyContent] = Action {
    val json = new Reporter().listJson(reportsRelativeDir, id)
    if (json.isEmpty) result(paramIncorrectMap("ID"))
    else Ok(json.get)
  }

  def get(id: String, report: String): Action[AnyContent] = Action {
    val reportContent = new Reporter().reportContent(reportsDir, id, report)
    if (reportContent.isEmpty) result(paramIncorrectMap("ID and/or report"))
    else Ok(reportContent.get)
  }

}
