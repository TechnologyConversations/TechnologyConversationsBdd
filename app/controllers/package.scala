import java.io.File

import play.api.Play
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.mvc.Results._

package object controllers {

  val noJsonResultMessage = "JSON was not found in the request body"
  val stageDir = "target/universal/stage/"

  def toJson(error: Option[String] = Option.empty, data: Option[JsValue] = Option.empty) = {
    val meta = Map(
      "error" -> error.getOrElse("")
    )
    val map = Map(
      "meta" -> Json.toJson(meta),
      "data" -> data.getOrElse(Json.parse("{}"))
    )
    Json.toJson(map)
  }

  @deprecated("Use toJson instead")
  def noResultMessage(node: String) = {
    s"$node was not found"
  }

  @deprecated("Use toJson instead")
  def noJsonResult: Result = {
    BadRequest(Json.toJson(noJsonResultMap))
  }

  @deprecated("Use toJson instead")
  def noJsonResultMap = errorMap(noJsonResultMessage)

  @deprecated("Use toJson instead")
  def noResult(node: String): Result = {
    BadRequest(Json.toJson(noResultMap(node)))
  }

  @deprecated("Use toJson instead")
  def noResultMap(node: String) = errorMap(noResultMessage(node))

  @deprecated("Use toJson instead")
  def paramIncorrect(param: String): Result = {
    BadRequest(Json.toJson(paramIncorrectMap(param)))
  }

  @deprecated("Use toJson instead")
  def paramIncorrectMap(param: String) = errorMap(s"$param is NOT correct")

  @deprecated("Use toJson instead")
  def result(map: Map[String, String]): SimpleResult = {
    if (map.contains("status") && !map("status").equals("OK") && !map("status").equals("FAILED")) {
      BadRequest(Json.toJson(map))
    } else {
      Ok(Json.toJson(map))
    }
  }

  @deprecated("Use toJson instead")
  def errorJson(message: String): Result = {
    BadRequest(Json.toJson(errorMap(message)))
  }

  @deprecated("Use toJson instead")
  def errorMap(message: String) = {
    Map(
      "status" -> "ERROR",
      "message" -> message
    )
  }

  @deprecated("Use toJson instead")
  def okJson(message: String): Result = {
    Ok(Json.toJson(okMap(message)))
  }

  @deprecated("Use toJson instead")
  def okMap(message: String) = {
    Map(
      "status" -> "OK",
      "message" -> message
    )
  }

  // TODO Test
  @deprecated("Use toJson instead")
  val compositesDir: String = {
    val dirPath = Play.current.configuration.getString("composites.root.dir").getOrElse("composites")
    absolutePath(dirPath)
  }

  // TODO Test
  @deprecated("Use toJson instead")
  val reportsRelativeDir: String = {
    Play.current.configuration.getString("reports.root.dir").getOrElse("public/jbehave")
  }

  // TODO Test
  @deprecated("Use toJson instead")
  val reportsDir: String = {
    absolutePath(reportsRelativeDir)
  }

  // TODO Test
  @deprecated("Use toJson instead")
  def absolutePath(dirPath: String): String = {
    val dir = new File(dirPath)
    val dirAbsolutePath = dir.getAbsolutePath
    if (new File(dirAbsolutePath).exists()) dirAbsolutePath
    else dirAbsolutePath.replace(stageDir, "")
  }

}
