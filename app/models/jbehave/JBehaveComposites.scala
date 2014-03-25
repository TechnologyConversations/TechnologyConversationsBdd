package models.jbehave

import play.api.libs.json.JsValue

class JBehaveComposites {

  def toText(json: JsValue): String = {
    val compositePackage = (json \ "package").as[String]
    val compositeClass = (json \ "class").as[String]
    val composites = (json \ "composites" \\ "composite").map{ composite =>
      JBehaveComposite(
        (composite \ "stepText").as[String],
        (composite \ "compositeSteps" \\ "step").map { step =>
          step.as[String]
        }.toList
      )
    }.toList
    views.html.jBehaveComposites.render(
      compositePackage,
      compositeClass,
      composites
    ).toString().trim
  }

  // TODO toJson

}