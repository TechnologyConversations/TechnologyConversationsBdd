package models

import java.io.File
import org.clapper.classutil.ClassFinder

class JBehaveSteps(dir: String = "steps") {

  private[models] def stepsJars = {
    new File(dir).listFiles.filter(_.getName.endsWith(".jar")).toList
  }

  private[models] def stepsClasses = {
    ClassFinder(stepsJars).getClasses().filter(_.isConcrete).toList
  }

}

object JBehaveSteps {

  def apply(): JBehaveSteps = new JBehaveSteps

}
