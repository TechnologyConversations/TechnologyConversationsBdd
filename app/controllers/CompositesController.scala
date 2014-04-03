package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.Composites
import play.api.Play
import play.api.libs.json.Json
import java.io.File

object CompositesController extends Controller {

  val dir = Play.current.configuration.getString("composites.root.dir").getOrElse("composites")

  def classesToJson: Action[AnyContent] = Action {
    val composites = Composites(dir)
    Ok(composites.classesToJson(composites.list()))
  }

  def classToJson(fullClassName: String): Action[AnyContent] = Action {
    try {
      Ok(Composites(dir).classToJson(fullClassName))
    } catch {
      case _: ClassNotFoundException => paramIncorrect("fullClassName")
    }
  }

  def putClass: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    if (jsonOption.isEmpty) {
      noJsonResult
    } else {
      try {
        val composites = Composites(dir)
        val json = jsonOption.get
        val classText = composites.classToText(json)
        val packageName = (json \ "package").as[String]
        val className = (json \ "class").as[String]
        val path = fullClassPath(s"$packageName.$className")
        composites.save(path, classText, overwrite = true)
        okJson("Class was saved successfully")
      } catch {
        case e: Exception => errorJson(e.getMessage)
      }
    }
  }

  def deleteClass(fullClassName: String): Action[AnyContent] = Action { implicit request =>
    Composites(dir).delete(fullClassPath(fullClassName))
    okJson(s"Class $fullClassName has been deleted")
  }

  private def fullClassPath(fullClassName: String) = {
    "app" + File.separator + fullClassName.replace(".", File.separator) + ".java"
  }

}
