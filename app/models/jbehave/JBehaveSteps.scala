package models.jbehave

import java.io.File
import org.clapper.classutil.ClassFinder
import play.api.libs.json.{Json, JsValue}
import org.jbehave.core.steps.{StepCandidate, Steps}
import org.jbehave.core.configuration.MostUsefulConfiguration
import scala.collection.JavaConversions._
import com.technologyconversations.bdd.steps.BddParam
import org.jbehave.core.annotations.{Then, When, Given}

class JBehaveSteps(dir: String = "steps") {

  def stepsToJson: JsValue = {
    val stepsMap = stepsCandidates.map { step =>
      val stepString = step.toString
      val stepType = stepString.split(" ")(0)
      Map(
        "type" -> stepType,
        "step" -> stepString.replaceFirst("GIVEN", "Given").replaceFirst("WHEN", "When").replaceFirst("THEN", "Then")
      )
    }
    Json.toJson(Map("steps" -> Json.toJson(stepsMap)))
  }

  def classesToJson: JsValue = {
    val classesMap = classes.map { className =>
      Map(
        "name" -> Json.toJson(className.substring(className.lastIndexOf(".") + 1)),
        "fullName" -> Json.toJson(className),
        "params" -> Json.toJson(classParamsMap(className))
      )
    }
    Json.toJson(Map("classes" -> Json.toJson(classesMap)))
  }

  private[jbehave] def stepsJars = {
    new File(dir).listFiles.filter(_.getName.endsWith(".jar")).toList
  }

  private[jbehave] def classes = {
    ClassFinder(stepsJars).getClasses()
      .filter(classInfo => hasSteps(classInfo.name))
      .map(_.name)
      .toList
  }

  private[jbehave] def hasSteps(className: String): Boolean = {
    for(method <- Class.forName(className).getMethods) {
      if (method.getAnnotation(classOf[Given]) != null ||
          method.getAnnotation(classOf[When]) != null ||
          method.getAnnotation(classOf[Then]) != null) return true
    }
    false
  }

  private[jbehave] def classParams(className: String): List[BddParam] = {
    Class.forName(className).getMethods
      .filter(_.getAnnotation(classOf[BddParam]) != null)
      .map(_.getAnnotation(classOf[BddParam]))
      .toList
  }

  private[jbehave] def classParamsMap(className: String): List[Map[String, String]] = {
    classParams(className).map(param => Map(
      "key" -> param.value(),
      "description" -> param.description()
    ))
  }

  private[jbehave] def steps = {
    val config = new MostUsefulConfiguration()
    classes.map { className =>
      new Steps(config, Class.forName(className).newInstance())
    }
  }

  private[jbehave] def stepsCandidates = {
    def stepsCandidates(steps: List[Steps], candidates: List[StepCandidate]): List[StepCandidate] = {
      if (steps.isEmpty) {
        candidates
      } else {
        stepsCandidates(steps.tail, candidates ::: steps.head.listCandidates().toList)
      }
    }
    stepsCandidates(steps, List()).sortWith(_.toString < _.toString)
  }

}

object JBehaveSteps {

  def apply(): JBehaveSteps = new JBehaveSteps

}
