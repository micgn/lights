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

import de.mign.restlights.dialog.LightsDialog;

public class Main {

    private static String scriptFileParam;

    public static void main(String args[]) {

        if (args.length != 1 || args[0].length() == 0) {
            System.err.println("Usage: java -jar ... [path to script]");
            System.exit(-1);
        }

        scriptFileParam = args[0];

        // wiring
        LightsDialog dialog = new LightsDialog();
        RunnerThread runner = new RunnerThread();
        Script script = new Script();
        LightsCommandThread commandThread = new LightsCommandThread();
        RestJsonClient rest = new RestJsonClient();

        runner.setLog(dialog);
        runner.setCommand(commandThread);
        runner.setScript(script);

        script.setLog(dialog);
        script.setCommand(commandThread);
        script.setRestJsonClient(rest);

        commandThread.setLog(dialog);

        rest.setLog(dialog);

        dialog.setStartable(runner);

        dialog.pack();
        dialog.setVisible(true);
    }

    public static String getScriptFileParam() {
        return scriptFileParam;
    }
}
