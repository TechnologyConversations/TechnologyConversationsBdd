package controllers

import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, FakeApplication}
import org.specs2.mutable.{After, Specification}
import play.api.libs.json._
import org.specs2.matcher.PathMatchers
import java.io.File

class StoryControllerSpec extends Specification with PathMatchers {

  "StoryController" should {

    "respond to GET / route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/"))
        status(result) must equalTo(OK)
      }
    }

    "respond to GET /stories/story.json?path=[STORY] route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/story.json?path=story1.story"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "respond to GET /stories/story.json route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/story.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

  }

  "StoryController GET /stories/list.json" should {

    "return JSON" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/list.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return all story files and directories" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/list.json"))
        val files = new File("stories").list.map(_.replace(".story", ""))
        val json = contentAsJson(result)
        val stories = (json \ "stories" \\ "name").map(_.as[String])
        val dirs = (json \ "dirs" \\ "name").map(_.as[String])
        (stories ++ dirs) must haveSize(files.length)
        (stories ++ dirs) must containTheSameElementsAs(files)
      }
    }

    "return all story files and directories inside specified directory" in {
      running(FakeApplication()) {
        val storiesSubDir = new File("stories").listFiles().filter(_.isDirectory)(0)
        val Some(result) = route(FakeRequest(GET, "/stories/list.json?path=" + storiesSubDir.getName))
        val files = storiesSubDir.list.map(_.replace(".story", ""))
        val json = contentAsJson(result)
        val stories = (json \ "stories" \\ "name").map(_.as[String])
        val dirs = (json \ "dirs" \\ "name").map(_.as[String])
        (stories ++ dirs) must haveSize(files.length)
        (stories ++ dirs) must containTheSameElementsAs(files)
      }
    }

  }
  
  "StoryController PUT /stories/story.json route" should {
    
    "return BAD_REQUEST if body is NOT JSON" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(PUT, url))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if JSON does not contain name" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, Json.parse("""{"name_does_not_exist": "true"}""")))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if story does NOT already exist" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return OK if story already exists" in new PostPutStory {
      running(FakeApplication()) {
        val Some(firstResult) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson)) // Create the story
        status(firstResult) must equalTo(OK)
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, mockJson)) // Update the existing story
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "rename story if name changed" in new PostPutStory {
      override lazy val mockJsonString =
        """
{
  "name": "my_renamed_test_story",
  "originalName": "my_test_story",
  "description": "This is description of this story",
  "meta": [ { "element": "integration" }, { "element": "product dashboard" } ],
  "narrative":
  {
    "inOrderTo": "communicate effectively to the business some functionality",
    "asA": "development team",
    "iWantTo": "use Behaviour-Driven Development"
  },
  "givenStories": [ { "story": "story.story" } ],
  "lifecycle":
  {
    "before": [ { "step": "Given a step that is executed before each scenario" } ],
    "after": [ { "step": "Given a step that is executed after each scenario" } ]
  },
  "scenarios":
  [
    {
      "title": "A scenario is a collection of executable steps of different type",
      "meta": [ { "element": "live" }, { "element": "product shopping cart" } ],
      "steps": [ { "step": "Given step represents a precondition to an event" } ],
      "examplesTable": "|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|"
    }
  ]
}""".stripMargin
      override lazy val storyPath = "stories/my_renamed_test_story.story"
      lazy val originalStoryPath = "stories/my_test_story.story"
      override def after = {
        val file = new File(storyPath)
        if (file.exists) {
          file.delete
        }
        val originalFile = new File(originalStoryPath)
        if (originalFile.exists) {
          originalFile.delete
        }
      }

      running(FakeApplication()) {
        new File(originalStoryPath).createNewFile()
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        storyPath must beAnExistingPath
        storyPath must beAFilePath
      }
    }

  }

  "StoryController POST /stories/story.json route" should {

    "return BAD_REQUEST if body is NOT JSON" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if JSON does not contain name" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, Json.parse("""{"name_does_not_exist": "true"}""")))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if story already exists" in new PostPutStory {
      running(FakeApplication()) {
        val Some(firstResult) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson)) // Create the story
        status(firstResult) must equalTo(OK)
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson)) // Try to overwrite existing story
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return OK if JSON contains name" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "save story as a file" in new PostPutStory {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        storyPath must beAFilePath
      }
    }

  }

}

class PostPutStory extends After {

  val url = "/stories/story.json"
  lazy val storyPath = "stories/my_test_story.story"
  val fakeJsonHeaders = FakeHeaders(Seq("Content-type" -> Seq("application/json")))
  lazy val mockJsonString =
    """
{
  "name": "my_test_story",
  "description": "This is description of this story",
  "meta": [ { "element": "integration" }, { "element": "product dashboard" } ],
  "narrative":
  {
    "inOrderTo": "communicate effectively to the business some functionality",
    "asA": "development team",
    "iWantTo": "use Behaviour-Driven Development"
  },
  "givenStories": [ { "story": "story.story" } ],
  "lifecycle":
  {
    "before": [ { "step": "Given a step that is executed before each scenario" } ],
    "after": [ { "step": "Given a step that is executed after each scenario" } ]
  },
  "scenarios":
  [
    {
      "title": "A scenario is a collection of executable steps of different type",
      "meta": [ { "element": "live" }, { "element": "product shopping cart" } ],
      "steps": [ { "step": "Given step represents a precondition to an event" } ],
      "examplesTable": "|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|"
    }
  ]
}""".stripMargin
  val mockJson = Json.parse(mockJsonString)

  override def after = {
    val file = new File(storyPath)
    if (file.exists) {
      file.delete
    }
  }

}
