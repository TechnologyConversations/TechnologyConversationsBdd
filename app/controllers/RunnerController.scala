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
import java.io.File

object RunnerController extends Controller {

  val storiesDir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")

  def run: Action[AnyContent] = Action.async { implicit request =>
    Future(run(request.body.asJson)).map(out => result(out))
  }

  private[controllers] def run(jsonOption: Option[JsValue]): Map[String, String] = {
    lazy val json = jsonOption.get
    lazy val storyPaths = (json \ "storyPaths").asOpt[List[JsValue]]
    lazy val classesJson = (json \ "classes").asOpt[List[JsValue]]
    lazy val compositesJsonOpt = (json \ "composites").asOpt[List[JsValue]]
    lazy val groovyCompositesJsonOpt = (json \ "groovyComposites").asOpt[List[JsValue]]
    if (jsonOption.isEmpty) {
      noJsonResultMap
    } else if (storyPaths.isEmpty) {
      noResultMap("storyPaths")
    } else if (classesJson.isEmpty || classesJson.get.size == 0) {
      noResultMap("classes")
    } else {
      val reportsId = DateTime.now.getMillis
      val fullStoryPaths = storyPaths.get.map { path =>
        storiesDir + "/" + (path \ "path").as[String]
      }
      var status = "OK"
      try {
        new Runner(
          fullStoryPaths,
          classesFromSteps(classesJson.get) ::: classesFromComposites(compositesJsonOpt),
          groovyCompositesJsonOpt.getOrElse(List()).map(composite => (composite \ "path").as[String]),
          reportsRelativeDir + "/" + reportsId
        ).run()
      } catch {
        case rsf: RunningStoriesFailed => status = "FAILED"
        case e: Exception => status = "Error"
      }
      Map(
        "status" -> status,
        "id" -> reportsId.toString,
        "reportsPath" -> s"$reportsId/view/reports.html"
      )
    }
  }

  // TODO Move to a separate class (without Controller) for easier testing
  private[controllers] def classesFromSteps(json: List[JsValue]) = {
    json.map { element =>
      val fullName = (element \ "fullName").as[String]
      val paramsJson = (element \ "params").asOpt[List[JsValue]]
      val params = paramsJson.getOrElse(List()).map { paramJson =>
        val key = (paramJson \ "key").as[String]
        val value = (paramJson \ "value").asOpt[String].getOrElse("")
        (key, value)
      }.toMap
      RunnerClass(fullName, params)
    }
  }

  // TODO Move to a separate class (without Controller) for easier testing
  private[controllers] def classesFromComposites(jsonOption: Option[List[JsValue]]) = {
    if (jsonOption.isEmpty) {
      List()
    } else {
      jsonOption.get.map {
        element =>
          val packageName = (element \ "package").as[String]
          val className = (element \ "class").as[String]
          RunnerClass(s"$packageName.$className")
      }
    }
  }

}

