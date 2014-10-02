package models.file

import org.specs2.mutable.{After, Specification}
import org.specs2.matcher.PathMatchers
import scala.io.Source
import java.io.{PrintWriter, File}

class BddFileTraitSpec extends Specification with PathMatchers {

  var storyCounter = 0

  "BddFile#content" should {

    "return content of the story" in new BddFileTraitMock() {
      val writer = new PrintWriter(new File(fullPath))
      writer.write("This is mock content")
      writer.close()
      content must be equalTo "This is mock content"
    }

    "return empty content if path is empty" in {
      val bddFile = new BddFileTrait {
        override val dir: String = ""
        override val path: String = ""
      }
      bddFile.content must be equalTo ""
    }

    "return empty content if path points to a file that does not exist" in {
      val bddFile = new BddFileTrait {
        override val dir: String = "test"
        override val path: String = "this_story_does_not_exist.story"
      }
      bddFile.content must be equalTo ""
    }

    "return empty content if path points to a directory" in {
      val bddFile = new BddFileTrait {
        override val dir: String = "test"
        override val path: String = "stories"
      }
      bddFile.content must be equalTo ""
    }

  }

  "BddFile#save" should {

    "save content of the story to the new file" in new BddFileTraitMock {
      saveFile(fullPath, expected, overwrite = false) must beTrue
      fullPath must beAnExistingPath
      fullPath must beAFilePath
      Source.fromFile(fullPath).mkString must be equalTo expected
    }

    "NOT overwrite old content of the file" in new BddFileTraitMock {
      saveFile(fullPath, expected, overwrite = false) must beTrue
      saveFile(fullPath, "something else", overwrite = false) must beFalse
      Source.fromFile(fullPath).mkString must be equalTo expected
    }

    "overwrite old content of the file" in new BddFileTraitMock {
      saveFile(fullPath, "something else", overwrite = false) must beTrue
      saveFile(fullPath, expected, overwrite = true) must beTrue
      Source.fromFile(fullPath).mkString must be equalTo expected
    }

  }

  "BddFile#delete" should {

    "delete the file" in new BddFileTraitMock {
      new File(fullPath).createNewFile()
      fullPath must beAnExistingPath
      fullPath must beAFilePath
      delete(fullPath)
      new File(fullPath).exists must beFalse
    }

    "delete the empty directory" in {
      val story = new BddFileTrait {
        storyCounter += 1
        override val dir = "test"
        val path = s"stories/myTestDir$storyCounter"
      }
      val path = story.dir + "/" + story.path
      new File(path).mkdir()

      path must beAnExistingPath
      path must beADirectoryPath
      story.delete(story.fullPath)
      new File(path).exists must beFalse
    }

    "delete the directory with files and sub directories" in {
      val story = new BddFileTrait {
        storyCounter += 1
        val dir = "test"
        val path = s"stories/myTestDir$storyCounter"
      }
      val path = story.dir + "/" + story.path
      new File(path).mkdir()
      new File(s"$path/subDir").mkdir()
      new File(s"$path/file1").createNewFile
      new File(s"$path/file2").createNewFile
      path must beAnExistingPath
      path must beADirectoryPath
      story.delete(story.fullPath)
      new File(path).exists must beFalse
    }

  }

  "BddFile#list" should {

    val bddFile = new BddFileTrait {
      override val path: String = "jbehave/1394658780515"
      override val dir: String = "test"
    }

    "return List instance" in {
      bddFile.list() must beAnInstanceOf[List[String]]
    }

    "list all files recursively" in {
      val path = bddFile.dir + "/" + bddFile.path
      val expected = new File(path).listFiles.filter(_.isFile).length + new File(s"$path/view").listFiles.length
      bddFile.list() must have size expected
    }

  }

  trait BddFileTraitMock extends BddFileTrait with After {

    storyCounter += 1
    val dir = "test"
    lazy val path = s"stories/temp_story$storyCounter.story"
    val expected = "Some invented content"

    delete(fullPath)

    override def after = {
      delete(fullPath)
    }

  }

  trait BddFileTraitDirMock extends BddFileTrait with After {

    storyCounter += 1
    val dir = "test"
    lazy val path = s"stories/temp_dir$storyCounter"
    val expected = "Some invented content"

    delete(fullPath)

    override def after = {
      delete(fullPath)
    }

  }

}
