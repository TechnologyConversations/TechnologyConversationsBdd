package models.jbehave

import play.api.libs.json.{Json, JsValue}
import java.lang.reflect.Method
import org.jbehave.core.annotations.{Then, When, Given, Composite}
import java.lang.annotation.Annotation
import java.io.File

trait JBehaveComposites {

  def content: String

  def classToText(json: JsValue): String = {
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

  def classesToJson(files: List[String]): JsValue = {
    Json.toJson(classesList(files))
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


}