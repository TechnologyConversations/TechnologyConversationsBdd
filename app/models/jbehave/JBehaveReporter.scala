package models.jbehave

import java.io.File
import play.api.libs.json.{Json, JsValue}

class JBehaveReporter {

  def list(reportsPath: String, id: String): Option[List[String]] = {
    val file = new File(s"$reportsPath/$id")
    if (!file.exists()) None
    else Some(file.list().toList.filter(_.endsWith(".html")))
  }

  def listJson(reportsPath: String, id: String): Option[JsValue] = {
    val reports = list(reportsPath, id)
    if (reports.isEmpty) None
    else {
      val reportsMap = reports.get.map { report => Map("report" -> report) }
      Some(Json.toJson(Map("reports" -> Json.toJson(reportsMap))))
    }
  }

}

object JBehaveReporter {

  def apply() = { new JBehaveReporter }

}
