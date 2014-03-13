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
      "message" -> s"$node was not found"
    )
  }

  def paramIncorrect(param: String): Result = {
    BadRequest(Json.toJson(paramIncorrectMap(param)))
  }

  def paramIncorrectMap(param: String) = {
    Map(
      "status" -> "ERROR",
      "message" -> s"$param is NOT correct"
    )
  }

  def result(map: Map[String, String]): SimpleResult = {
    if (map.contains("status") && !map("status").equals("OK") && !map("status").equals("FAILED")) {
      BadRequest(Json.toJson(map))
    } else {
      Ok(Json.toJson(map))
    }
  }

}
