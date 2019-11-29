package org.dice_research.opal.metadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.common.constants.ISO_639_1;
import org.dice_research.opal.common.interfaces.JenaModelProcessor;
import org.dice_research.opal.metadata_refinement.lang_detection.ISO_639_3;
import org.dice_research.opal.metadata_refinement.lang_detection.LangDetector;

import opennlp.tools.langdetect.Language;

/**
 * Updates language tags of titles and descriptions.
 *
 * @author Adrian Wilke
 */
public class LanguageDetection implements JenaModelProcessor {

	protected final static double REQUIRED_CONFIDENCE_TITLE = 0.01;
	protected final static double REQUIRED_CONFIDENCE_DESCRIPTION = 0.75;

	protected static LangDetector langDetector;
	protected static Map<String, String> supportedLanguages;
	protected static Set<String> supportedIso_639_3;

	@Override
	public Model process(Model model, String datasetUri) throws Exception {

		initialize();

		// Create a new model
		model = ModelFactory.createDefaultModel().add(model);
		Resource dataset = ResourceFactory.createResource(datasetUri);

		// Set language tag for title
		NodeIterator titleIterator = model.listObjectsOfProperty(dataset, DCTerms.title);
		while (titleIterator.hasNext()) {
			RDFNode titleNode = titleIterator.next();
			if (titleNode.isLiteral()) {
				Literal literal = updateLanguageTag(titleNode.asLiteral(), REQUIRED_CONFIDENCE_TITLE);
				model.remove(dataset, DCTerms.title, titleNode);
				model.add(dataset, DCTerms.title, literal);
			}
		}

		// Set language tag for description
		NodeIterator decriptionIterator = model.listObjectsOfProperty(dataset, DCTerms.description);
		while (decriptionIterator.hasNext()) {
			RDFNode decriptionNode = decriptionIterator.next();
			if (decriptionNode.isLiteral()) {
				Literal literal = updateLanguageTag(decriptionNode.asLiteral(), REQUIRED_CONFIDENCE_DESCRIPTION);
				model.remove(dataset, DCTerms.description, decriptionNode);
				model.add(dataset, DCTerms.description, literal);
			}
		}

		return model;
	}

	/**
	 * Updates language tag.
	 * 
	 * If language tag is already set, nothing is changed.
	 * 
	 * If the confidence of language detection is less 'requiredConfidence', nothing
	 * is changed.
	 * 
	 * If the detected language is not supported, nothing is changed.
	 * 
	 * @throws IOException on language detection errors
	 */
	protected Literal updateLanguageTag(Literal literal, double requiredConfidence) throws IOException {
		if (literal.getLanguage().isEmpty()) {
			Language lang = predictsupportedLanguage(literal.getString());
			if (lang != null && lang.getConfidence() >= requiredConfidence) {
				return ResourceFactory.createLangLiteral(literal.getString(),
						LanguageDetection.supportedLanguages.get(lang.getLang()));
			}
		}
		return literal;
	}

	/**
	 * Predicts language.
	 * 
	 * The returned object also contains the confidence of the detection.
	 * 
	 * @return predicted language or null
	 * 
	 * @throws IOException on language detection errors
	 */
	public Language predictsupportedLanguage(String text) throws IOException {

		initialize();

		// Get highest confidence for supported languages
		Language predictedLanguage = null;
		Language[] languages = LanguageDetection.langDetector.predictLanguages(text);
		for (Language language : languages) {
			if (LanguageDetection.supportedIso_639_3.contains(language.getLang())) {
				if (predictedLanguage == null || language.getConfidence() >= predictedLanguage.getConfidence()) {
					predictedLanguage = language;
				}
			}
		}

		return predictedLanguage;
	}

	/**
	 * Sets class vaiables.
	 */
	protected void initialize() {

		// Will download language model (10 MB) on first run.
		if (LanguageDetection.langDetector == null) {
			LanguageDetection.langDetector = new LangDetector();
		}

		// Initialize supported languages
		if (LanguageDetection.supportedLanguages == null) {
			LanguageDetection.supportedLanguages = getSupportedLanguages();
			LanguageDetection.supportedIso_639_3 = supportedLanguages.keySet();
		}
	}

	/**
	 * Gets mapping ISO-639-3 to ISO-639-1 of supported languages.
	 */
	protected Map<String, String> getSupportedLanguages() {
		Map<String, String> map = new HashMap<>();
		map.put(ISO_639_3.CODE_DEU, ISO_639_1.CODE_DE);
		map.put(ISO_639_3.CODE_ENG, ISO_639_1.CODE_EN);
		map.put(ISO_639_3.CODE_FRA, ISO_639_1.CODE_FR);
		map.put(ISO_639_3.CODE_SPA, ISO_639_1.CODE_ES);
		return map;
	}
}