package models.jbehave;

import com.technologyconversations.bdd.steps.CommonSteps;
import com.technologyconversations.bdd.steps.FileSteps;
import com.technologyconversations.bdd.steps.WebSteps;
import models.RunnerClass;
import org.apache.commons.cli.*;

import java.util.*;

import java.io.File;

public class JBehaveRunnerCommandLine {

    final private List<String> defaultStepsClasses = Arrays.asList(
        CommonSteps.class.getName(),
        WebSteps.class.getName(),
        FileSteps.class.getName()
    );

    // TODO Test
    public static void main(String[] args) throws Throwable {
        JBehaveRunnerCommandLine cpRunner = new JBehaveRunnerCommandLine();
        if (!cpRunner.isHelp(args)) {
            JBehaveRunner runner = cpRunner.getRunner(args);
            try {
                runner.run();
                runner.cleanUp();
            } catch (Throwable e) {
                runner.cleanUp();
                throw e;
            }
        }
    }

    protected boolean isHelp(String[] args) throws ParseException {
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(getOptions(), args);
        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JBehaveRunnerCommandLine", getOptions());
            return true;
        } else {
            return false;
        }
    }

    protected Options getOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Prints this message");
        options.addOption("s", "story_path", true, "Specifies path to the story. Ant format can be used in each path. If not specified, default path public/stories/**/*.story is used. Multiple values are allowed.");
        options.addOption("c", "steps_class", true, "Specifies steps class. If not specified, default classes are used. Multiple values are allowed.");
        Option params = OptionBuilder.withArgName("param=value")
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("Specifies steps parameters as key/value pairs. Comma separated list of steps parameters. Multiple values are allowed.")
                .create("P");
        options.addOption(params);
        options.addOption("o", "composites_path", true, "Specifies path to the composite. Comma separated list of composite classes paths. Ant format can be used in each path. Multiple values are allowed.");
        options.addOption("r", "reports_path", true, "Path to the reports directory. Ant format can be used. If not specified, default path public/jbehave is used.");
        return options;
    }

    public JBehaveRunner getRunner(String[] args) throws Exception {
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(getOptions(), args);
        return new JBehaveRunner(
            getStoryPaths(cmd.getOptionValues("story_path")),
            getStepClasses(cmd.getOptionValues("steps_class"), convertPropertiesToMap(cmd.getOptionProperties("P"))),
            getCompositesPaths(cmd.getOptionValues("composites_path")),
            getUniqueReportsPath(cmd.getOptionValue("reports_path"))
        );
    }

    private Map<String, String> convertPropertiesToMap(Properties properties) {
        Map<String, String> map = new HashMap<>();
        if (properties != null) {
            for (String name : properties.stringPropertyNames()) {
                map.put(name, properties.getProperty(name));
            }
        }
        return map;
    }

    public List<String> getStoryPaths(String[] paths) {
        if (paths != null && paths.length > 0) {
            return Arrays.asList(paths);
        } else {
            return Arrays.asList("public/stories/**/*.story");
        }
    }

    public List<RunnerClass> getStepClasses(String[] classes, Map<String, String> params) {
        List<String> classNames;
        if (classes != null && classes.length > 0) {
            classNames = Arrays.asList(classes);
        } else {
            classNames = defaultStepsClasses;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        List<RunnerClass> list = new ArrayList<>();
        for (String className : classNames) {
            list.add(RunnerClass.runnerClassFromJava(className, params));
        }
        return list;
    }

    public List<String> getCompositesPaths(String[] paths) {
        if (paths != null && paths.length > 0) {
            return Arrays.asList(paths);
        } else {
            return Collections.emptyList();
        }
    }

    protected String getUniqueReportsPath(String reportsPath) {
        String uniquePath = File.separator + System.currentTimeMillis();
        if (reportsPath == null || reportsPath.isEmpty()) {
            return "public/jbehave" + uniquePath;
        } else {
            return reportsPath + uniquePath;
        }
    }

}
