package models.file

import org.specs2.mutable.Specification

class FileStorySpec extends Specification {

  "FileStory" should {

    "extend BddStory" in {
      new FileTraitStory {
        override val path: String = ""
        override val dir: String = ""
      } must beAnInstanceOf[BddFileTrait]
    }

  }

}
