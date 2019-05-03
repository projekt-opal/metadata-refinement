package org.dice_research.opal.metadata_extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.dice_research.opal.common.vocabulary.OpalLanguage;
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetector;
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetectorJena;
import org.junit.Test;

public class LangDetectorTest {

	/**
	 * Test of {@link LangDetector}
	 */
	@Test
	public void test() throws IOException {
		LangDetector langDetector = new LangDetector();
		assertEquals(LangDetector.LANG_DEU, langDetector.detectLanguageString("Guten Tag, wie spät ist es?"));
		assertEquals(LangDetector.LANG_ENG,
				langDetector.detectLanguageString("Good morning sir. What time is it, please?"));
	}

	/**
	 * Test of {@link LangDetectorJena}
	 */
	@Test
	public void testJenaModel() throws IOException {

		// Create test data

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://projekt-opal.de/dataset/10-minutenstationsdatenextrem-temperatur";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, org.apache.jena.vocabulary.RDF.type, org.apache.jena.vocabulary.DCAT.dataset);

		Literal title = ResourceFactory.createPlainLiteral("10-Minuten Stationsdaten: Extrem-Temperatur");
		model.addLiteral(dataset, org.apache.jena.vocabulary.DCTerms.title, title);

		Literal description = ResourceFactory.createPlainLiteral(
				"Üblicherweise werden Stundenwerte, Tageswerte, Monatswerte und langjährige Mittel der Stationsdaten verwendet. Die automatisierten Stationen melden Werte in höherer zeitlicher Auflösung ( 1-minütige Niederschlagsmessungen und 10-minütige Werte für Temperatur und Feuchte, Min/Max-Temperaturen, Niederschlag, Windgeschwindigkeit und Windspitze, solare Strahlungsparameter), die ab sofort auch zur Verfügung gestellt werden.");
		model.addLiteral(dataset, org.apache.jena.vocabulary.DCTerms.description, description);

		// Test

		LangDetectorJena langDetectorJena = new LangDetectorJena();
		Model testModel = langDetectorJena.addLanguage(model);

		// The defined title and description should be returned
		Resource testResource = testModel.getResource(datasetUri);
		assertTrue(testResource.hasProperty(org.apache.jena.vocabulary.DCTerms.title));
		assertTrue(testResource.hasProperty(org.apache.jena.vocabulary.DCTerms.description));

		// The language should be in the model
		NodeIterator langIterator = testModel.listObjectsOfProperty(testResource, OpalLanguage.language);
		assertTrue(langIterator.hasNext());
		assertEquals(OpalLanguage.LANGUAGE_DE, langIterator.next().asResource());
	}

}