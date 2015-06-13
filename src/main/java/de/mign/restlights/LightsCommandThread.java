package de.mign.restlights;

import java.io.IOException;

public class LightsCommand {

    private StatusSubscriber log;
    private boolean green, yellow, red;

    public LightsCommand(StatusSubscriber sub) {
	this.log = sub;
    }

    public void setGreen(boolean green) {
	this.green = green;
    }

    public void setYellow(boolean yellow) {
	this.yellow = yellow;
    }

    public void setRed(boolean red) {
	this.red = red;
    }

    public void execute() {

	String cmd = "USBswitchCmd.exe ";
	if (green) {
	    cmd += "G";
	}
	try {
	    Runtime.getRuntime().exec(cmd);
	} catch (IOException e) {
	    log.error(e.getMessage());
	}

    }
}
