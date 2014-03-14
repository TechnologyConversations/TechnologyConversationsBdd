package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import models.Steps

object StepsController extends Controller {

  def listJson: Action[AnyContent] = Action {
    Ok(Steps().stepsToJson)
  }

  def classesJson: Action[AnyContent] = Action {
    Ok(Steps().classesToJson)
  }

}
