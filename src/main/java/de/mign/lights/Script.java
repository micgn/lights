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
package de.mign.lights;

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
            s = lines.stream().map((line) -> line + "\n").reduce(s, String::concat);
            return s;
        } finally {

        }
    }
}
