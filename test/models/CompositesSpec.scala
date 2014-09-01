package models

import org.specs2.mutable.Specification

import models.jbehave.JBehaveComposites
import models.file.FileTraitComposites

class CompositesSpec extends Specification {

  "Composites" should {

    val composites = Composites("", "")

    "extend JBehaveComposites" in {
      composites must beAnInstanceOf[JBehaveComposites]
    }

    "extend FileComposites" in {
      composites must beAnInstanceOf[FileTraitComposites]
    }

  }

}
