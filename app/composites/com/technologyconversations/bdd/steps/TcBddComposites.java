package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;
import com.technologyconversations.bdd.steps.util.BddVariable;

public class TcBddComposites {
    
        @Given("Web user is in the View Story page")
        @Composite(steps = {"Given Web address /page/stories/view/myStory is opened"})
        public void compositeStep0() { }
    
        @Given("Web user is in the Browse Stories dialog")
        @Composite(steps = {"Given Web home page is opened", "When Web user clicks the element browseStories"})
        public void compositeStep1() { }
    
        @Then("Web modal header should have text $text")
        @Composite(steps = {"Then Web element modalHeader should have text <text>"})
        public void compositeStep2(@Named("text") BddVariable text) { }
    
        @Given("Web user is in the New Story page")
        @Composite(steps = {"Given Web address /page/stories/new is opened"})
        public void compositeStep3() { }
    
}