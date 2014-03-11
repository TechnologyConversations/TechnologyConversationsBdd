package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.{JBehaveRunner, JBehaveSteps}
import play.api.libs.json.Json
import scala.collection.JavaConversions._
import org.joda.time.DateTime
import play.api.Play

object RunnerController extends Controller {

  val reportsDir = Play.current.configuration.getString("reports.root.dir").getOrElse("jbehave")
  val storiesDir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")

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
      val reportsPath = reportsDir + "/" + DateTime.now.getMillis
      val storiesPath = storiesDir + "/" + storyPath.get
      new JBehaveRunner(storiesPath, stepsClasses.get, s"../$reportsPath").run()
      Ok(Json.toJson(s"""{"status": "OK", "reportsPath": "$reportsPath"}"""))
    }
  }

}

