package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LanguageDetectorTest {

	@Test
	public void test() throws IOException {
		LanguageDetector languageDetector = new LanguageDetector();
		assertEquals("deu", languageDetector.detectLanguageString("Guten Tag, wie spät ist es?"));
		assertEquals("eng", languageDetector.detectLanguageString("Good morning sir. What time is it, please?"));
	}

}
