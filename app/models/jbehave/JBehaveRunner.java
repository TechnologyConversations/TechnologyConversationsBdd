package models.jbehave;

import com.technologyconversations.bdd.steps.util.BddParamsBean;
import models.RunnerClass;
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
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.SilentStepMonitor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JBehaveRunner extends JUnitStories {

    private List<String> storyPaths;
    public void setStoryPaths(List<String> value) {
        storyPaths = value;
    }
    public List<String> getStoryPaths() {
        return storyPaths;
    }

    private List<Object> stepsInstances;
    public void setStepsInstances(List<RunnerClass> value) throws Exception {
        stepsInstances = new ArrayList<>();
        for (RunnerClass runnerClass : value) {
            Object stepsInstance = Class.forName(runnerClass.fullName()).newInstance();
            for (Method method : stepsInstance.getClass().getMethods()) {
                if (method.getAnnotation(BddParamsBean.class) != null) {
                    Map<String, String> params = runnerClass.javaParams();
                    method.invoke(stepsInstance, params);
                    break;
                }
            }
            stepsInstances.add(stepsInstance);
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

    public JBehaveRunner(List<String> storyPathsValue,
                         List<RunnerClass> stepsClasses,
                         String reportsPathValue) throws Exception {
        setStoryPaths(storyPathsValue);
        setStepsInstances(stepsClasses);
        setReportsPath(reportsPathValue);
    }

    @Override
    protected List<String> storyPaths() {
        String searchIn = CodeLocations.codeLocationFromPath("").getFile();
        return new StoryFinder().findPaths(searchIn, getStoryPaths(), new ArrayList<String>());
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
                        .withFormats(Format.CONSOLE, Format.HTML, Format.XML, Format.TXT)
                        .withCrossReference(new CrossReference()))
                .useStepMonitor(new SilentStepMonitor())
                .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true));
    }


}
