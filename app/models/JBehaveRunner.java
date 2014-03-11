package models;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.SilentStepMonitor;

import java.util.ArrayList;
import java.util.List;

public class JBehaveRunner extends JUnitStories {

    private String storyPath;
    public void setStoryPath(String value) {
        storyPath = value;
    }
    public String getStoryPath() {
        return storyPath;
    }

    private List<Object> stepsInstances;
    public void setStepsInstancesFromNames(List<String> value) throws Exception {
        stepsInstances = new ArrayList<>();
        for (String className : value) {
            stepsInstances.add(Class.forName(className).newInstance());
        }
    }
    public List<Object> getStepsInstances() {
        return stepsInstances;
    }

    private String reportsPath;
    public void setReportsPath(String value) {
        reportsPath = value;
    }
    public String getReportsPath() {
        return reportsPath;
    }

    public JBehaveRunner(String storyPathValue,
                         List<String> stepsInstancesNames,
                         String reportsPathValue) throws Exception {
        setStoryPath(storyPathValue);
        setStepsInstancesFromNames(stepsInstancesNames);
        setReportsPath(reportsPathValue);
    }


    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
                .findPaths(CodeLocations.codeLocationFromPath("").getFile(), getStoryPath(), "");
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), getStepsInstances());
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withRelativeDirectory(getReportsPath())
                        .withDefaultFormats()
                        .withFormats(Format.CONSOLE, Format.HTML, Format.XML)
                        .withCrossReference(new CrossReference()))
                .useStepMonitor(new SilentStepMonitor());
    }

}
