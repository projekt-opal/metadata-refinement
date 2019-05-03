package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import org.dice_research.opal.metadata_extraction.fox.FoxRemote;
import org.junit.Test;

public class FoxRemoteTest {

	public static final String FOX_API_ENDPOINT = "http://fox.cs.upb.de:4444/fox";
	public static final boolean PRINT_RESULT = false;

	public static final String TEXT = "A. Einstein was born in Ulm.";
	public static final String LANG = FoxRemote.LANG_EN;

	public static final String FILE_TTL_BERLIN = "src/test/resources/fox-response-berlin.ttl";

	@Test
	public void test() throws MalformedURLException, IOException {
		FoxRemote foxRemote = new FoxRemote().setEndpoint(FOX_API_ENDPOINT);
		String result = foxRemote.request(TEXT, LANG);

		assertTrue(result.contains("Albert_Einstein"));

		if (PRINT_RESULT) {
			System.out.println(result);
		}
	}
}
