package org.dice_research.opal.metadata_extraction;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

/**
 * Detects language of texts based on OpenLNP default model.
 * 
 * @author Adrian Wilke
 */
public class LanguageDetector {

	// http://opennlp.apache.org/models.html
	private final static String MODEL_URL = "https://www-eu.apache.org/dist/opennlp/models/langdetect/1.8.3/langdetect-183.bin";
	private final static String MODEL_FILENAME = "OpenNLP.model.langdetect-183.bin";

	private static opennlp.tools.langdetect.LanguageDetector languageDetector;

	private File modelFile;

	/**
	 * Detects a language and returns the representing String.
	 * 
	 * @see https://www.apache.org/dist/opennlp/models/langdetect/1.8.3/README.txt
	 */
	public String detectLanguageString(String text) throws IOException {
		return detectLanguage(text).getLang();
	}

	/**
	 * Detects a language.
	 * 
	 * @see http://opennlp.apache.org/models.html
	 */
	public Language detectLanguage(String text) throws IOException {

		// Get model
		if (modelFile == null) {
			modelFile = new File(MODEL_FILENAME);

			if (!modelFile.exists()) {
				System.out.println("Downloading " + modelFile.getPath());
				FileUtils.copyURLToFile(new URL(MODEL_URL), modelFile, 5000, 5000);
			}
		}

		// Create detector
		if (languageDetector == null) {
			LanguageDetectorModel model = new LanguageDetectorModel(FileUtils.openInputStream(modelFile));
			languageDetector = new LanguageDetectorME(model);
		}

		// Get best language
		return languageDetector.predictLanguage(text);
	}
}