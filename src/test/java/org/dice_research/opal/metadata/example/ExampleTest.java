package org.dice_research.opal.metadata.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.common.utilities.FileHandler;
import org.junit.Test;

/**
 * Minimal working example.
 *
 * @author Adrian Wilke
 */
public class ExampleTest {

	public static final boolean DELETE_GEO_TEST_FILE = true;

	@Test
	public void testLanguage() throws Exception {

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://example.org/";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Literal title = ResourceFactory.createPlainLiteral("Places in Berlin");
		model.addLiteral(dataset, DCTerms.title, title);

		File turtleInputFile = File.createTempFile(ExampleTest.class.getName(), ".in.txt");
		FileHandler.export(turtleInputFile, model);
		turtleInputFile.deleteOnExit();

		File turtleOutputFile = File.createTempFile(ExampleTest.class.getName(), ".out.txt");
		turtleOutputFile.deleteOnExit();

		assertTrue(turtleInputFile.canRead());

		Example example = new Example();
		example.updateLanguageTags(turtleInputFile, turtleOutputFile, datasetUri);

		assertTrue(turtleOutputFile.exists());
		assertNotEquals(turtleInputFile.length(), turtleOutputFile.length());
	}

	@Test
	public void testGeo() throws Exception {

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://example.org/";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Literal title = ResourceFactory.createPlainLiteral("Places in Berlin");
		model.addLiteral(dataset, DCTerms.title, title);

		File turtleInputFile = File.createTempFile(ExampleTest.class.getName(), ".in.txt");
		FileHandler.export(turtleInputFile, model);
		if (DELETE_GEO_TEST_FILE) {
			turtleInputFile.deleteOnExit();
		} else {
			System.out.println("In:  " + turtleInputFile.getAbsolutePath());
		}

		File turtleOutputFile = File.createTempFile(ExampleTest.class.getName(), ".out.txt");
		if (DELETE_GEO_TEST_FILE) {
			turtleOutputFile.deleteOnExit();
		} else {
			System.out.println("Out: " + turtleOutputFile.getAbsolutePath());
		}

		assertTrue(turtleInputFile.canRead());

		Example example = new Example();
		example.createGeoData(turtleInputFile, turtleOutputFile, datasetUri);

		assertTrue(turtleOutputFile.exists());
		assertNotEquals(turtleInputFile.length(), turtleOutputFile.length());
	}

}