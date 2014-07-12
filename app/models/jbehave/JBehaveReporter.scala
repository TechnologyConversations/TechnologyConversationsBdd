package models.jbehave

import java.io.File
import play.api.libs.json.{Json, JsValue}
import scala.io.Source
import scala.xml.XML

class JBehaveReporter {

  def listJson(reportsPath: String, id: String): Option[JsValue] = {
    val reports = list(reportsPath, id)
    if (reports.isEmpty) None
    else {
      val reportsMap = reports.get.map { report =>
        val stepsReportPath = s"$reportsPath/$id/$report".replace(".html", ".xml")
        Map(
          "path" -> Json.toJson(report),
          "steps" -> Json.toJson(steps(stepsReportPath))
        )
      }
      Some(Json.toJson(reportsMap))
    }
  }

  def reportContent(reportsPath: String, id: String, report: String): Option[String] = {
    val file = new File(s"$reportsPath/$id/$report")
    if (!file.exists()) None
    else Some(Source.fromFile(file).mkString)
  }

  private[jbehave] def list(reportsPath: String, id: String): Option[List[String]] = {
    val dir = new File(s"$reportsPath/$id")
    if (!dir.exists()) {
      Thread.sleep(5000)
    }
    if (!dir.exists()) None
    else Some(dir.listFiles()
      .filter(_.getName.endsWith(".html"))
      .sortWith(_.lastModified() < _.lastModified())
      .map(file => file.getName)
      .toList
    )
  }

  private[jbehave] def steps(reportPath: String) = {
    val xml = XML.loadFile(new File(reportPath))
    (xml \ "scenario" \ "step").map(node => Map(
      "text" -> node.text,
      "status" -> (node \ "@outcome").text
    ))
  }

}

object JBehaveReporter {
  def apply() = new JBehaveReporter
}
