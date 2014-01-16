package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Story

object Application extends Controller {

  def index = Action {
    Redirect(routes.Application.stories)
  }

  def stories = Action {
    Ok(views.html.index(Story.all(), storyForm))
  }

  def newStory = Action { implicit request =>
    storyForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Story.all(), errors)),
      name => {
        Story.create(name)
        Redirect(routes.Application.stories)
      }
    )
  }

  def deleteStory(id: Long) = Action {
    Story.delete(id)
    Redirect(routes.Application.stories)
  }

  val storyForm = Form(
    "name" -> nonEmptyText
  )

}