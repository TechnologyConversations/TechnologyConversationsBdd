package models.jbehave;

import com.technologyconversations.bdd.steps.util.BddParamsBean;
import groovy.lang.GroovyClassLoader;
import models.RunnerClass;
import org.apache.commons.io.FileUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.LoadFromURL;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.SilentStepMonitor;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.io.File.separator;

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
                        .withPathResolver(new FilePrintStreamFactory.ResolveToSimpleName())
                        .withRelativeDirectory(getReportsPath())
                        .withDefaultFormats()
                        .withFormats(Format.CONSOLE, Format.HTML, Format.XML, Format.TXT)
                        .withCrossReference(new CrossReference()))
                .useStepMonitor(new SilentStepMonitor())
                .useStoryLoader(new LoadFromURL())
//                .useStoryLoader(new LoadFromRelativeFile())
//                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true));
        EmbedderControls embedderControls = configuredEmbedder().embedderControls();
        embedderControls.useStoryTimeoutInSecs(storyTimeoutMinutes * secondsInMinute);
    }

    @Override
    protected List<String> storyPaths() {
        String searchIn = CodeLocations.codeLocationFromPath("").getFile().replace("target/universal/stage/", "../../../");
        return new StoryFinder().findPaths(searchIn, getStoryPaths(), new ArrayList<String>(), "file:");
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
    public final void cleanUp() {
        File sourceDir = this.getSourceDir(this.getReportsPath());
        File destinationDir = this.getDestinationDir(this.getReportsPath());
        if (sourceDir.exists() && !destinationDir.exists()) {
            this.copyDirectory(sourceDir, destinationDir);
            this.deleteDirectory(sourceDir);
        }
    }

    protected File getSourceDir(String path) {
        File reportsDir = new File("target" + separator + path);
        String sourcePath = reportsDir.getAbsolutePath();
        return new File(sourcePath);
    }

    protected File getDestinationDir(String path) {
        File reportsDir = new File(path);
        String stagePath = "target" + separator + "universal" + separator + "stage" + separator;
        String destinationPath = reportsDir.getAbsolutePath().replace(stagePath, "");
        return new File(destinationPath);
    }

    // TODO Test
    protected void copyDirectory(File sourceDir, File destinationDir) {
        try {
            FileUtils.copyDirectory(sourceDir, destinationDir);
        } catch(IOException e) {
            Logger.error("Could not copy the JBehave directory.", e);
        }
    }

    // TODO Test
    protected void deleteDirectory(File sourceDir) {
        try {
            FileUtils.deleteDirectory(sourceDir);
        } catch(IOException e) {
            Logger.error("Could not delete the JBehave directory.", e);
        }
    }

}
