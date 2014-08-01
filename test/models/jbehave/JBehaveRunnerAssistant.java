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
                runner.cleanUp();
                cpRunner.stopFakeApp();
            } catch (Throwable e) {
                runner.cleanUp();
                cpRunner.stopFakeApp();
                throw e;
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
