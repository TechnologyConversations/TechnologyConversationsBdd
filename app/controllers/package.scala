import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.mvc.Results._

package object controllers {

  val noJsonResultMessage = "JSON was not found in the request body"
  def noResultMessage(node: String) = {
    s"$node was not found"
  }

  def noJsonResult: Result = {
    BadRequest(Json.toJson(noJsonResultMap))
  }

  def noJsonResultMap = errorMap(noJsonResultMessage)

  def noResult(node: String): Result = {
    BadRequest(Json.toJson(noResultMap(node)))
  }

  def noResultMap(node: String) = errorMap(noResultMessage(node))

  def paramIncorrect(param: String): Result = {
    BadRequest(Json.toJson(paramIncorrectMap(param)))
  }

  def paramIncorrectMap(param: String) = errorMap(s"$param is NOT correct")

  def result(map: Map[String, String]): SimpleResult = {
    if (map.contains("status") && !map("status").equals("OK") && !map("status").equals("FAILED")) {
      BadRequest(Json.toJson(map))
    } else {
      Ok(Json.toJson(map))
    }
  }

  def errorJson(message: String): Result = {
    BadRequest(Json.toJson(errorMap(message)))
  }
  def errorMap(message: String) = {
    Map(
      "status" -> "ERROR",
      "message" -> message
    )
  }

  def okJson(message: String): Result = {
    Ok(Json.toJson(okMap(message)))
  }
  def okMap(message: String) = {
    Map(
      "status" -> "OK",
      "message" -> message
    )
  }

}
