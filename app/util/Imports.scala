package util

import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object Imports {

  lazy val features: JsValue = {
    val featuresSource = Source.fromFile("data/json/features.json", "UTF-8").mkString
    Json.parse(featuresSource)
  }

  def featureIsEnabled(feature: String): Boolean = {
    (features \ feature \ "enabled").as[Boolean]
  }

}
