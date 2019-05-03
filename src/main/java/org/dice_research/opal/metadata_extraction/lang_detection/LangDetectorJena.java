package org.dice_research.opal.metadata_extraction.lang_detection;

import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.common.vocabulary.OpalLanguage;

import opennlp.tools.langdetect.Language;

/**
 * Language detection of datasets in Jena model.
 *
 * @author Adrian Wilke
 */
public class LangDetectorJena {

	private LangDetector langDetector = new LangDetector();

	/**
	 * Detects languages of metadata of datasets.
	 * 
	 * @throws IOException on errors reading the language model
	 */
	public Model addLanguage(Model model) throws IOException {

		// Duplicate model object
		model = ModelFactory.createDefaultModel().add(model);

		// Go through datasets
		ResIterator datasetIterator = model.listSubjectsWithProperty(RDF.type, DCAT.dataset);
		StringBuilder stringBuilder = new StringBuilder();
		while (datasetIterator.hasNext()) {
			RDFNode dataset = datasetIterator.next();

			// Collect natural language parts.
			// Assumes there is exactly one title/description.

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

			// Detect language of dataset and add it to model

			if (stringBuilder.length() != 0) {
				Language lang = langDetector.detectLanguage(stringBuilder.toString());
				String languageKey = lang.getLang();

				if (languageKey.equals(LangDetector.LANG_DEU)) {
					model.add(dataset.asResource(), OpalLanguage.language, OpalLanguage.LANGUAGE_DE);
				} else if (languageKey.equals(LangDetector.LANG_ENG)) {
					model.add(dataset.asResource(), OpalLanguage.language, OpalLanguage.LANGUAGE_EN);
				} else if (languageKey.equals(LangDetector.LANG_FRA)) {
					model.add(dataset.asResource(), OpalLanguage.language, OpalLanguage.LANGUAGE_FR);
				} else if (languageKey.equals(LangDetector.LANG_SPA)) {
					model.add(dataset.asResource(), OpalLanguage.language, OpalLanguage.LANGUAGE_ES);
				}
			}
		}

		return model;
	}
}