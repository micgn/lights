package de.mign.restlights;

import java.io.IOException;

public class LightsCommandThread implements Runnable {

    private StatusSubscriber log;

    private boolean threadRunning = false;
    private static final long sleepMillis = 1000;

    public final Light green = new Light();
    public final Light yellow = new Light();
    public final Light red = new Light();

    public void setLog(StatusSubscriber log) {
        this.log = log;
    }

    public class Light {

        private boolean on;
        private long millisOn, millisOff;

        private long lastChange;
        private boolean shouldBeOn;

        Light() {
            reset();
        }

        final void reset() {
            on = false;
            millisOn = Long.MAX_VALUE;
            millisOff = Long.MAX_VALUE;
            lastChange = -1;
            shouldBeOn = false;
        }

        public synchronized void set(boolean on) {
            if (this.on != on || millisOn != Long.MAX_VALUE || millisOff != Long.MAX_VALUE) {
                this.on = on;
                millisOn = Long.MAX_VALUE;
                millisOff = Long.MAX_VALUE;
                lastChange = -1;
            }
        }

        public synchronized void set(boolean on, long millisOn, long millisOff) {
            if (this.on != on || this.millisOn != millisOn || this.millisOff != millisOff) {
                this.on = on;
                this.millisOn = millisOn;
                this.millisOff = millisOff;
                lastChange = -1;
            }
        }

        synchronized boolean switchNow() {
            long now = System.currentTimeMillis();

            if (lastChange == -1) {
                shouldBeOn = on;
                lastChange = now;
                return true;
            }

            if (shouldBeOn && now - lastChange > millisOn) {
                shouldBeOn = false;
                lastChange = now;
                return true;
            }
            if (!shouldBeOn && now - lastChange > millisOff) {
                shouldBeOn = true;
                lastChange = now;
                return true;
            }
            return false;
        }

        boolean shouldBeOn() {
            return shouldBeOn;
        }

    }

    public void start() {
        threadRunning = true;
        (new Thread(this)).start();
    }

    public void stop() {
        threadRunning = false;
        red.reset();
        yellow.reset();
        green.reset();
        execute();
    }

    @Override
    public void run() {
        while (threadRunning) {
            execute();
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException ex) {
                ;
            }
        }
    }
    
    private void execute() {
        if (green.switchNow() || yellow.switchNow() || red.switchNow()) {
                String cmd = "";
                if (green.shouldBeOn()) {
                    cmd += "G ";
                }
                if (yellow.shouldBeOn()) {
                    cmd += "Y ";
                }
                if (red.shouldBeOn()) {
                    cmd += "R ";
                }
                if (cmd.length() == 0) {
                    cmd = "O";
                }
                log.say("cmd=" + cmd);

                cmd = "USBswitchCmd.exe " + cmd + " 1";

                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
    }
}
