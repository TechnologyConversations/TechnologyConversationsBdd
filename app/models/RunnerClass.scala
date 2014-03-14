package models

import scala.collection.JavaConversions._

case class RunnerClass(fullName: String, params: Map[String, String]) {

  def javaParams: java.util.Map[String, String] = params

}
