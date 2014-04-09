package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;
import com.technologyconversations.bdd.steps.util.BddVariable;

public class TcBddComposites {
    
        @Given("Web user is in the Browse Stories dialog")
        @Composite(steps = {"Given Web home page is opened", "When Web user clicks the element browseStories"})
        public void compositeStep0() { }
    
        @When("Web user clicks the directory $directory")
        @Composite(steps = {"When Web user clicks the element <directory>"})
        public void compositeStep1(@Named("directory") BddVariable directory) { }
    
        @Then("Web story $story exists")
        @Composite(steps = {"Then Web element <story> exists"})
        public void compositeStep2(@Named("story") BddVariable story) { }
    
        @Then("Web directory $text should appear in the $selector")
        @Composite(steps = {"Then Web element <selector> should have text <text>"})
        public void compositeStep3(@Named("selector") BddVariable selector, @Named("text") BddVariable text) { }
    
        @Then("Web modal header should have text $text")
        @Composite(steps = {"Then Web element modalHeader should have text <text>"})
        public void compositeStep4(@Named("text") BddVariable text) { }
    
}