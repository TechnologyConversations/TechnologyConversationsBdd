package models;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

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
        stepsInstances = new ArrayList<Object>();
        for (String className : value) {
            stepsInstances.add(Class.forName(className).newInstance());
        }
    }
    public List<Object> getStepsInstances() {
        return stepsInstances;
    }

    public JBehaveRunner(String storyPathValue, List<String> stepsInstancesNames) throws Exception {
        setStoryPath(storyPathValue);
        setStepsInstancesFromNames(stepsInstancesNames);
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
                .findPaths(CodeLocations.codeLocationFromPath(""), getStoryPath(), "");
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), stepsInstances);
    }
}
