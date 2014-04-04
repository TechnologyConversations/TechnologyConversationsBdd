package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;

public class WebStepsComposites {
    
        @Given("this is \"my\" <param1>, <param2> and <param3>")
        @Composite(steps = {"Given \"my\" <param1>", "When <param2>", "Then <param3>"})
        public void compositeStep0(@Named("param1") String param1, @Named("param2") String param2, @Named("param3") String param3) { }
    
        @Given("this is my composite")
        @Composite(steps = {"Given something", "When else", "Then OK"})
        public void compositeStep1() { }
    
}