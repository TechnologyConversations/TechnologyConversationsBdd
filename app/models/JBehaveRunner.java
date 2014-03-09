package models;

import com.technologyconversations.bdd.steps.WebSteps;
//import org.jbehave.core.configuration.Configuration;
//import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
//import org.jbehave.core.reporters.Format;
//import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.List;

public class JBehaveRunner extends JUnitStories {

//    @Override
//    public Configuration configuration() {
//        return new MostUsefulConfiguration()
//                .useStoryReporterBuilder(new StoryReporterBuilder().withFormats(Format.ANSI_CONSOLE));
//    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
                .findPaths(CodeLocations.codeLocationFromPath(""), "stories/**/*.story", "");
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new WebSteps());
    }
}
