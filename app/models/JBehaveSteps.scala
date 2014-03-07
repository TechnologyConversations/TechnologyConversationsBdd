package models

import java.io.File
import org.clapper.classutil.ClassFinder
import play.api.libs.json.{Json, JsValue}
import org.jbehave.core.steps.{StepCandidate, Steps}
import org.jbehave.core.configuration.MostUsefulConfiguration
import scala.collection.JavaConversions._

class JBehaveSteps(dir: String = "steps") {

  def toJson: JsValue = {
    val steps = stepsCandidates.map { step =>
      val stepString = step.toString
      val stepType = stepString.split(" ")(0)
      Map(
        "type" -> stepType,
        "step" -> stepString.replaceFirst("GIVEN", "Given").replaceFirst("WHEN", "When").replaceFirst("THEN", "Then")
      )
    }
    Json.toJson(Map("steps" -> Json.toJson(steps)))
  }

  private[models] def stepsJars = {
    new File(dir).listFiles.filter(_.getName.endsWith(".jar")).toList
  }

  private[models] def stepsClasses = {
    ClassFinder(stepsJars).getClasses().filter(_.isConcrete).toList
  }

  private[models] def steps = {
    val config = new MostUsefulConfiguration()
    stepsClasses.map { stepsClass =>
      new Steps(config, Class.forName(stepsClass.name).newInstance())
    }
  }

  private[models] def stepsCandidates = {
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
