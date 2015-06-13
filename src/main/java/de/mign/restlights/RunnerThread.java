package de.mign.restlights;

import de.mign.restlights.dialog.Startable;

public class RunnerThread implements Runnable, Startable {

    private static final int POLLING_MILLIS = 10 * 1000;

    private StatusSubscriber log;
    private Script script;
    private LightsCommandThread commandThread;

    private boolean shouldRun = false;

    public void setCommand(LightsCommandThread c) {
        commandThread = c;
    }

    public void setScript(Script s) {
        script = s;
    }

    public void setLog(StatusSubscriber l) {
        log = l;
    }

    public void start() {
        shouldRun = true;
        (new Thread(this)).start();
        commandThread.start();
        log.say("started");
    }

    public void stop() {
        log.say("stopping...");
        shouldRun = false;
        commandThread.stop();
    }

    @Override
    public void run() {

        long lastPollTime = 0;
        while (shouldRun) {

            if (lastPollTime == 0
                    || System.currentTimeMillis() - lastPollTime >= POLLING_MILLIS) {

                script.execute();
                lastPollTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        log.say("stopped");
    }
}
