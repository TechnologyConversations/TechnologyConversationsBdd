package models

import org.specs2.mutable.{After, Specification}
import org.specs2.matcher.PathMatchers
import scala.io.Source
import java.io.{PrintWriter, File}

class FileStorySpec extends Specification with PathMatchers {

  var storyCounter = 0

  "FileStory#content" should {

    "return content of the story" in new FileStoryMock() {
      val writer = new PrintWriter(new File(path))
      writer.write("This is mock content")
      writer.close()
      content must be equalTo "This is mock content"
    }

    "return empty content if path is empty" in {
      val fileStory = new FileStory {
        override val path: String = ""
      }
      fileStory.content must be equalTo ""
    }

    "return empty content if path points to a file that does not exist" in {
      val fileStory = new FileStory {
        override val path: String = "this_story_does_not_exist.story"
      }
      fileStory.content must be equalTo ""
    }

    "return empty content if path points to a directory" in {
      val fileStory = new FileStory {
        override val path: String = "stories"
      }
      fileStory.content must be equalTo ""
    }

  }

  "FileStory#save" should {

    "save content of the story to the new file" in new FileStoryMock {
      save(expected, overwrite = false) must beTrue
      path must beAnExistingPath
      path must beAFilePath
      Source.fromFile(path).mkString must be equalTo expected
    }

    "NOT overwrite old content of the file" in new FileStoryMock {
      save(expected, overwrite = false) must beTrue
      save("something else", overwrite = false) must beFalse
      Source.fromFile(path).mkString must be equalTo expected
    }

    "overwrite old content of the file" in new FileStoryMock {
      save("something else", overwrite = false) must beTrue
      save(expected, overwrite = true) must beTrue
      Source.fromFile(path).mkString must be equalTo expected
    }

  }

  "FileStory#createDirectory" should {

    "create new directory" in new FileStoryDirMock {
      new File(path).exists must beFalse
      createDirectory
      path must beAnExistingPath
      path must beADirectoryPath
    }

    "do nothing if directory already exists" in new FileStoryDirMock {
      new File(path).exists must beFalse
      for(i <- 1 to 3) {
        createDirectory
        path must beAnExistingPath
        path must beADirectoryPath
      }
    }

  }

  "FileStory#delete" should {

    "delete the file" in new FileStoryMock {
      new File(path).createNewFile()
      path must beAnExistingPath
      path must beAFilePath
      delete
      new File(path).exists must beFalse
    }

    "delete the empty directory" in {
      val story = new FileStory {
        storyCounter += 1
        val path = s"test/stories/myTestDir$storyCounter"
      }
      val path = story.path
      new File(path).mkdir()
      path must beAnExistingPath
      path must beADirectoryPath
      story.delete
      new File(path).exists must beFalse
    }

    "delete the directory with files and sub directories" in {
      val story = new FileStory {
        storyCounter += 1
        val path = s"test/stories/myTestDir$storyCounter"
      }

      val path = story.path
      new File(path).mkdir()
      new File(s"$path/subDir").mkdir()
      new File(s"$path/file1").createNewFile
      new File(s"$path/file2").createNewFile
      path must beAnExistingPath
      path must beADirectoryPath
      story.delete
      new File(path).exists must beFalse
    }

  }

  trait FileStoryMock extends FileStory with After {

    storyCounter += 1
    lazy val path = s"test/stories/temp_story$storyCounter.story"
    val expected = "Some invented content"

    delete

    override def after = {
      delete
    }

  }

  trait FileStoryDirMock extends FileStory with After {

    storyCounter += 1
    lazy val path = s"test/stories/temp_dir$storyCounter"
    val expected = "Some invented content"

    delete

    override def after = {
      delete
    }

  }

}
