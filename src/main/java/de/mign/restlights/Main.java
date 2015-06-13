package de.mign.restlights;

import de.mign.restlights.dialog.LightsDialog;

public class Main {

    private static String scriptFileParam;

    public static void main(String args[]) {

	if (args.length != 1 || args[0].length() == 0) {
	    System.err.println("Usage: java -jar ... [path to script]");
	    System.exit(-1);
	}

	scriptFileParam = args[0];

	LightsDialog dialog = new LightsDialog();
	dialog.pack();
	dialog.setVisible(true);

	Runner runner = new Runner(dialog);
	dialog.setStartable(runner);
    }

    public static String getScriptFileParam() {
	return scriptFileParam;
    }
}
