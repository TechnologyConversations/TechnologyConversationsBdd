package models

import scala.collection.JavaConversions._

case class RunnerClass(fullName: String, params: Map[String, String] = Map()) {

  def javaParams: java.util.Map[String, String] = params

}
object RunnerClass {

  def runnerClassFromJava(fullName: String, params: java.util.Map[String, String]): RunnerClass = {
    RunnerClass(fullName, params.toMap)
  }

}
