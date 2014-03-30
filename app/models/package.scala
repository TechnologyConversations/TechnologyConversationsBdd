import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.mvc.Results._

package object models {

  def noNodeMessage(node: String) = {
    s"$node was not found"
  }
  def nodeIsIncorrectMessage(node: String) = {
    s"$node is incorrect"
  }

}
