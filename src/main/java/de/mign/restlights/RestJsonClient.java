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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestJsonClient {

    private StatusSubscriber log;

    public void setLog(StatusSubscriber l) {
        log = l;
    }

    public String get(String urlStr) {

        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                log.error("HTTP error code : " + conn.getResponseCode());
                return null;
            }

            String out = read(conn.getInputStream());
            return out;

        } catch (IOException e) {
            log.error(e.getMessage());

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    private static String read(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String output = "";
        String line;
        while ((line = br.readLine()) != null) {
            output += line + "\n";
        }
        return output;
    }
}
