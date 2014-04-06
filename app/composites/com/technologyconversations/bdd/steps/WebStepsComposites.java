package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;
import com.technologyconversations.bdd.steps.util.BddVariable;

public class WebStepsComposites {
    
        @Given("this is \"my\" <param1>, <param2> and <param3>")
        @Composite(steps = {"Given \"my\" <param11>", "When <param2>", "Then <param3>"})
        public void compositeStep0(@Named("param1") BddVariable param1, @Named("param2") BddVariable param2, @Named("param3") BddVariable param3, @Named("param11") BddVariable param11) { }
    
        @Given("this is my composite")
        @Composite(steps = {"Given something", "When else", "Then OK"})
        public void compositeStep1() { }
    
}