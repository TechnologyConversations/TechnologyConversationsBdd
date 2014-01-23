package models

import org.specs2.mutable.Specification

class StorySpec extends Specification {

  "Story model" should {

    "return all stories from files ending with .story" in {
      val expected = (1 to 3).map(number => Story("story" + number + ".story"))
      Story.all("test/data") should equalTo(expected)
    }

  }

}
