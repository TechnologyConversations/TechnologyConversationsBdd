package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import models.Reporter

object ReporterController extends Controller {

  def list(id: String): Action[AnyContent] = Action {
    val json = new Reporter().listJson(reportsRelativeDir, id)
    if (json.isEmpty) BadRequest(toJson(message = Option("ID is NOT correct")))
    else Ok(json.get)
  }

  def get(id: String, report: String): Action[AnyContent] = Action {
    val reportContent = new Reporter().reportContent(reportsDir, id, report)
    if (reportContent.isEmpty) BadRequest(toJson(message = Option("ID or report is NOT correct")))
    else Ok(reportContent.get).as("text/html")
  }

}
