package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import models.{Composites, Steps}
import play.api.Play

object StepsController extends Controller {

  val dir = Play.current.configuration.getString("steps.root.dir").getOrElse("steps")
  val compositesDir = Play.current.configuration.getString("composites.root.dir").getOrElse("composites")
  val appDir = Play.current.configuration.getString("app.root.dir").getOrElse("app")

  def listJson: Action[AnyContent] = Action {
    val composites = Composites(compositesDir).list().map{ file =>
      file.drop(appDir.length + 1)
    }
    Ok(Steps(dir, composites).stepsToJson)
  }

  def classesJson: Action[AnyContent] = Action {
    Ok(Steps(dir).classesToJson)
  }

}
