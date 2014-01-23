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
      val expected = (1 to 3).map(number => Story("story" + number + ".story")).toList
      Story.all("test/data") must equalTo(expected)
    }

  }

}
