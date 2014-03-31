package models.jbehave

import play.api.libs.json.{Json, JsValue}
import java.lang.reflect.Method
import org.jbehave.core.annotations.{Then, When, Given, Composite}
import java.lang.annotation.Annotation
import java.io.File
import models.noNodeMessage
import models.nodeIsIncorrectMessage

trait JBehaveComposites {

  def content: String

  def classesToJson(files: List[String]): JsValue = {
    Json.toJson(classesList(files))
  }

  def classToText(json: JsValue): String = {
    val packageOption = (json \ "package").asOpt[String]
    val classOption = (json \ "class").asOpt[String]
    val compositesOption = (json \ "composites").asOpt[List[JsValue]]
    validateJson(packageOption, classOption, compositesOption)
    val composites = compositesOption.get.map{ composite =>
      JBehaveComposite(
        (composite \ "stepText").as[String],
        (composite \ "compositeSteps" \\ "step").map(_.as[String]).toList
      )
    }.toList
    views.html.jBehaveComposites.render(
      packageOption.get,
      classOption.get,
      composites
    ).toString().trim
  }

  def classToJson(className: String): JsValue = {
    Json.toJson(classMap(className))
  }

  private def classesList(files: List[String]) = {
    files.map { file =>
      val separator = File.separator(0)
      val parts = file.stripPrefix(s"app$separator").split(separator)
      Map(
        "package" -> parts.init.mkString("."),
        "class" -> parts.last.stripSuffix(".java")
      )
    }
  }

  private def classMap(className: String) = {
    val compositeClass = Class.forName(className)
    Map(
      "package" -> Json.toJson(compositeClass.getPackage.getName),
      "class" -> Json.toJson(compositeClass.getSimpleName),
      "composites" -> Json.toJson(methodCollection(compositeClass.getMethods.toList))
    )
  }

  private def methodCollection(methods: List[Method], composites: List[Map[String, JsValue]] = List()): List[Map[String, JsValue]] = {
    if (methods == Nil) composites
    else {
      val method = methods.head
      val stepAnnotations = method.getAnnotations.filter { annotation =>
        annotation.isInstanceOf[Given] || annotation.isInstanceOf[When] || annotation.isInstanceOf[Then]
      }
      val compositeAnnotation = method.getAnnotation(classOf[Composite])
      if (stepAnnotations.size > 0 && compositeAnnotation != null) {
        val list = Map(
          "stepText" -> Json.toJson(stepAnnotationValue(stepAnnotations(0))),
          "compositeSteps" -> Json.toJson(compositeAnnotationValue(compositeAnnotation))
        )
        methodCollection(methods.tail, composites :+ list)
      } else {
        methodCollection(methods.tail, composites)
      }
    }
  }

  private def stepAnnotationValue(annotation: Annotation) = {
    annotation match {
      case givenAnnotation: Given => "Given " + givenAnnotation.value()
      case whenAnnotation: When => "When " + whenAnnotation.value()
      case thenAnnotation: Then => "Then " + thenAnnotation.value()
      case _ => ""
    }
  }

  private def compositeAnnotationValue(annotation: Annotation) = {
    annotation.asInstanceOf[Composite].steps().map(step => Map("step" -> step)).toList
  }

  private def validateJson(packageOption: Option[String],
                           classOption: Option[String],
                           compositesOption: Option[List[JsValue]]) {
    if (packageOption.isEmpty) {
      throw new Exception(noNodeMessage("package"))
    } else if (classOption.isEmpty) {
      throw new Exception(noNodeMessage("class"))
    } else if (!classOption.get.matches("""[a-zA-Z_$][a-zA-Z\d_$]*""")) {
      throw new Exception(nodeIsIncorrectMessage("class"))
    } else if (compositesOption.isEmpty) {
      throw new Exception(noNodeMessage("composites"))
    }
    for(composite <- compositesOption.get) {
      if (!(composite \ "stepText").as[String].matches("""(Given|When|Then) .+""")) {
        throw new Exception(nodeIsIncorrectMessage("stepText"))
      }
      val compositeStepsOption = (composite \ "compositeSteps").asOpt[List[JsValue]]
      if (compositeStepsOption.isEmpty) {
        throw new Exception(noNodeMessage("compositeSteps"))
      }
      for(compositeStep <- compositeStepsOption.get) {
        if (!(compositeStep \ "step").as[String].matches("""(Given|When|Then) .+""")) {
          throw new Exception(nodeIsIncorrectMessage("step"))
        }
      }
    }
  }


}