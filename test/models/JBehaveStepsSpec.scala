package models

import org.specs2.mutable.Specification
import java.io.File
import org.clapper.classutil.ClassInfo

class JBehaveStepsSpec extends Specification {

  "JBehaveSteps#stepsJars" should {

    "return a list of all JARs" in {
      val jars = JBehaveSteps().stepsJars
      jars must beAnInstanceOf[List[File]]
      jars.size must beGreaterThan(0)
    }

  }

  "JBehaveSteps#stepsClasses" should {

    "return a list of all classes" in {
      val classes = JBehaveSteps().stepsClasses
      classes must beAnInstanceOf[List[ClassInfo]]
//      Class.forName(classes(0).name).newInstance()
      classes.size must beGreaterThan(0)
    }

  }

}
