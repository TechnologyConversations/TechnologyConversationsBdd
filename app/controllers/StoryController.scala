package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Story

object StoryController extends Controller {

  def index = Action {
    // TODO Move "stories" to config
    Ok(views.html.stories(Story.all("stories"), form))
  }

  // TODO Test
  def create = Action(parse.multipartFormData) { implicit request =>
    request.body.file("storyFile").map { storyFile =>
      import java.io.File
      val fileName = storyFile.filename
      val contentType = storyFile.contentType
      val dir = new File("stories")
      if (!dir.exists) dir.mkdir
      val file = new File(s"${dir.getPath}/$fileName")
      if (file.exists) {
        Conflict(views.html.message(
          "File Exists",
          "Seems that the file you are trying to upload already exists."))
    } else {
        storyFile.ref.moveTo(file)
        Ok(views.html.message(
          "Great success",
          "Your story has been uploaded successfully."))
      }
    }.getOrElse{
      NotFound(views.html.message(
        "File Not Found",
        "Please try again."))
    }
  }

  val form = Form(
    mapping(
      "storyNameInput" -> nonEmptyText
    )(Story.apply)(Story.unapply)
  )

}