package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.dice_research.opal.metadata_extraction.lang_detection.LangDetector;
import org.junit.Test;

public class LangDetectorTest {

	@Test
	public void test() throws IOException {
		LangDetector langDetector = new LangDetector();
		assertEquals(LangDetector.LANG_DEU, langDetector.detectLanguageString("Guten Tag, wie spät ist es?"));
		assertEquals(LangDetector.LANG_ENG,
				langDetector.detectLanguageString("Good morning sir. What time is it, please?"));
	}

}
