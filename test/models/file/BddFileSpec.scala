package models.file

import org.specs2.matcher.PathMatchers
import org.specs2.mock._
import org.specs2.mutable.Specification
import java.io.File
import org.mockito.Mockito.doNothing

class BddFileSpec extends Specification with PathMatchers with Mockito {

  "BddFile#saveFile" should {

    val content = "SOME CONTENT"
    val file = mock[File]

    "return false if file exists and should not be overridden" in {
      val bddFile = spy(BddFile())
      file.exists() returns true
      val actual = bddFile.saveFile(file, content, overwrite = false)
      actual must beFalse
    }

    "create parent dir if specified" in {
      val bddFile = spy(BddFile())
      doNothing().when(bddFile).writeStringToFile(any[File], anyString)
      val parentDir = mock[File]
      file.exists() returns false
      file.getParentFile returns parentDir
      bddFile.saveFile(file, content, overwrite = false)
      there was one(parentDir).mkdirs()
    }

    "NOT create parent dir if NOT specified" in {
      val bddFile = spy(BddFile())
      doNothing().when(bddFile).writeStringToFile(any[File], anyString)
      val parentDir = mock[File]
      file.exists() returns false
      file.getParentFile returns null
      bddFile.saveFile(file, content, overwrite = false)
      there was no(parentDir).mkdirs()
    }

    "return true when file does not exist" in {
      val bddFile = spy(BddFile())
      doNothing().when(bddFile).writeStringToFile(any[File], anyString)
      file.exists() returns false
      val actual = bddFile.saveFile(file, content, overwrite = false)
      actual must beTrue
    }

    "return true when file exist but should be overwritten" in {
      val bddFile = spy(BddFile())
      doNothing().when(bddFile).writeStringToFile(any[File], anyString)
      file.exists() returns true
      val actual = bddFile.saveFile(file, content, overwrite = true)
      actual must beTrue
    }

    "call writeStringToFile" in {
      val bddFile = spy(BddFile())
      doNothing().when(bddFile).writeStringToFile(any[File], anyString)
      file.exists() returns true
      bddFile.saveFile(file, content, overwrite = true)
      there was one(bddFile).writeStringToFile(file, content)
    }

  }

}
