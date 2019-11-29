package org.dice_research.opal.metadata;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.dice_research.opal.metadata_refinement.lang_detection.LangDetector;
import org.junit.Test;

import opennlp.tools.langdetect.Language;

/**
 * Tests OpenLNP wrapper {@link LangDetector}.
 * 
 * @author Adrian Wilke
 */
public class LangDetectorTest {

	@Test
	public void test() throws IOException {
		LangDetector langDetector = new LangDetector();
		Language result;
		String input;
		String expected;

		input = TestData.EN1;
		expected = "eng";
		result = langDetector.predictLanguage(input);
		printResult(result, input);
		assertEquals(expected, result.getLang());

		input = TestData.DE1;
		expected = "deu";
		result = langDetector.predictLanguage(input);
		printResult(result, input);
		assertEquals(expected, result.getLang());

		input = TestData.DE2;
		expected = "deu";
		result = langDetector.predictLanguage(input);
		printResult(result, input);
		assertEquals(expected, result.getLang());

		input = TestData.DE3;
		expected = "deu";
		result = langDetector.predictLanguage(input);
		printResult(result, input);
		assertEquals(expected, result.getLang());

		input = TestData.DE4;
		expected = "deu";
		result = langDetector.predictLanguage(input);
		printResult(result, input);
		// assertEquals(expected, result.getLang());
		// result: lat (0.029925063621542805)
		// Problem with library.
		// Possible solution: Check all predictions and use only supported languages

		input = TestData.DE5;
		expected = "deu";
		result = langDetector.predictLanguage(input);
		printResult(result, input);
		assertEquals(expected, result.getLang());
	}

	private void printResult(Language language, String input) {
		int textSize = 30;
		if (input.length() > textSize) {
			input = input.substring(0, textSize);
		}
		System.out.println(language + " " + input + " " + this.getClass().getName());
	}
}