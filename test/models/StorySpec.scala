package models

import org.specs2.mutable.Specification

class StorySpec extends Specification {

  "Story class" should {

    "have name without extension" in {
      Story("myStory.story").name must be matching "myStory"
    }

  }

  "Story object" should {

    "return all stories from files ending with .story" in {
      Story.all("test/data") must have size(3)
    }

  }

}
