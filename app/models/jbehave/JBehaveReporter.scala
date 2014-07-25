package models.jbehave

import java.io.File
import play.api.libs.json.{Json, JsValue}
import scala.io.Source
import scala.xml.XML

class JBehaveReporter {

  val FINISHED = "finished"

  def listJson(reportsPath: String, id: String): Option[JsValue] = {
    val realReportsPath = {
      if (new File(s"$reportsPath/$id").exists()) {
        reportsPath
      } else {
        s"target/$reportsPath"
      }
    }
    println(realReportsPath)
    println(new File(realReportsPath).getAbsolutePath)
    val reports = list(realReportsPath, id)
    if (reports.isEmpty) None
    else {
      val data = Map(
        "status" -> Json.toJson(status(realReportsPath, id)),
        "reports" -> Json.toJson(reportsMap(realReportsPath, id, reports.get))
      )
      Some(Json.toJson(data))
    }
  }

  def reportContent(reportsPath: String, id: String, report: String): Option[String] = {
    val file = new File(s"$reportsPath/$id/$report")
    if (!file.exists()) None
    else Some(Source.fromFile(file).mkString)
  }

  private[jbehave] def list(reportsPath: String, id: String): Option[List[String]] = {
    val dir = new File(s"$reportsPath/$id")
    println(dir + " " + dir.exists())
    if (!dir.exists()) None
    else Some(dir.listFiles()
      .filter(_.getName.endsWith(".html"))
      .sortWith(_.lastModified() < _.lastModified())
      .map(file => file.getName)
      .toList
    )
  }

  private[jbehave] def steps(reportPath: String, reportsPath: String, id: String) = {
    if (status(reportsPath, id).equals(FINISHED)) {
      val xml = XML.loadFile(new File(reportPath))
      Some((xml \ "scenario" \ "step").map(node => Map(
        "text" -> node.text,
        "status" -> (node \ "@outcome").text
      )))
    } else {
      None
    }
  }

  private[jbehave] def status(reportsPath: String, id: String): String = {
    if (new File(s"$reportsPath/$id/view").exists()) FINISHED
    else "inProgress"
  }

  private[jbehave] def reportsMap(reportsPath: String, id: String, reports: List[String]) = {
    reports.map { report =>
      val stepsReportPath = s"$reportsPath/$id/$report".replace(".html", ".xml")
      Map(
        "path" -> Json.toJson(report),
        "steps" -> Json.toJson(steps(stepsReportPath, reportsPath, id).getOrElse(List()))
      )
    }
  }


}

object JBehaveReporter {
  def apply() = new JBehaveReporter
}
