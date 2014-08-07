package models.jbehave;

import play.test.TestServer;
import play.test.Helpers;

public class JBehaveRunnerAssistant extends JBehaveRunnerCommandLine {

    private TestServer server;

    public static void main(String[] args) throws Throwable {
        JBehaveRunnerAssistant cpRunner = new JBehaveRunnerAssistant();
        if (!cpRunner.isHelp(args)) {
            JBehaveRunner runner = cpRunner.getRunner(args);
            try {
                cpRunner.startFakeApp();
                runner.run();
                cpRunner.stopFakeApp();
                runner.cleanUp();
                System.exit(0);
            } catch (Throwable e) {
                cpRunner.stopFakeApp();
                runner.cleanUp();
                System.exit(1);
            }
        }
    }

    public void startFakeApp() {
        server = Helpers.testServer(1234, Helpers.fakeApplication());
        Helpers.start(server);
    }

    public void stopFakeApp() {
        Helpers.stop(server);
    }

}
