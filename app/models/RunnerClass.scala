package models

import scala.collection.JavaConversions._

case class RunnerClass(fullName: String, params: Map[String, String] = Map()) {

  def javaParams: java.util.Map[String, String] = params

}
