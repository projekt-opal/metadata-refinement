package org.dice_research.opal.metadata_extraction.fox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

/**
 * Based on FOX Java API
 * 
 * @see https://github.com/dice-group/fox-java/blob/master/src/main/java/org/aksw/fox/binding/FoxApi.java
 *
 * @author Adrian Wilke
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 */
public class Fox {

	public static final String LANG_DE = "de";
	public static final String LANG_EN = "en";
	public static final String LANG_FR = "fr";
	public static final String LANG_ES = "es";
	public static final String LANG_NL = "nl";

	private String endpoint = null;

	/**
	 * Sets FOX endpoint, e.g. http://localhost:4444/fox"
	 */
	public Fox setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public String send(String text, String language) throws MalformedURLException, IOException {

		if (endpoint == null) {
			throw new NullPointerException("FOX endpoint not set!");
		}

		final String body = new JSONObject()//
				.put("input", text)

				.put("lang", language)

				.put("type", "text")

				.put("task", "NER")

				.put("output", "TURTLE")

				.toString();

		final StringBuilder data = new StringBuilder();
		HttpURLConnection connection = null;

		connection = (HttpURLConnection) new URL(endpoint).openConnection();
		if (connection != null) {
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
			connection.setRequestProperty("Content-Type",
					"application/json; charset=".concat(StandardCharsets.UTF_8.name().toLowerCase()));
			connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

			OutputStreamWriter writer = null;
			writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(body);
			writer.flush();

			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			for (String line; (line = reader.readLine()) != null;) {
				data.append(line);
				data.append(System.lineSeparator());
			}

			writer.close();
			reader.close();
		}

		return data.toString();
	}
}