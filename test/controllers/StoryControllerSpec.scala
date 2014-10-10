package controllers

import models.Story
import models.db.BddDb
import models.file.BddFile
import org.specs2.mock.Mockito
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, FakeApplication}
import org.specs2.mutable.{After, Specification}
import play.api.libs.json._
import org.specs2.matcher.{JsonMatchers, PathMatchers}
import java.io.File

class StoryControllerSpec extends Specification with PathMatchers with JsonMatchers with Mockito {

  val fakeJsonHeaders = FakeHeaders(Seq("Content-type" -> Seq("application/json")))
  val storiesPath = "data/stories"
  val disabledMongo: Map[String, String] = {
    Map(
      "db.mongodb.enabled" -> "false"
    )
  }

  // TODO Refactor and remove route
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

  "GET /stories/list.json" should {

    "return results of findStories" in {
      val mockedStory = mock[Story]
      val expected = Json.parse("""{"key": "value"}""")
      lazy val controller = new StoryController() {
        override val story = mockedStory
        story.findStories(any[File], any[String], any[String]) returns Option(expected)
      }
      val result = controller.listJson("path/to/some/dir/")(FakeRequest())
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      contentAsJson(result) must equalTo(expected)
    }

    "return BAD_REQUEST when findStories returns an empty option" in {
      val mockedStory = mock[Story]
      lazy val controller = new StoryController() {
        override val story = mockedStory
        story.findStories(any[File], any[String], any[String]) returns Option.empty
      }
      val result = controller.listJson("path/to/some/dir/")(FakeRequest())
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome("application/json")
    }

  }

  // TODO Refactor and remove route
  "PUT /stories/story.json route" should {

    "return BAD_REQUEST if body is NOT JSON" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(PUT, url))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if JSON does not contain path" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, Json.parse("""{"path_does_not_exist": "true"}""")))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return OK if story already exists" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(firstResult) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson)) // Create the story
        status(firstResult) must equalTo(OK)
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, mockJson)) // Update the existing story
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "rename story if path changed" in new MockStory {
      override lazy val mockJsonString =
        """
{
  "path": "my_renamed_test_story.story",
  "originalPath": "my_test_story.story",
  "name": "my_renamed_test_story",
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
      override lazy val storyPath = s"$storiesPath/my_renamed_test_story.story"
      lazy val originalStoryPath = s"$storiesPath/my_test_story.story"
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

      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        new File(originalStoryPath).createNewFile()
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        storyPath must beAnExistingPath
        storyPath must beAFilePath
      }
    }

  }

  // TODO Refactor and remove route
  "POST /stories/story.json route" should {

    "return BAD_REQUEST if body is NOT JSON" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(POST, url))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if JSON does not contain path" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, Json.parse("""{"path_does_not_exist": "true"}""")))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if story already exists" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(firstResult) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson)) // Create the story
        status(firstResult) must equalTo(OK)
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson)) // Try to overwrite existing story
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return OK if JSON contains name" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "save story as a file" in new MockStory {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, mockJson))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        storyPath must beAFilePath
      }
    }

  }

  // TODO Refactor and remove route
  "POST /stories/dir.json route" should {

    val path = "testDir"
    val url = s"/stories/dir.json"
    val fullPath = s"$storiesPath/$path"

    "return BAD_REQUEST if body is NOT JSON" in {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(POST, url))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if JSON does not contain path" in {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, Json.parse("""{"path_does_not_exist": "true"}""")))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "create directory" in new AfterStoryControllerSpec(fullPath) {
      running(FakeApplication(additionalConfiguration = disabledMongo)) {
        val json = Json.parse(s"""{"path": "$path"}""")
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, json))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        fullPath must beAnExistingPath
        fullPath must beADirectoryPath
      }
    }

  }

  // TODO Refactor and remove route
  "DELETE /stories/story.json route" should {

    "delete story from the specified path" in new MockStory {
      running(FakeApplication()) {
        new File(storyPath).createNewFile
        val Some(result) = route(FakeRequest(DELETE, s"/stories/story/$story"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        new File(storyPath).exists must beFalse
      }
    }

  }

  "StoryControlller#storiesFromFileToMongoDb" should {

    lazy val controller = new StoryController() {
      override val story = mock[Story]
      story.storiesFromFileToMongoDb(any[String]) returns true
    }

    "return OK" in {
      val result = controller.storiesFromFileToMongoDb()(FakeRequest())
      status(result) must equalTo(OK)
    }

    "return JSON" in {
      val result = controller.storiesFromFileToMongoDb()(FakeRequest())
      contentType(result) must beSome("application/json")
      contentAsString(result) must /("meta") */("message" -> "OK")
    }

    "call Story#storiesFromFileToMongoDb" in {
      val mockedStory = mock[Story]
      lazy val controller = new StoryController() {
        override val story = mockedStory
      }
      controller.storiesFromFileToMongoDb()(FakeRequest())
      there was one(mockedStory).storiesFromFileToMongoDb(any[String])
    }

    "return BadRequest when storiesFromFileToMongoDb failed" in {
      val mockedStory = mock[Story]
      lazy val controller = new StoryController() {
        override val story = mockedStory
        story.storiesFromFileToMongoDb(any[String]) returns false
      }
      val result = controller.storiesFromFileToMongoDb()(FakeRequest())
      status(result) must equalTo(BAD_REQUEST)
    }

  }

  class MockStory extends After {

    val url = "/stories/story.json"
    lazy val story = "my_test_story.story"
    lazy val storyPath = s"$storiesPath/$story"
    lazy val mockJsonString =
      """
{
  "path": "my_test_story.story",
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

  class AfterStoryControllerSpec(path: String) extends After {

    override def after = {
      val file = new File(path)
      if (file.exists) {
        file.delete
      }
    }

  }

}

