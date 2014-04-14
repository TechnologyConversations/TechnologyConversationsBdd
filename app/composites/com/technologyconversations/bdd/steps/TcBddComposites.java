package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;
import com.technologyconversations.bdd.steps.util.BddVariable;

public class TcBddComposites {
    
        @Then("Web modal header should have text $text")
        @Composite(steps = {"Then Web element modalHeader should have text <text>"})
        public void compositeStep0(@Named("text") BddVariable text) { }
    
        @Given("Web user is in the View Story page")
        @Composite(steps = {"When File is copied from public/stories/tcbdd/storiesList.story to public/stories/testDirectory/testStory.story", "Given Web address /page/stories/view/testDirectory/testStory is opened"})
        public void compositeStep1() { }
    
        @Given("Web user is in the New Story page")
        @Composite(steps = {"Given Web address /page/stories/new is opened"})
        public void compositeStep2() { }
    
        @Given("Web user is in the Browse Stories dialog")
        @Composite(steps = {"Given Web home page is opened", "When Web user clicks the element browseStories"})
        public void compositeStep3() { }
    
}