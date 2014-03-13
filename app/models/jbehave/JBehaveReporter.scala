package models.jbehave

import java.io.File
import play.api.libs.json.{Json, JsValue}

class JBehaveReporter {

  def list(reportsPath: String, id: String): Option[List[String]] = {
    val dir = new File(s"$reportsPath/$id")
    if (!dir.exists()) None
    else Some(dir.listFiles()
      .filter(_.getName.endsWith(".html"))
      .sortWith(_.lastModified() < _.lastModified())
      .map(file => file.getName)
      .toList
    )
  }

  def listJson(reportsPath: String, id: String): Option[JsValue] = {
    val reports = list(reportsPath, id)
    if (reports.isEmpty) None
    else {
      val reportsMap = reports.get.map { report => Map("path" -> s"/$reportsPath/$id/$report".replace("/public/", "/assets/")) }
      Some(Json.toJson(Map("reports" -> Json.toJson(reportsMap))))
    }
  }

}

object JBehaveReporter {

  def apply() = { new JBehaveReporter }

}
