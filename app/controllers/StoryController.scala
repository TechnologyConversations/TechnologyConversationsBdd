package controllers

import play.api.mvc._
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._
import models.{StoryUtil, StoryList}
import play.api.Play

object StoryController extends Controller {

  def allJson = Action {
    val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
    Ok(StoryUtil().allJson(dir))
  }

}