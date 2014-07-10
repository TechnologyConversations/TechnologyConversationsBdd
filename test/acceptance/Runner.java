package acceptance;

import com.technologyconversations.bdd.steps.WebSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.test.Helpers;
import play.test.TestServer;

import java.util.Arrays;
import java.util.List;

public class Runner extends JUnitStories {
    private final CrossReference xref = new CrossReference();
    private final String storiesLocation = "test/acceptance/stories";
    private final String storyFilter = "**/*.story";
    private static TestServer testServer;

    private Configuration configuration;

    @Override
    public Configuration configuration() {
        if (configuration == null) {
            configuration = new MostUsefulConfiguration()
                    .useStoryLoader(new LoadFromRelativeFile(CodeLocations.codeLocationFromPath(storiesLocation)))
                    .useStoryReporterBuilder(new StoryReporterBuilder()
                            .withFormats(Format.STATS, Format.CONSOLE)
                            //.withFormats(Format.HTML, Format.STATS, Format.CONSOLE) //Usual
                            .withFailureTrace(true)
                            .withFailureTraceCompression(true)
                            .withCrossReference(xref))
                    .useStepMonitor(xref.getStepMonitor());
        }
        return configuration;
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new WebSteps());
    }

    @Override
    protected List<String> storyPaths() {
        String codeLocation = CodeLocations.codeLocationFromPath(storiesLocation).getFile();
        return new StoryFinder().findPaths(codeLocation, Arrays.asList(storyFilter), Arrays.asList(""));
    }

    @BeforeClass
    public static void fakeAppStart() {
        // Starting Fake Server. Stories will run against this environment
        testServer = Helpers.testServer(1234, Helpers.fakeApplication());
        Helpers.start(testServer);
    }

    @AfterClass
    public static void fakeAppStop() {
        // Stopping Fake Server
        Helpers.stop(testServer);
    }
}
