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
	private static final String URI_PADERBORN = "http://de.dbpedia.org/resource/Paderborn";
	private static final String URI_NRW = "http://de.dbpedia.org/resource/NRW";
	private static final String URI_DEUTSCHLAND = "http://de.dbpedia.org/resource/Deutschland";
	private static final String URI_BAD_OEYNHAUSEN = "http://de.dbpedia.org/resource/Bad_Oeynhausen";

	private static final String NAME_BERLIN = "Berlin";
	private static final String NAME_PADERBORN = "Paderborn";
	private static final String NAME_NRW = "NRW";
	private static final String NAME_DEUTSCHLAND = "Deutschland";
	private static final String NAME_BAD_OEYNHAUSEN = "Bad Oeynhausen";

	@Test
	public void testNameExtraction() throws MalformedURLException, IOException {
		String ttl = FileUtils.readFileToString(new File(FILE_TTL_BERLIN), "UTF-8");
		List<String> locations = new Extraction().extractLocationNames(ttl);
		assertTrue(locations.contains(NAME_BERLIN));
		assertTrue(locations.contains(NAME_PADERBORN));
		assertTrue(locations.contains(NAME_NRW));
		assertTrue(locations.contains(NAME_DEUTSCHLAND));
		assertTrue(locations.contains(NAME_BAD_OEYNHAUSEN));
	}

	@Test
	public void testUriExtraction() throws MalformedURLException, IOException {
		String ttl = FileUtils.readFileToString(new File(FILE_TTL_BERLIN), "UTF-8");
		List<String> locations = new Extraction().extractLocationUris(ttl);
		assertTrue(locations.contains(URI_BERLIN));
		assertTrue(locations.contains(URI_PADERBORN));
		assertTrue(locations.contains(URI_NRW));
		assertTrue(locations.contains(URI_DEUTSCHLAND));
		assertTrue(locations.contains(URI_BAD_OEYNHAUSEN));
	}
}