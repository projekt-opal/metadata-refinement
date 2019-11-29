package org.dice_research.opal.metadata_refinement.lang_detection;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

/**
 * Detects language of texts based on OpenLNP default model.
 * 
 * The OpenNLP default model file is downloaded at the first run.
 * 
 * @see https://www.apache.org/dist/opennlp/models/langdetect/1.8.3/README.txt
 * 
 * @author Adrian Wilke
 */
public class LangDetector {

	private static final Logger LOGGER = LogManager.getLogger();

	// OpenNLP model
	// http://opennlp.apache.org/models.html
	private final static String MODEL_URL = "https://www-eu.apache.org/dist/opennlp/models/langdetect/1.8.3/langdetect-183.bin";
	private final static String MODEL_FILENAME = "OpenNLP.model.langdetect-183.bin";

	private static LanguageDetector languageDetector;
	private File modelFile;

	/**
	 * Detects a language. The returned object also contains the confidence of the
	 * detection.
	 * 
	 * @see http://opennlp.apache.org/models.html
	 * 
	 * @throws IOException on errors reading the language model
	 */
	public Language detectLanguage(String text) throws IOException {

		// Get/download model
		if (modelFile == null) {
			modelFile = new File(MODEL_FILENAME);

			if (!modelFile.exists()) {
				LOGGER.info("Downloading language model to " + modelFile.getPath());
				FileUtils.copyURLToFile(new URL(MODEL_URL), modelFile, 5000, 5000);
			}
		}

		// Create static language detector instance
		if (languageDetector == null) {
			LanguageDetectorModel model = new LanguageDetectorModel(FileUtils.openInputStream(modelFile));
			languageDetector = new LanguageDetectorME(model);
		}

		// Get best language
		return languageDetector.predictLanguage(text);
	}
}