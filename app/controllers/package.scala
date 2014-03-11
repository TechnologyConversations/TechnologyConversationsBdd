import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.mvc.Results._

package object controllers {

  def noJsonResult: Result = {
    BadRequest(Json.toJson(noJsonResultMap))
  }

  def noJsonResultMap = {
    Map(
      "status" -> "ERROR",
      "message" -> "JSON was not found in the request body"
    )
  }

  def noResult(node: String): Result = {
    BadRequest(Json.toJson(noResultMap(node)))
  }

  def noResultMap(node: String) = {
    Map(
      "status" -> "ERROR",
      "message" -> "$node was not found"
    )
  }

  def result(map: Map[String, String]): SimpleResult = {
    if (map.contains("status") && !map("status").equals("OK") && !map("status").equals("Failed")) {
      BadRequest(Json.toJson(map))
    } else {
      Ok(Json.toJson(map))
    }
  }

}
