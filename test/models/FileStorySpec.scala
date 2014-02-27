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

  "FileStory#delete" should {

    "delete the file" in new FileStoryMock {
      new File(path).createNewFile()
      path must beAnExistingPath
      path must beAFilePath
      delete
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

}
