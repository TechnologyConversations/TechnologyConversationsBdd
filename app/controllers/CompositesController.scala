package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.Composites
import play.api.Play
import java.io.File

object CompositesController extends Controller {

  val javaCompositesDir = Play.current.configuration.getString("javaComposites.root.dir").getOrElse("app/composites")
  def classesToJson: Action[AnyContent] = Action {
    val composites = Composites(javaCompositesDir)
    Ok(composites.classesToJson(composites.list()))
  }

  def groovyClassesToJson: Action[AnyContent] = Action {
    val composites = Composites(compositesDir)
    Ok(composites.groovyClassesToJson(composites.list()))
  }

  def classToJson(fullClassName: String): Action[AnyContent] = Action {
    try {
      Ok(Composites(javaCompositesDir).classToJson(fullClassName))
    } catch {
      case _: ClassNotFoundException => BadRequest(toJson(message = Option("fullClassName is NOT correct")))
    }
  }

  def groovyClassToJson(className: String): Action[AnyContent] = Action {
    if (!new File(s"$compositesDir/$className").exists()) {
      BadRequest(toJson(message = Option("className is NOT correct")))
    } else {
      Ok(Composites(compositesDir).groovyClassToJson(compositesDir, className))
    }
  }

  def putClass: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    if (jsonOption.isEmpty) {
      BadRequest(toJson(message = Option("JSON was not found in the request body")))
    } else {
      try {
        val composites = Composites(javaCompositesDir)
        val json = jsonOption.get
        val classText = composites.classToText(json)
        val packageName = (json \ "package").as[String]
        val className = (json \ "class").as[String]
        val path = fullClassPath(s"$packageName.$className")
        composites.saveFile(path, classText, overwrite = true)
        Ok(toJson(message = Option("Class was saved successfully")))
      } catch {
        case e: Exception => errorJson(e.getMessage)
      }
    }
  }

  def putGroovyClass: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    if (jsonOption.isEmpty) {
      BadRequest(toJson(message = Option("JSON was not found in the request body")))
    } else {
      try {
        val composites = Composites(compositesDir)
        val json = jsonOption.get
        val classText = composites.groovyClassToText(json)
        val className = (json \ "class").as[String]
        val path = s"$compositesDir/$className.groovy"
        composites.saveFile(path, classText, overwrite = true)
        Ok(toJson(message = Option("Class was saved successfully")))
      } catch {
        case e: Exception => errorJson(e.getMessage)
      }
    }
  }

  def deleteClass(fullClassName: String): Action[AnyContent] = Action { implicit request =>
    Composites(compositesDir).delete(fullClassPath(fullClassName))
    Ok(toJson(message = Option(s"Class $fullClassName has been deleted")))
  }

  def deleteGroovyClass(className: String): Action[AnyContent] = Action { implicit request =>
    val path = s"$compositesDir/$className.groovy"
    Composites(javaCompositesDir).delete(path)
    Ok(toJson(message = Option(s"Class $className has been deleted")))
  }

  private def fullClassPath(fullClassName: String) = {
    "app" + File.separator + fullClassName.replace(".", File.separator) + ".java"
  }

}
