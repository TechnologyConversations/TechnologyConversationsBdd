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

  }

  trait FileStoryMock extends FileStory with After {

    storyCounter += 1
    lazy val path = s"test/stories/temp_story$storyCounter.story"
    val expected = "Some invented content"

    delete

    override def after = {
      delete
    }

    def delete = {
      val file = new File(path)
      if (file.exists) {
        file.delete
      }
    }

  }

}
