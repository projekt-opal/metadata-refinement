package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import org.dice_research.opal.metadata_extraction.fox.Fox;
import org.junit.Test;

public class FoxTest {

	public static final String FOX_API_ENDPOINT = "http://fox.cs.upb.de:4444/fox";
	public static final boolean PRINT_RESULT = true;

	public static final String TEXT = "A. Einstein was born in Ulm.";
	public static final String LANG = Fox.LANG_EN;

	@Test
	public void test() throws MalformedURLException, IOException {
		Fox foxApi = new Fox().setEndpoint(FOX_API_ENDPOINT);
		String result = foxApi.send(TEXT, LANG);

		assertTrue(result.contains("Albert_Einstein"));

		if (PRINT_RESULT) {
			System.out.println(result);
		}
	}

}
