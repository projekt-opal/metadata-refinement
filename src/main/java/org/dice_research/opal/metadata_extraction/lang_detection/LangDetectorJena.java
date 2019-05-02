package org.dice_research.opal.metadata_extraction.lang_detection;

import java.io.IOException;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.ResourceFactory;
import org.dice_research.opal.common.constants.ISO_639_1;

import opennlp.tools.langdetect.Language;

public class LangDetectorJena {

	private LangDetector langDetector = new LangDetector();
	private final static Literal LITERAL_DE = ResourceFactory.createPlainLiteral(ISO_639_1.DE);
	private final static Literal LITERAL_EN = ResourceFactory.createPlainLiteral(ISO_639_1.EN);
	private final static Literal LITERAL_ES = ResourceFactory.createPlainLiteral(ISO_639_1.ES);
	private final static Literal LITERAL_FR = ResourceFactory.createPlainLiteral(ISO_639_1.FR);

	/**
	 * 
	 * @throws IOException on errors reading the language model
	 */
	public Model addLanguage(Model model) throws IOException {

		// Duplicate model object
		model = ModelFactory.createDefaultModel().add(model);

		// Go through datasets
		ResIterator datasetIterator = model.listSubjectsWithProperty(org.apache.jena.vocabulary.RDF.type,
				org.apache.jena.vocabulary.DCAT.dataset);
		StringBuilder stringBuilder = new StringBuilder();
		while (datasetIterator.hasNext()) {
			RDFNode dataset = datasetIterator.next();

			// Collect natural language parts

			NodeIterator descriptionIterator = model.listObjectsOfProperty(dataset.asResource(),
					org.apache.jena.vocabulary.DCTerms.description);
			if (descriptionIterator.hasNext()) {
				stringBuilder.append(descriptionIterator.next());
			}

			NodeIterator titleIterator = model.listObjectsOfProperty(dataset.asResource(),
					org.apache.jena.vocabulary.DCTerms.title);
			if (titleIterator.hasNext()) {
				stringBuilder.append(System.lineSeparator());
				stringBuilder.append(titleIterator.next());
			}

			// Detect language

			if (stringBuilder.length() != 0) {
				Language language = langDetector.detectLanguage(stringBuilder.toString());
				String languageKey = language.getLang();

				if (languageKey.equals(LangDetector.LANG_DEU)) {
					model.addLiteral(dataset.asResource(), org.apache.jena.vocabulary.DCTerms.language, LITERAL_DE);
				} else if (languageKey.equals(LangDetector.LANG_ENG)) {
					model.addLiteral(dataset.asResource(), org.apache.jena.vocabulary.DCTerms.language, LITERAL_EN);
				} else if (languageKey.equals(LangDetector.LANG_FRA)) {
					model.addLiteral(dataset.asResource(), org.apache.jena.vocabulary.DCTerms.language, LITERAL_FR);
				} else if (languageKey.equals(LangDetector.LANG_SPA)) {
					model.addLiteral(dataset.asResource(), org.apache.jena.vocabulary.DCTerms.language, LITERAL_ES);
				}

			}

		}

		return model;
	}
}
