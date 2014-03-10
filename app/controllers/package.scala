import play.api.libs.json.Json
import play.api.mvc._
import play.api.mvc.Results._

package object controllers {

  def noJsonResult: Result = {
    BadRequest(Json.parse("""{"status": "ERROR", "message": "JSON was not found in the request body"}"""))
  }

  def noResult(node: String): Result = {
    BadRequest(Json.parse(s"""{"status": "ERROR", "message": "$node was not found"}"""))
  }

}
