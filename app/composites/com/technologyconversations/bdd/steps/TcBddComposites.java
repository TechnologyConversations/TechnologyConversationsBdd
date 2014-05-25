package composites.com.technologyconversations.bdd.steps;

import org.jbehave.core.annotations.*;

public class TcBddComposites {
    
        @Given("this is something")
        @Composite(steps = {"When I do something", "Then the result is something else"})
        public void compositeStep0() { }

}