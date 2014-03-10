package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.{JBehaveRunner, JBehaveSteps}
import play.api.libs.json.Json
import scala.collection.JavaConversions._

object RunnerController extends Controller {

  def run: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    lazy val json = jsonOption.get
    lazy val storyPathOption = (json \ "storyPath").asOpt[String]
    if (jsonOption.isEmpty) {
      noJsonResult
    } else if (storyPathOption.isEmpty) {
      noResult("storyPath")
    } else {
      val steps = List("com.technologyconversations.bdd.steps.WebSteps")
      new JBehaveRunner("/stories", steps).run()
      Ok(Json.toJson("""{"status": "OK", "storyPath": "xxx"}""".stripMargin))
    }
  }

}
