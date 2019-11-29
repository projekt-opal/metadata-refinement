package org.dice_research.opal.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.metadata.LanguageDetection;
import org.dice_research.opal.metadata.lang.LangDetector;
import org.dice_research.opal.metadata.lang.TestData;
import org.junit.Test;

import opennlp.tools.langdetect.Language;

/**
 * Tests {@link LanguageDetection}.
 *
 * @author Adrian Wilke
 */
public class LanguageDetectionTest {

	@Test
	public void testLibrary() throws Exception {
		Language[] results = new LangDetector().predictLanguages(TestData.DE4);
		assertEquals(results[0].getLang(), "lat");

		// Problematic case: lat is returned instead of deu

		// for (Language language : results) {
		// System.out.println(language);
		// }

		// lat (0.029925063621542805)
		// swe (0.01992496647129409)
		// ron (0.01965788266047045)
		// deu (0.019636277011578288)
		// nld (0.01862476006396864)
		// eng (0.017850525730095598)

		Language language = new LanguageDetection().predictsupportedLanguage(TestData.DE4);
		assertEquals(language.getLang(), "deu");

		// Problematic case solved.
	}

	@Test
	public void test() throws Exception {

		// Create test data

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://projekt-opal.de/dataset/10-minutenstationsdatenextrem-temperatur";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.dataset);

		// Use of problematic string
		Literal title = ResourceFactory.createPlainLiteral(TestData.DE4);
		model.addLiteral(dataset, DCTerms.title, title);

		Literal description = ResourceFactory.createPlainLiteral(TestData.DE2);
		model.addLiteral(dataset, DCTerms.description, description);

		// Test

		LanguageDetection languageDetection = new LanguageDetection();

		Model testModel = languageDetection.process(model, datasetUri);
		Resource testResource = testModel.getResource(datasetUri);

		// Properties also in returned model
		assertTrue(testResource.hasProperty(DCTerms.title));
		assertTrue(testResource.hasProperty(DCTerms.description));

		// Literals have language tags
		assertFalse(testModel.getProperty(testResource, DCTerms.description).getObject().asLiteral().getLanguage()
				.isEmpty());
		assertFalse(testModel.getProperty(testResource, DCTerms.title).getObject().asLiteral().getLanguage().isEmpty());

		// Literals have correct language tags
		assertEquals("de",
				testModel.getProperty(testResource, DCTerms.description).getObject().asLiteral().getLanguage());
		assertEquals("de", testModel.getProperty(testResource, DCTerms.title).getObject().asLiteral().getLanguage());
	}

}
