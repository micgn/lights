package de.mign.restlights;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Script {

    private StatusSubscriber log;
    private ScriptEngine engine;

    public Script(StatusSubscriber subs) {
	this.log = subs;
	ScriptEngineManager factory = new ScriptEngineManager();
	engine = factory.getEngineByName("JavaScript");
    }

    public void execute() {
	LightsCommand command = new LightsCommand(log);
	engine.put("command", command);
	RestJsonClient rest = new RestJsonClient(log);
	engine.put("rest", rest);
	engine.put("logWindow", log);
	try {
	    engine.eval(loadScript());
	    log.say("script run");

	    command.execute();
	    log.say("lights triggered");

	} catch (ScriptException e) {
	    log.error(e.getMessage());
	} catch (IOException e) {
	    log.error("file " + Main.getScriptFileParam() + " not found!");
	}
    }

    private String loadScript() throws IOException {
	String fileName = Main.getScriptFileParam();
	try {
	    Path path = FileSystems.getDefault().getPath(fileName);
	    List<String> lines = Files.readAllLines(path);
	    String s = "";
	    for (String line : lines)
		s += line + "\n";
	    return s;
	} finally {

	}
    }
}
