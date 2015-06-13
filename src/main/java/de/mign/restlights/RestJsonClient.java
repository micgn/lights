package de.mign.restlights;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestJsonClient {

    private StatusSubscriber log;

    public RestJsonClient(StatusSubscriber subs) {
	this.log = subs;
    }

    public String get(String urlStr) {

	URL url = null;
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
