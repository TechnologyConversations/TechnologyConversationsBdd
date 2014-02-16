package models

import org.specs2.mutable.{After, Specification}
import org.specs2.matcher.PathMatchers
import scala.io.Source
import java.io.{PrintWriter, File}

class FileStorySpec extends Specification with PathMatchers {

  "FileStory#content" should {

    val writer = new PrintWriter(new File("test/stories/temp_story.story"))
    writer.write("This is mock content")
    writer.close()

    "return content of the story" in new FileStoryMock {
      content must be equalTo "This is mock content"
    }

  }

  "FileStory#put" should {

    val expected = "Some invented content"

    "save content of the story to the file" in new FileStoryMock {
      put(expected)
      path must beAnExistingPath
      path must beAFilePath
      Source.fromFile(path).mkString must be equalTo expected
    }

    "overwrite old content of the file" in new FileStoryMock {
      put(expected)
      put(expected)
      Source.fromFile(path).mkString must be equalTo expected
    }

  }

}

trait FileStoryMock extends FileStory with After {

  val path = "test/stories/temp_story.story"

  override def after = {
    if (new File(path).exists) {
      new File(path).delete
    }
  }

}
