package de.mign.restlights;

import de.mign.restlights.dialog.Startable;

public class Runner implements Runnable, Startable {

    private static final int POLLING_MILLIS = 10 * 1000;

    private StatusSubscriber log;
    private Script script;
    
    private boolean shouldRun = false;

    public Runner(StatusSubscriber subs) {
	this.log = subs;
	this.script = new Script(subs);
    }

    public void start() {
	shouldRun = true;
	(new Thread(this)).start();
	log.say("started");
    }

    public void stop() {
	log.say("stopping...");
	shouldRun = false;
    }

    @Override
    public void run() {

	long lastPollTime = 0;
	while (shouldRun) {

	    if (lastPollTime == 0
		    || System.currentTimeMillis() - lastPollTime >= POLLING_MILLIS) {

		poll();
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

    private void poll() {
	script.execute();
    }
}
