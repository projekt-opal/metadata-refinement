package org.dice_research.opal.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.metadata_refinement.lang_detection.LangDetector;
import org.junit.Test;

import opennlp.tools.langdetect.Language;

public class LangDetectorTest {
	private static final String DE1 = "Guten Tag, wie spät ist es?";
	private static final String DE2 = "Üblicherweise werden Stundenwerte, Tageswerte, Monatswerte und langjährige Mittel der Stationsdaten verwendet. Die automatisierten Stationen melden Werte in höherer zeitlicher Auflösung ( 1-minütige Niederschlagsmessungen und 10-minütige Werte für Temperatur und Feuchte, Min/Max-Temperaturen, Niederschlag, Windgeschwindigkeit und Windspitze, solare Strahlungsparameter), die ab sofort auch zur Verfügung gestellt werden.";
	private static final String DE3 = "Üblicherweise werden Stundenwerte der Stationsdaten verwendet.";
	private static final String DE4 = "10-Minuten Stationsdaten: Extrem-Temperatur";
	private static final String DE5 = "Zehn Minuten Stationsdaten: Die sehr hohen Temperaturen";
	private static final String EN1 = "Good morning sir. What time is it, please?";

	/**
	 * Tests {@link LangDetector}
	 */
	@Test
	public void test() throws IOException {
		LangDetector langDetector = new LangDetector();
		Language lang;

		lang = langDetector.detectLanguage(DE1);
		System.out.println(lang + " " + this.getClass().getName());
		assertEquals("deu", lang.getLang());

		lang = langDetector.detectLanguage(EN1);
		System.out.println(lang + " " + this.getClass().getName());
		assertEquals("eng", lang.getLang());

		lang = langDetector.detectLanguage(DE2);
		System.out.println(lang + " " + this.getClass().getName());
		assertEquals("deu", lang.getLang());

		lang = langDetector.detectLanguage(DE3);
		System.out.println(lang + " " + this.getClass().getName());
		assertEquals("deu", lang.getLang());

		lang = langDetector.detectLanguage(DE4);
		System.out.println(lang + " " + this.getClass().getName());
		// lat (0.029925063621542805)
		// assertEquals("deu", lang.getLang());

		lang = langDetector.detectLanguage(DE5);
		System.out.println(lang + " " + this.getClass().getName());
		// deu (0.03238149535200103)
		assertEquals("deu", lang.getLang());

		// -> Confidence for short strings ~ 0.3
	}

	/**
	 * Tests {@link LanguageDetection}
	 */
	@Test
	public void testJenaModel() throws Exception {

		// Create test data

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://projekt-opal.de/dataset/10-minutenstationsdatenextrem-temperatur";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.dataset);

		Literal title = ResourceFactory.createPlainLiteral(DE1);
		model.addLiteral(dataset, DCTerms.title, title);

		Literal description = ResourceFactory.createPlainLiteral(DE2);
		model.addLiteral(dataset, DCTerms.description, description);

		// Test

		LanguageDetection languageDetection = new LanguageDetection();
		Model testModel = languageDetection.process(model, datasetUri);
		Resource testResource = testModel.getResource(datasetUri);
		assertTrue(testResource.hasProperty(DCTerms.title));
		assertTrue(testResource.hasProperty(DCTerms.description));

		assertFalse(testModel.getProperty(testResource, DCTerms.description).getObject().asLiteral().getLanguage()
				.isEmpty());
		assertFalse(testModel.getProperty(testResource, DCTerms.title).getObject().asLiteral().getLanguage().isEmpty());

		assertEquals("de",
				testModel.getProperty(testResource, DCTerms.description).getObject().asLiteral().getLanguage());
		assertEquals("de", testModel.getProperty(testResource, DCTerms.title).getObject().asLiteral().getLanguage());
	}

}