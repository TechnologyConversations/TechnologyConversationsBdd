package controllers

import play.api.mvc._
import models.{Story, StoryList}
import play.api.Play

object StoryController extends Controller {

  def allJson = Action {
    val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
    Ok(StoryList(dir).json)
  }

  def storyJson(storyPath: String) = Action {
    if (storyPath.isEmpty) {
      Ok(Story().json)
    } else {
      val dir = Play.current.configuration.getString("stories.root.dir").getOrElse("stories")
      Ok(Story(dir + "/" + storyPath).json)
    }
  }

}