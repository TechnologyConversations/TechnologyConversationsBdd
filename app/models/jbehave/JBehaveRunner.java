package models.jbehave;

import com.technologyconversations.bdd.steps.util.BddParamsBean;
import groovy.lang.GroovyClassLoader;
import models.RunnerClass;
import org.apache.commons.io.FileUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.EmbedderControls;
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

import java.io.IOException;
import java.lang.reflect.Method;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JBehaveRunner extends JUnitStories {

    private Configuration configuration;

    private List<String> storyPaths;
    public final void setStoryPaths(List<String> value) {
        storyPaths = value;
    }
    public final List<String> getStoryPaths() {
        return storyPaths;
    }

    private List<String> compositePaths;
    public final void setCompositePaths(List<String> value) {
        compositePaths = value;
    }
    public final List<String> getCompositePaths() {
        return compositePaths;
    }

    private List<Object> stepsInstances;
    public final void setStepsInstances(List<RunnerClass> value) throws Exception {
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
                         List<String> compositePaths,
                         String reportsPathValue) throws Exception {
        final int secondsInMinute = 60;
        final int storyTimeoutMinutes = 20;
        setStoryPaths(storyPathsValue);
        setStepsInstances(stepsClasses);
        setCompositePaths(compositePaths);
        setReportsPath(reportsPathValue);

        configuration = new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withRelativeDirectory(getReportsPath())
                        .withDefaultFormats()
                        .withFormats(Format.CONSOLE, Format.HTML, Format.XML, Format.TXT)
                        .withCrossReference(new CrossReference()))
                .useStepMonitor(new SilentStepMonitor())
                .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true));
        EmbedderControls embedderControls = configuredEmbedder().embedderControls();
        embedderControls.useStoryTimeoutInSecs(storyTimeoutMinutes * secondsInMinute);
    }

    @Override
    protected List<String> storyPaths() {
        String searchIn = CodeLocations.codeLocationFromPath("").getFile();
        System.out.println("searchIn: " + searchIn + "!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("getStoryPaths: " + getStoryPaths() + "!!!!!!!!!!!!!!!!");
        return new StoryFinder().findPaths(searchIn, getStoryPaths(), new ArrayList<String>());
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        List<Object> instances = new ArrayList<>();
        instances.addAll(getStepsInstances());
        for(String path : getCompositePaths()) {
            File groovyStepsFile = new File(path);
            try {
                Object instance = new GroovyClassLoader().parseClass(groovyStepsFile).newInstance();
                instances.add(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new InstanceStepsFactory(configuration(), instances);
    }

    @Override
    public Configuration configuration() {
        return configuration;
    }

    // TODO Test
    public final void cleanUp() throws IOException {
        File reportsDir = new File("target/" + this.getReportsPath());
        String sourcePath = reportsDir.getAbsolutePath();
        String destinationPath = sourcePath.replace("target/universal/stage/", "").replace("target/", "");
        File sourceDir = new File(sourcePath);
        File destinationDir = new File(destinationPath);
        if (sourceDir.exists() && !destinationDir.exists()) {
            FileUtils.moveDirectory(sourceDir, destinationDir);
        }
    }


}
