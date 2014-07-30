package models.jbehave;

import com.technologyconversations.bdd.steps.FileSteps;
import com.technologyconversations.bdd.steps.WebSteps;
import models.RunnerClass;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.File;

public class JBehaveRunnerCommandLine {

    // TODO Test
    public static void main(String[] args) throws Throwable {
        JBehaveRunnerCommandLine cpRunner = new JBehaveRunnerCommandLine();
        cpRunner.verifyArguments(args);
        JBehaveRunner runner = cpRunner.getRunner(args);
//        try {
//            runner.run();
//        } catch (Throwable e) {
//            throw e;
//        } finally {
//            runner.cleanUp();
//        }
    }

    public JBehaveRunner getRunner(String args[]) throws Exception {
        return new JBehaveRunner(
            getStoryPaths(args[0]),
            getStepClasses(args[1], args[2]),
            getCompositesPaths(args[3]),
            getUniqueReportsPath(args[4])
        );
    }

    public void verifyArguments(String[] args) {
        if (args.length < 5) {
            StringBuilder message = new StringBuilder();
            message.append("JBehaveRunner [STORY_PATHS] [STEPS_CLASSES] [STEPS_PARAMS] [COMPOSITES_PATH] [REPORTS_PATH]");
            message.append("\nSTORY_PATHS: Comma separated list of story paths. Ant format can be used in each path. If empty, default path is used.");
            message.append("\nSTEPS_CLASSES: Comma separated list of steps classes. If empty, default classes are used.");
            message.append("\nSTEPS_PARAMS: Comma separated list of steps classes. If empty, default classes are used.");
            message.append("\nCOMPOSITES_PATH: Comma separated list of composite classes paths. Ant format can be used in each path. If empty, default paths are used.");
            message.append("\nREPORTS_PATH: Path to the reports directory. Ant format can be used. If empty, default path is used.");
            throw new IllegalArgumentException(message.toString());
        }
    }

    public List<String> argToList(String arg, String defaultValue) {
        if (arg == null || arg.isEmpty()) {
            return Arrays.asList(defaultValue.split(","));
        } else {
            return Arrays.asList(arg.split(","));
        }
    }

    public Map<String, String> argToMap(String arg) {
        Map<String, String> map = new HashMap<>();
        if (arg != null && !arg.isEmpty()) {
            List<String> list = Arrays.asList(arg.split(","));
            for (String param : list) {
                String[] keyValue = param.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    public List<String> getStoryPaths(String arg) {
        return argToList(arg, "public/stories/**/*.story");
    }

    public List<RunnerClass> getStepClasses(String classes, String params) {
        String defaultValue = WebSteps.class.getName() + "," + FileSteps.class.getName();
        List<String> classNames = argToList(classes, defaultValue);
        Map<String, String> paramsMap = argToMap(params);
        List<RunnerClass> list = new ArrayList<>();
        for (String className : classNames) {
            list.add(RunnerClass.runnerClassFromJava(className, paramsMap));
        }
        return list;
    }

    public List<String> getCompositesPaths(String arg) {
        return argToList(arg, "composites/**/*.groovy");
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
