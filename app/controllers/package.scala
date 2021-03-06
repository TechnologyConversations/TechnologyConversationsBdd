import java.io.File

import play.api.Play
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.mvc.Results._

import scala.io.Source

package object controllers {

  val noJsonResultMessage = "JSON was not found in the request body"
  val stageDir = "target/universal/stage/"

  def toJson(error: Option[String] = Option.empty, message: Option[String] = Option.empty, data: Option[JsValue] = Option.empty) = {
    val meta = Map(
      "error" -> error.getOrElse(""),
      "message" -> message.getOrElse("")
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
  def noJsonResultMap = errorMap(noJsonResultMessage)

  @deprecated("Use toJson instead")
  def noResultMap(node: String) = errorMap(noResultMessage(node))

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

  val compositesDir: String = {
    val dirPath = Play.current.configuration.getString("composites.root.dir").getOrElse("composites")
    absolutePath(dirPath)
  }

  val reportsRelativeDir: String = {
    Play.current.configuration.getString("reports.root.dir").getOrElse("public/jbehave")
  }

  val reportsDir: String = {
    absolutePath(reportsRelativeDir)
  }

  val storiesRelativeDir: String = {
    Play.current.configuration.getString("stories.root.dir").getOrElse("data/stories")
  }

  val tempStoriesRelativeDir: String = {
    Play.current.configuration.getString("stories.temp.dir").getOrElse("data/temp_stories")
  }

  val storiesDir: String = {
    absolutePath(storiesRelativeDir)
  }

  def absolutePath(dirPath: String): String = {
    val dir = new File(dirPath)
    val dirAbsolutePath = dir.getAbsolutePath
    if (new File(dirAbsolutePath).exists()) dirAbsolutePath
    else dirAbsolutePath.replace(stageDir, "")
  }

  lazy val mongoEnabled = Play.current.configuration.getBoolean("db.mongodb.enabled").getOrElse(true)
  lazy val mongoUri = Play.current.configuration.getString("db.mongodb.uri").getOrElse("mongodb://localhost:27017")
  lazy val mongoDb = Play.current.configuration.getString("db.mongodb.db").getOrElse("tcbdd")

}
