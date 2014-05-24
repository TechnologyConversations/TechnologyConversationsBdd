package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import models.Composites
import play.api.Play
import play.api.libs.json.Json
import java.io.File

object CompositesController extends Controller {

  val javaCompositesDir = Play.current.configuration.getString("javaComposites.root.dir").getOrElse("app/composites")
  val compositesDir = Play.current.configuration.getString("composites.root.dir").getOrElse("composites")

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
      case _: ClassNotFoundException => paramIncorrect("fullClassName")
    }
  }

  def groovyClassToJson(className: String): Action[AnyContent] = Action {
    if (!new File(s"$compositesDir/$className").exists()) {
      paramIncorrect("className")
    } else {
      Ok(Composites(compositesDir).groovyClassToJson(compositesDir, className))
    }
  }

  def putClass: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    if (jsonOption.isEmpty) {
      noJsonResult
    } else {
      try {
        val composites = Composites(javaCompositesDir)
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

  def putGroovyClass: Action[AnyContent] = Action { implicit request =>
    val jsonOption = request.body.asJson
    if (jsonOption.isEmpty) {
      noJsonResult
    } else {
      try {
        val composites = Composites(compositesDir)
        val json = jsonOption.get
        val classText = composites.groovyClassToText(json)
        val className = (json \ "class").as[String]
        val path = s"$compositesDir/$className.groovy"
        composites.save(path, classText, overwrite = true)
        okJson("Class was saved successfully")
      } catch {
        case e: Exception => errorJson(e.getMessage)
      }
    }
  }

  def deleteClass(fullClassName: String): Action[AnyContent] = Action { implicit request =>
    Composites(compositesDir).delete(fullClassPath(fullClassName))
    okJson(s"Class $fullClassName has been deleted")
  }

  def deleteGroovyClass(className: String): Action[AnyContent] = Action { implicit request =>
    val path = s"$compositesDir/$className.groovy"
    Composites(javaCompositesDir).delete(path)
    okJson(s"Class $className has been deleted")
  }

  private def fullClassPath(fullClassName: String) = {
    "app" + File.separator + fullClassName.replace(".", File.separator) + ".java"
  }

}
