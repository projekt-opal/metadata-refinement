package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dice_research.opal.metadata_extraction.fox.Extraction;
import org.junit.Test;

public class FoxExtractionTest {

	private static final String FILE_TTL_BERLIN = "src/test/resources/fox-response-berlin.ttl";
	private static final String URI_BERLIN = "http://de.dbpedia.org/resource/Berlin";

	@Test
	public void testExtraction() throws MalformedURLException, IOException {
		String ttl = FileUtils.readFileToString(new File(FILE_TTL_BERLIN), "UTF-8");
		List<String> locations = new Extraction().extractLocations(ttl);
		assertTrue(locations.contains(URI_BERLIN));
	}
}