package org.dice_research.opal.metadata;

import java.io.IOException;

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

	protected static LangDetector langDetector;
	protected final static double REQUIRED_CONFIDENCE_TITLE = 0.03;
	protected final static double REQUIRED_CONFIDENCE_DESCRIPTION = 0.75;

	@Override
	public Model process(Model model, String datasetUri) throws Exception {

		// Initialize.
		// Will download language model (10 MB) on first run.
		if (LanguageDetection.langDetector == null) {
			LanguageDetection.langDetector = new LangDetector();
		}

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
			Language lang = langDetector.detectLanguage(literal.getString());
			if (lang.getConfidence() >= requiredConfidence) {
				if (lang.getLang().equals(ISO_639_3.CODE_DEU)) {
					return ResourceFactory.createLangLiteral(literal.getString(), ISO_639_1.CODE_DE);
				} else if (lang.getLang().equals(ISO_639_3.CODE_ENG)) {
					return ResourceFactory.createLangLiteral(literal.getString(), ISO_639_1.CODE_EN);
				} else if (lang.getLang().equals(ISO_639_3.CODE_FRA)) {
					return ResourceFactory.createLangLiteral(literal.getString(), ISO_639_1.CODE_FR);
				} else if (lang.getLang().equals(ISO_639_3.CODE_SPA)) {
					return ResourceFactory.createLangLiteral(literal.getString(), ISO_639_1.CODE_ES);
				}
			}
		}
		return literal;
	}
}