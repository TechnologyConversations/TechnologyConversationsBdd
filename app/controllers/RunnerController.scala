package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.{RunnerClass, Runner}
import play.api.libs.json.JsValue
import scala.collection.JavaConversions._
import org.joda.time.DateTime
import play.api.Play
import org.jbehave.core.embedder.Embedder.RunningStoriesFailed
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

object RunnerController extends Controller {

  val reportsDir = Play.current.configuration.getString("reports.root.dir").getOrElse("jbehave")
  val storiesDir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")

  def run: Action[AnyContent] = Action.async { implicit request =>
    Future(run(request.body.asJson)).map(out => result(out))
  }

  private[controllers] def run(jsonOption: Option[JsValue]): Map[String, String] = {
    lazy val json = jsonOption.get
    lazy val storyPath = (json \ "storyPath").asOpt[String]
    lazy val classesJson = (json \ "classes").asOpt[List[JsValue]]
    if (jsonOption.isEmpty) {
      noJsonResultMap
    } else if (storyPath.isEmpty) {
      noResultMap("storyPath")
    } else if (classesJson.isEmpty || classesJson.get.size == 0) {
      noResultMap("classes")
    } else {
      val id = DateTime.now.getMillis
      val reportsPath = reportsDir + "/" + id
      val storiesPath = storiesDir + "/" + storyPath.get
      var status = "OK"
      val classes = classesJson.get.map { classJson =>
        val fullName = (classJson \ "fullName").as[String]
        val paramsJson = (classJson \ "params").asOpt[List[JsValue]]
        val params = paramsJson.getOrElse(List()).map { paramJson =>
          val key = (paramJson \ "key").as[String]
          val value = (paramJson \ "value").as[String]
          (key, value)
        }.toMap
        RunnerClass(fullName, Map())
      }
      try {
        new Runner(storiesPath, classes, s"../$reportsPath").run()
      } catch {
        case rsf: RunningStoriesFailed => status = "FAILED"
        case e: Exception => status = "Error"
      }
      Map(
        "status" -> status,
        "id" -> id.toString,
        "reportsPath" -> s"$reportsPath/view/reports.html"
      )
    }
  }

}

