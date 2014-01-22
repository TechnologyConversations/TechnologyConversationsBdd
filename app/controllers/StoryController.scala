package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Story

object StoryController extends Controller {

  def index = Action {
    Ok(views.html.stories(Story.all(), form))
  }

  // TODO Test
  def create = Action { implicit request =>
    form.bindFromRequest.fold(
      errors => BadRequest(views.html.stories(Story.all(), errors)),
      story => {
        Story.create(story)
        Redirect(routes.StoryController.index)
      }
    )
  }

  // TODO Test
  def delete(name: String) = Action {
    Story.delete(name)
    Redirect(routes.StoryController.index)
  }

  // TODO Test
  val form = Form(
    mapping(
      "storyNameInput" -> nonEmptyText
    )(Story.apply)(Story.unapply)
  )

}