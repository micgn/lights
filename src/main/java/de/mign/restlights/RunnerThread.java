/* 
 * Copyright 2015 Michael Gnatz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @Override
    public void start() {
        shouldRun = true;
        (new Thread(this)).start();
        commandThread.start();
        log.say("started");
    }

    @Override
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
