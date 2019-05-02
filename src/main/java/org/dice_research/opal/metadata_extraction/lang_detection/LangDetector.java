package org.dice_research.opal.metadata_extraction.lang_detection;

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
 * The OpenNLP default model file is downloaded at the first run.
 * 
 * @author Adrian Wilke
 */
public class LangDetector {

	// https://www.apache.org/dist/opennlp/models/langdetect/1.8.3/README.txt
	public final static String LANG_DEU = "deu";
	public final static String LANG_ENG = "eng";
	public final static String LANG_FRA = "fra";
	public final static String LANG_SPA = "spa";

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
	 * Detects a language. The returned object also contains the confidence of the
	 * detection.
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