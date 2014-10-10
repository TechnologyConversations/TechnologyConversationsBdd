package models.file

import org.specs2.mock._
import org.specs2.mutable.Specification
import java.io.File
import org.mockito.Mockito.doNothing
import scala.io.BufferedSource
import org.mockito.Mockito._

class BddFileSpec extends Specification with Mockito {

  val file = mock[File]
  file.getName returns "file"
  val dir = mock[File]
  dir.getName returns "dir"
  val dir1 = mock[File]
  dir1.isDirectory returns true
  dir1.getName returns "dir1"
  val dir2 = mock[File]
  dir2.isDirectory returns true
  dir2.getName returns "dir2"
  val file1 = mock[File]
  file1.isFile returns true
  file1.getName returns "file1"
  val file2 = mock[File]
  file2.isFile returns true
  file2.getName returns "file2"

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

  "BddFile#renameFile" should {

    val source = mock[File]
    val destination = mock[File]

    "rename the file" in {
      val bddFile = BddFile()
      bddFile.renameFile(source, destination)
      there was one(source).renameTo(destination)
    }

    "return true when file was renamed" in {
      val bddFile = BddFile()
      source.renameTo(destination) returns true
      bddFile.renameFile(source, destination) must beTrue
    }

    "return true when file was NOT renamed" in {
      val bddFile = BddFile()
      source.renameTo(destination) returns false
      bddFile.renameFile(source, destination) must beFalse
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
      file.exists returns false
      bddFile.fileToString(file) must equalTo(Option.empty)
    }

    "return empty option when file is directory" in {
      val bddFile = BddFile()
      file.exists returns true
      file.isDirectory returns true
      bddFile.fileToString(file) must equalTo(Option.empty)
    }

  }

  "BddFile#listDirs" should {

    "return the list of all directories" in {
      val bddFile = BddFile()
      file.listFiles() returns Array(dir1, dir2, file1, file2)
      val actual = bddFile.listDirs(file)
      actual must have size 2
      actual must containTheSameElementsAs(Seq(dir1.getName, dir2.getName))
    }

  }

  "BddFile#listFiles" should {

    val bddFile = BddFile()
    dir.exists() returns true
    dir1.exists() returns true
    dir2.exists() returns true

    "return the list of all files" in {
      dir1.listFiles() returns Array()
      dir2.listFiles() returns Array(file2)
      dir.listFiles() returns Array(dir1, dir2, file1)
      val actual = bddFile.listFiles(dir)
      actual must have size 1
      actual must containTheSameElementsAs(Seq(file1.getName))
    }

    "return the list with files including sub directories" in {
      dir1.listFiles() returns Array()
      dir2.listFiles() returns Array(file2)
      dir.listFiles() returns Array(dir1, dir2, file1)
      val actual = bddFile.listFiles(dir, recursive = true)
      actual must have size 2
      actual must containTheSameElementsAs(Seq(file1.getName, dir2.getName + "/" + file2.getName))
    }

    "return empty collection when directory does not exist" in {
      val nonExistentDir = mock[File]
      nonExistentDir.exists() returns false
      bddFile.listFiles(nonExistentDir) must have size 0
    }

    "return only files with specified extension" in {
      val storyFile = mock[File]
      storyFile.isFile returns true
      storyFile.getName returns "this_is_my.story"
      dir.listFiles() returns Array(file1, storyFile)
      val actual = bddFile.listFiles(dir, extension = Option(".story"))
      actual must have size 1
      actual must containTheSameElementsAs(Seq(storyFile.getName))
    }

  }

  "BddFile#createDirectory" should {

    val bddFile = BddFile()

    "create new directory" in {
      val file = mock[File]
      file.exists returns false
      bddFile.createDirectory(file)
      there was one(file).mkdir()
    }

    "NOT create new directory if one already exists" in {
      val file = mock[File]
      file.exists returns true
      bddFile.createDirectory(file)
      there was no(file).mkdir()
    }

    //    "create new directory" {
    //      new File(fullPath).exists must beFalse
    //      createDirectory()
    //      fullPath must beAnExistingPath
    //      fullPath must beADirectoryPath
    //    }
    //
    //    "do nothing if directory already exists" in new BddFileTraitDirMock {
    //      new File(fullPath).exists must beFalse
    //      for(i <- 1 to 3) {
    //        createDirectory()
    //        fullPath must beAnExistingPath
    //        fullPath must beADirectoryPath
    //      }
    //    }
  }

}
