package controllers

import play.api.test.{FakeApplication, FakeRequest, PlaySpecification}
import play.api.mvc.{SimpleResult, Results, Controller}
import scala.concurrent.Future
import play.api.GlobalSettings

class StoryControllerSpec extends PlaySpecification with Results {

  class TestController extends Controller with StoryController

  "StoryController#index" should {
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }
  }

}