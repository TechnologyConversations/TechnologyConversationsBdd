package models.jbehave

import play.api.libs.json.{Json, JsValue}
import java.lang.reflect.Method
import org.jbehave.core.annotations.{Then, When, Given, Composite}
import java.lang.annotation.Annotation
import java.io.File
import models._
import groovy.lang.GroovyClassLoader

trait JBehaveComposites {

  def content: String

  def classesToJson(files: List[String]): JsValue = {
    Json.toJson(classesList(files))
  }

  def groovyClassesToJson(files: List[String]): JsValue = {
    Json.toJson(groovyClassesList(files))
  }

  def classToText(json: JsValue): String = {
    val packageOption = (json \ "package").asOpt[String]
    val classOption = (json \ "class").asOpt[String]
    val compositesOption = (json \ "composites").asOpt[List[JsValue]]
    validateJavaJson(packageOption, classOption, compositesOption)
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

  def groovyClassToText(json: JsValue): String = {
    val classOption = (json \ "class").asOpt[String]
    val compositesOption = (json \ "composites").asOpt[List[JsValue]]
    validateGroovyJson(classOption, compositesOption)
    val composites = compositesOption.get.map{ composite =>
      JBehaveComposite(
        (composite \ "stepText").as[String],
        (composite \ "compositeSteps" \\ "step").map(_.as[String]).toList
      )
    }.toList
    views.html.jBehaveGroovyComposites.render(
      classOption.get,
      composites
    ).toString().trim
  }

  def classToJson(className: String): JsValue = {
    Json.toJson(classMap(className))
  }

  def groovyClassToJson(dirPath: String, className: String): JsValue = {
    Json.toJson(groovyClassMap(dirPath, className))
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

  private def groovyClassesList(files: List[String]) = {
    files
      .filter(_.endsWith(".groovy"))
      .map(file => Map("path" -> file))
  }

  private def classMap(className: String) = {
    val compositeClass = Class.forName(className)
    Map(
      "package" -> Json.toJson(compositeClass.getPackage.getName),
      "class" -> Json.toJson(compositeClass.getSimpleName),
      "composites" -> Json.toJson(methodCollection(compositeClass.getMethods.toList))
    )
  }

  private def groovyClassMap(path: String, className: String) = {
    val clazz = new GroovyClassLoader().parseClass(new File(path + "/" + className))
    Map(
      "class" -> Json.toJson(className.replace(".groovy", "")),
      "composites" -> Json.toJson(methodCollection(clazz.getMethods.toList))
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

  private def validateJavaJson(packageOption: Option[String],
                           classOption: Option[String],
                           compositesOption: Option[List[JsValue]]) {
    if (packageOption.isEmpty) {
      throw new Exception(noNodeMessage("package"))
    } else if (classOption.isEmpty) {
      throw new Exception(noNodeMessage("class"))
    }
    validateJson(classOption.get, compositesOption.get)
  }

  private def validateGroovyJson(classOption: Option[String], compositesOption: Option[List[JsValue]]) {
    if (classOption.isEmpty) {
      throw new Exception(noNodeMessage("class"))
    }
    validateJson(classOption.get, compositesOption.get)
  }

  private def validateJson(className: String, composites: List[JsValue]) {
    verifyJavaNaming(className)
    for(composite <- composites) {
      val stepText = (composite \ "stepText").as[String]
      if (!stepText.matches("""(Given|When|Then) .+""")) {
        throw new Exception(notGivenWhenThenMessage(stepText))
      }
      verifyStep(stepText)
      val compositeStepsOption = (composite \ "compositeSteps").asOpt[List[JsValue]]
      if (compositeStepsOption.isEmpty) {
        throw new Exception(noNodeMessage("compositeSteps"))
      }
      for(compositeStep <- compositeStepsOption.get) {
        val step = (compositeStep \ "step").as[String]
        if (!step.matches("""(Given|When|Then) .+""")) {
          throw new Exception(notGivenWhenThenMessage(step))
        }
        verifyStep(step)
      }
    }
  }

  private def verifyStep(step: String) {
    val params = "<.+?>".r.findAllIn(step).toSet[String]
    for (param <- params) {
      verifyJavaNaming(param.tail.init)
    }
  }

  private def verifyJavaNaming(name: String) {
    if (!name.matches("""[a-zA-Z_$][a-zA-Z\d_$]*""")) {
      throw new Exception(nameIsIncorrectMessage(name))
    }
  }


}