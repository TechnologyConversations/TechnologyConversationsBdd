package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;
import com.technologyconversations.bdd.steps.util.BddVariable;

public class TcBddComposites {
    
        @Given("Web user is in the Story page with pending steps")
        @Composite(steps = {"When File is copied from public/stories/test/dummyPending.story to public/stories/testDirectory/testPendingStory.story", "Given Web address /page/stories/view/testDirectory/testPendingStory is opened", "Given Web page is refreshed", "When Web user clicks the element expandPanels"})
        public void compositeStep0() { }
    
        @Given("Web user is in the Story page with failed steps")
        @Composite(steps = {"When File is copied from public/stories/test/dummyFailed.story to public/stories/testDirectory/testFailedStory.story", "Given Web address /page/stories/view/testDirectory/testFailedStory is opened", "Given Web page is refreshed", "When Web user clicks the element expandPanels"})
        public void compositeStep1() { }
    
        @Given("Web user is in the Story page with all successful steps")
        @Composite(steps = {"When File is copied from public/stories/test/dummySuccess.story to public/stories/testDirectory/testSuccessStory.story", "Given Web address /page/stories/view/testDirectory/testSuccessStory is opened", "Given Web page is refreshed", "When Web user clicks the element expandPanels"})
        public void compositeStep2() { }
    
        @Given("Web user is in the New Story page")
        @Composite(steps = {"Given File public/storie/testDirectory/newStory.story does NOT exist", "Given Web address /page/stories/new/testDirectory/ is opened", "When Web user clicks the element expandPanels"})
        public void compositeStep3() { }
    
        @When("Web user fills in the existing story form")
        @Composite(steps = {"When Web user sets value story description to the element storyDescription", "When Web user sets value story meta 1 to the element storyMeta1", "When Web user sets value accomplish something to the element storyNarrativeInOrderTo", "When Web user sets value user to the element storyNarrativeAsA", "When Web user sets value do something to the element storyNarrativeIWantTo", "When Web user clicks the element addStoryGivenStory", "When Web user sets value path/to/my/given/story to the element storyGivenStory1", "When Web user sets value Given this is before step to the element storyLifecycleBeforeStep1", "When Web user sets value Given this is after step to the element storyLifecycleAfterStep1"})
        public void compositeStep4() { }
    
        @Then("Web story is deleted")
        @Composite(steps = {"Then File public/stories/testDirectory/testStory.story does NOT exist"})
        public void compositeStep5() { }
    
        @Then("Web story is reverted to the original version")
        @Composite(steps = {"Then Web element storyDescription should NOT have value @storyDescription", "Then Web element storyMeta1 should NOT have value @storyMeta1", "Then Web element storyNarrativeInOrderTo should NOT have value @storyNarrativeInOrderTo", "Then Web element storyNarrativeAsA should NOT have value @storyNarrativeAsA", "Then Web element storyNarrativeIWantTo should NOT have value @storyNarrativeIWantTo", "Then Web element storyGivenStory1 should NOT have value @storyGivenStory1", "Then Web element storyLifecycleBeforeStep1 should NOT have value @storyLifecycleBeforeStep1"})
        public void compositeStep6() { }
    
        @Given("Web user is in the View Story page")
        @Composite(steps = {"Given variable storyName has value testStory", "When File is copied from public/stories/test/dummy.story to public/stories/testDirectory/testStory.story", "Given Web address /page/stories/view/testDirectory/testStory is opened", "When Web user clicks the element expandPanels"})
        public void compositeStep7() { }
    
        @Then("Web story is saved")
        @Composite(steps = {"Given Web address /page/stories/view/testDirectory/@storyName is opened", "Given Web page is refreshed", "When Web user clicks the element scenariosPanel", "Then Web element storyName should have value @storyName", "When Web user clicks the element storyPanel", "When Web user clicks the element storyDescriptionPanel", "Then Web element storyDescription should have value @storyDescription", "When Web user clicks the element storyMetaPanel", "Then Web element storyMeta1 should have value @storyMeta1", "When Web user clicks the element storyNarrativePanel", "Then Web element storyNarrativeInOrderTo should have value @storyNarrativeInOrderTo", "Then Web element storyNarrativeAsA should have value @storyNarrativeAsA", "Then Web element storyNarrativeIWantTo should have value @storyNarrativeIWantTo", "When Web user clicks the element storyGivenStoriesPanel", "Then Web element storyGivenStory1 should have value @storyGivenStory1", "When Web user clicks the element storyLifecyclePanel", "Then Web element storyLifecycleBeforeStep1 should have value @storyLifecycleBeforeStep1", "When Web user clicks the element scenariosPanel"})
        public void compositeStep8() { }
    
        @Given("Web user is in the Browse Stories dialog")
        @Composite(steps = {"Given Web home page is opened", "When Web user clicks the element browseStories"})
        public void compositeStep9() { }
    
        @Then("Web story scenario is saved")
        @Composite(steps = {"Given Web address /page/stories/view/testDirectory/@storyName is opened", "Given Web page is refreshed", "When Web user clicks the element scenario1Panel", "Then Web element scenario1Title should have value @scenario1Title", "Then Web element scenario1Meta1 should have value @scenario1Meta1", "Then Web element scenario1Step1 should have value @scenario1Step1", "Then Web element scenario1Step2 should have value @scenario1Step2"})
        public void compositeStep10() { }
    
        @When("Web user adds scenario to the story form")
        @Composite(steps = {"When Web user clicks the element addScenario", "When Web user clicks the element scenario1Panel", "When Web user sets value Scenario 1 Title to the element scenario1Title", "When Web user clicks the element addScenario1Meta", "When Web user sets value Scenario1Meta1 to the element scenario1Meta1", "When Web user clicks the element addScenario1Step", "When Web user sets value When step is written to the element scenario1Step1", "When Web user clicks the element addScenario1Step", "When Web user sets value Then there is still hope to the element scenario1Step2"})
        public void compositeStep11() { }
    
        @Then("Web modal header should have text $text")
        @Composite(steps = {"Then Web element modalHeader should have text <text>"})
        public void compositeStep12(@Named("text") BddVariable text) { }
    
        @Then("Web story is renamed")
        @Composite(steps = {"Then File public/stories/testDirectory/@storyName.story exists"})
        public void compositeStep13() { }
    
        @When("Web user fills in the new story form")
        @Composite(steps = {"When Web user sets value newStory to the element storyName", "When Web user sets value story description to the element storyDescription", "When Web user clicks the element addStoryMeta", "When Web user sets value story meta 1 to the element storyMeta1", "When Web user sets value accomplish something to the element storyNarrativeInOrderTo", "When Web user sets value user to the element storyNarrativeAsA", "When Web user sets value do something to the element storyNarrativeIWantTo", "When Web user clicks the element addStoryGivenStory", "When Web user sets value path/to/my/given/story to the element storyGivenStory1", "When Web user clicks the element addStoryLifecycleBeforeStep", "When Web user sets value Given this is before step to the element storyLifecycleBeforeStep1", "When Web user clicks the element addStoryLifecycleAfterStep", "When Web user sets value Given this is after step to the element storyLifecycleAfterStep1"})
        public void compositeStep14() { }
    
        @Then("story is running")
        @Composite(steps = {"Then Web element runnerStatus should have text Story run is in progress"})
        public void compositeStep15() { }
    
        @Then("Web user is in the composites modal page")
        @Composite(steps = {"Then Web element modalHeader should have text Browse Composites"})
        public void compositeStep16() { }
    
}