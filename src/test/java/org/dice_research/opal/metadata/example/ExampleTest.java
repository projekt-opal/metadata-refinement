package org.dice_research.opal.metadata.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

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

	@Test
	public void testLanguage() throws Exception {

		String resourceName = "Europeandataportal-Iceland.ttl";
		URL url = this.getClass().getClassLoader().getResource(resourceName);
		File turtleInputFile = new File(url.toURI());

		File turtleOutputFile = File.createTempFile(ExampleTest.class.getName(), "");
		turtleOutputFile.deleteOnExit();

		String datasetUri = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

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
		turtleInputFile.deleteOnExit();

		File turtleOutputFile = File.createTempFile(ExampleTest.class.getName(), ".out.txt");
		turtleOutputFile.deleteOnExit();

		assertTrue(turtleInputFile.canRead());

		Example example = new Example();
		example.createGeoData(turtleInputFile, turtleOutputFile, datasetUri);

		assertTrue(turtleOutputFile.exists());
		assertNotEquals(turtleInputFile.length(), turtleOutputFile.length());
	}

}