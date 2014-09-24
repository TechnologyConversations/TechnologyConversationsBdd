package models.file

import org.specs2.mock._
import org.specs2.mutable.Specification
import java.io.File
import org.mockito.Mockito.doNothing
import scala.io.BufferedSource
import org.mockito.Mockito._

class BddFileSpec extends Specification with Mockito {

  val file = mock[File]
  val dir1 = mock[File]
  dir1.isDirectory returns true
  dir1.getName returns "dir1"
  val dir2 = mock[File]
  dir2.isDirectory returns true
  dir2.getName returns "dir2"
  val file1 = mock[File]
  file1.isFile returns true
  val file2 = mock[File]
  file2.isFile returns true

  "BddFile#saveFile" should {

    val content = "SOME CONTENT"

    "return false if file exists and should not be overwritten" in {
      val bddFile = BddFile()
      file.exists() returns true
      val actual = bddFile.saveFile(file, content, overwrite = false)
      actual must beFalse
    }

    "create parent dir when specified" in {
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

    "call writeStringToFile" in {
      val bddFile = spy(BddFile())
      doNothing().when(bddFile).writeStringToFile(any[File], anyString)
      file.exists() returns true
      bddFile.saveFile(file, content, overwrite = true)
      there was one(bddFile).writeStringToFile(file, content)
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

  }

  "BddFile#deleteFile" should {

    "delete the file" in {
      val bddFile = BddFile()
      bddFile.deleteFile(file)
      there was one(file).delete()
    }

    "return true when file was deleted" in {
      val bddFile = BddFile()
      file.delete() returns true
      bddFile.deleteFile(file) must beTrue
    }

    "return false when file was not deleted" in {
      val bddFile = BddFile()
      file.delete() returns false
      bddFile.deleteFile(file) must beFalse
    }

  }

  "BddFile#fileToString" should {

    val content = "THIS IS CONTENT"

    "return content of the file" in {
      val bddFile = spy(BddFile())
      val source = mock[BufferedSource]
      source.mkString returns content
      doReturn(source).when(bddFile).sourceFromFile(file)
      bddFile.fileToString(file) must equalTo(Option(content))
    }

    "close the source" in {
      val bddFile = spy(BddFile())
      val source = mock[BufferedSource]
      source.mkString returns content
      doReturn(source).when(bddFile).sourceFromFile(file)
      bddFile.fileToString(file)
      there was one(source).close()
    }

    "return empty option when file does not exist" in {
      val bddFile = BddFile()
      file.exists() returns false
      bddFile.fileToString(file) must equalTo(Option.empty)
    }

    "return empty option when file is directory" in {
      val bddFile = BddFile()
      file.exists() returns true
      file.isDirectory() returns true
      bddFile.fileToString(file) must equalTo(Option.empty)
    }

  }

  "BddFile#listDirs" should {

    "return the list of all directories" in {
      val bddFile = BddFile()
      file.listFiles() returns Array(dir1, dir2, file1, file1)
      val actual = bddFile.listDirs(file)
      actual must have size 2
      actual must containTheSameElementsAs(Seq(dir1.getName, dir2.getName))
    }

  }

  "BddFile#listFiles" should {

    "return the list of all files" in {
      val bddFile = BddFile()
      file.listFiles() returns Array(dir1, dir2, file1, file1)
      val actual = bddFile.listFiles(file)
      actual must have size 2
      actual must containTheSameElementsAs(Seq(file1.getName, file2.getName))
    }

  }

}
