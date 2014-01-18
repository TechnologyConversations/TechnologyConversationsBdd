package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Story

trait StoryController { this: Controller =>

  def index = Action {
    Ok(views.html.stories(Story.all(), form))
  }

  def create = Action { implicit request =>
    form.bindFromRequest.fold(
      errors => BadRequest(views.html.stories(Story.all(), errors)),
      name => {
        Story.create(name)
        Redirect(routes.StoryController.index)
      }
    )
  }

  def delete(id: Long) = Action {
    Story.delete(id)
    Redirect(routes.StoryController.index)
  }

  val form = Form(
    "name" -> nonEmptyText
  )
}
object StoryController extends Controller with StoryController