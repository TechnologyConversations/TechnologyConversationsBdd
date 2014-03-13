package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.Runner
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
    lazy val stepsClasses = (json \ "stepsClasses").asOpt[List[String]]
    if (jsonOption.isEmpty) {
      noJsonResultMap
    } else if (storyPath.isEmpty) {
      noResultMap("storyPath")
    } else if (stepsClasses.isEmpty || stepsClasses.get.size == 0) {
      noResultMap("stepsClasses")
    } else {
      val id = DateTime.now.getMillis
      val reportsPath = reportsDir + "/" + id
      val storiesPath = storiesDir + "/" + storyPath.get
      var status = "OK"
      try {
        new Runner(storiesPath, stepsClasses.get, s"../$reportsPath").run()
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

