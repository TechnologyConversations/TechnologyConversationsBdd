package models.file

import org.specs2.mutable.Specification

class FileCompositesSpec extends Specification {

  "FileComposite" should {

    "extend BddStory" in {
      new FileTraitComposites {
        override val path: String = ""
        override val dir: String = ""
      } must beAnInstanceOf[BddFileTrait]
    }

  }

}
