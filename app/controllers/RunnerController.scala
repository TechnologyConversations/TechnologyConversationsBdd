package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.{JBehaveRunner, JBehaveSteps}
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import org.joda.time.DateTime
import play.api.Play

object RunnerController extends Controller {

  val dir = Play.current.configuration.getString("reports.root.dir").getOrElse("jbehave")

  def run: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    lazy val json = jsonOption.get
    lazy val storyPath = (json \ "storyPath").asOpt[String]
    lazy val stepsClasses = (json \ "stepsClasses").asOpt[List[String]]
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (storyPath.isEmpty) {
      noResult("storyPath")
    } else if (stepsClasses.isEmpty || stepsClasses.get.size == 0) {
      noResult("stepsClasses")
    } else {
      val path = dir + "/" + DateTime.now.getMillis
      new JBehaveRunner(storyPath.get, stepsClasses.get, s"../$path").run()
      Ok(Json.toJson(s"""{"status": "OK", "reportsPath": "$path"}"""))
    }
  }

}

