package models

import org.specs2.mutable.Specification

class StorySpec extends Specification {

  "Story" should {

    "have name without extension" in {
      Story("myStory.story").name must be matching "myStory"
    }

  }

  "Story#all" should {

    "return all stories from files ending with .story" in {
      Story.all("test/stories") must have size(3)
    }

    "return no stories when the destination directory does not exist" in {
      Story.all("test/stories/non_existent_dir") must have size(0)
    }

    "return no stories when the destination directory is empty" in {
      Story.all("test/stories/empty") must have size(0)
    }

  }

}
