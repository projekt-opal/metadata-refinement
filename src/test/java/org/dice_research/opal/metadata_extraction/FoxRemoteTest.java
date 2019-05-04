package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.dice_research.opal.metadata_extraction.fox.FoxRemote;
import org.junit.Test;

public class FoxRemoteTest {

	private static final String FOX_API_ENDPOINT = "http://fox.cs.upb.de:4444/fox";

	private static final boolean PRINT_RESULT = false;
	private static final boolean PRINT_TIME = true;

	private static final String EINSTEIN_TEXT = "A. Einstein was born in Ulm.";
	private static final String EINSTEIN_LANG = FoxRemote.LANG_EN;

	private static final String BERLIN_FILE = "src/test/resources/title-description-berlin.txt";
	private static final String BERLIN_LANG = FoxRemote.LANG_DE;

	@Test
	public void testEinstein() throws MalformedURLException, IOException {
		String result = run(EINSTEIN_TEXT, EINSTEIN_LANG);
		assertTrue(result.contains("Albert_Einstein"));
	}

	@Test
	public void testBerlin() throws MalformedURLException, IOException {
		String text = FileUtils.readFileToString(new File(BERLIN_FILE), "UTF-8");
		String result = run(text, BERLIN_LANG);
		assertTrue(result.contains("Berlin"));
		assertTrue(result.contains("Paderborn"));
		assertTrue(result.contains("NRW"));
	}

	private String run(String text, String language) throws IOException {

		long time = System.currentTimeMillis();

		FoxRemote foxRemote = new FoxRemote().setEndpoint(FOX_API_ENDPOINT);
		String result = foxRemote.getTurtle(text, language);

		if (PRINT_RESULT) {
			System.out.println(result);
		}

		if (PRINT_TIME) {
			System.out.println("FOX remote execution: " + (System.currentTimeMillis() - time) / 1000 + " seconds");
		}

		return result;
	}
}
