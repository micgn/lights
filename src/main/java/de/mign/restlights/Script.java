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
    private RestJsonClient rest;
    private LightsCommandThread command;
    private final ScriptEngine engine;

    public Script() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("JavaScript");
    }

    public void setLog(StatusSubscriber l) {
        log = l;
    }

    public void setRestJsonClient(RestJsonClient c) {
        rest = c;
    }

    public void setCommand(LightsCommandThread c) {
        command = c;
    }

    public void execute() {

        engine.put("command", command);
        engine.put("rest", rest);
        engine.put("logWindow", log);
        try {
            engine.eval(loadScript());
            log.say("script run");

        } catch (ScriptException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error("file loading problem: " + e.getMessage());
        }
    }

    private String loadScript() throws IOException {
        String fileName = Main.getScriptFileParam();
        try {
            Path path = FileSystems.getDefault().getPath(fileName);
            List<String> lines = Files.readAllLines(path);
            String s = "";
            for (String line : lines) {
                s += line + "\n";
            }
            return s;
        } finally {

        }
    }
}
