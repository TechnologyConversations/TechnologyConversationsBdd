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
  def nameIsIncorrectMessage(name: String) = {
    s"$name is incorrect. It cannot start with a number or use any character other than letters, digits, underscores and dollar signs."
  }
  def notGivenWhenThenMessage(text: String) = {
    s"$text does not start with Given, When or Then"
  }

}
