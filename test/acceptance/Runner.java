package acceptance;

import com.technologyconversations.bdd.steps.CommonSteps;
import com.technologyconversations.bdd.steps.FileSteps;
import com.technologyconversations.bdd.steps.WebSteps;
import groovy.lang.GroovyClassLoader;
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

import java.io.File;
import java.util.*;


// TODO extend existing RunnerClass
public class Runner extends JUnitStories {
    private final CrossReference xref = new CrossReference();
    private final String storiesLocation = "public/stories/tcbdd";
    private final String storyFilter = "**/*.story";
    private static TestServer testServer;

    private Configuration configuration;

    @Override
    public Configuration configuration() {
        if (configuration == null) {
            configuration = new MostUsefulConfiguration()
                    .useStoryLoader(new LoadFromRelativeFile(CodeLocations.codeLocationFromPath(storiesLocation)))
                    .useStoryReporterBuilder(new StoryReporterBuilder()
                            .withFormats(Format.STATS, Format.HTML)
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
        List<Object> stepClasses = new ArrayList<Object>();

        // Steps
        stepClasses.add(new CommonSteps());

        stepClasses.add(new FileSteps());

        Map<String, String> params = new HashMap<String, String>();
        params.put("url", "http://localhost:1234");
        params.put("browser", "phantomjs");
        WebSteps webSteps = new WebSteps();
        webSteps.setParams(params);
        webSteps.setWebDriver();
        stepClasses.add(webSteps);

        // Composites
        File groovyStepsFile = new File("composites/TcBDDComposites.groovy");
        try {
            Object instance = new GroovyClassLoader().parseClass(groovyStepsFile).newInstance();
            stepClasses.add(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new InstanceStepsFactory(configuration(), stepClasses);
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
