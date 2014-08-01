package models.jbehave;

import play.test.TestServer;
import play.test.Helpers;

public class JBehaveRunnerAssistant extends JBehaveRunnerCommandLine {

    private TestServer server;

    public static void main(String[] args) throws Throwable {
        JBehaveRunnerAssistant cpRunner = new JBehaveRunnerAssistant();
        System.out.println("111");
        if (!cpRunner.isHelp(args)) {
            System.out.println("222");
            JBehaveRunner runner = cpRunner.getRunner(args);
            System.out.println("333");
            try {
                cpRunner.startFakeApp();
                System.out.println("444");
                runner.run();
                System.out.println("444");
                cpRunner.stopFakeApp();
                System.out.println("555");
                runner.cleanUp();
                System.out.println("666");
                System.exit(0);
            } catch (Throwable e) {
                System.out.println("777");
                cpRunner.stopFakeApp();
                System.out.println("888");
                runner.cleanUp();
                System.exit(1);
            }
        }
        System.out.println("000");
    }

    public void startFakeApp() {
        server = Helpers.testServer(1234, Helpers.fakeApplication());
        Helpers.start(server);
    }

    public void stopFakeApp() {
        Helpers.stop(server);
    }

}
