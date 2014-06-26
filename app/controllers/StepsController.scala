package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import models.{Composites, Steps}
import play.api.Play

object StepsController extends Controller {

  val dir = Play.current.configuration.getString("steps.root.dir").getOrElse("steps")
  val appDir = Play.current.configuration.getString("app.root.dir").getOrElse("app")

  def listJson: Action[AnyContent] = Action {
    val composites = Composites(compositesDir).list()
    println(composites + "XXXXXXXXXXXX")
    Ok(Steps(dir, composites).stepsToJson)
  }

  def classesJson: Action[AnyContent] = Action {
    Ok(Steps(dir).classesToJson)
  }

}
