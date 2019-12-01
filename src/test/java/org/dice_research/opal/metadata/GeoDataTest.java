package org.dice_research.opal.metadata;

import java.io.File;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.common.utilities.FileHandler;
import org.dice_research.opal.metadata.lang.TestData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link GeoData}.
 *
 * @author Adrian Wilke
 */
public class GeoDataTest {

	@Test
	public void testInitialization() throws Exception {

		int linesOfPlacesFile = 25485;

		GeoData geoData = new GeoData();
		geoData.initialize();
		Assert.assertEquals("geo data size", linesOfPlacesFile / 3, GeoData.geoContainers.size());
		if (Boolean.FALSE) {
			System.out.println((linesOfPlacesFile / 3) + " places " + GeoDataTest.class.getName());
		}

		String firstKey = GeoData.geoContainers.keySet().iterator().next();
		float firstLat = GeoData.geoContainers.get(firstKey).lat;
		float firstLon = GeoData.geoContainers.get(firstKey).lon;
		Assert.assertEquals("geo data first label", "Heiligenstedtenerkamp", firstKey);
		Assert.assertEquals("geo data first lat", 53.9f, firstLat, 0);
		Assert.assertEquals("geo data first lon", 9.46667f, firstLon, 0);
	}

	@Test
	public void testExtraction() throws Exception {
		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://projekt-opal.de/example";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Literal title = ResourceFactory.createPlainLiteral(TestData.DE7);
		model.addLiteral(dataset, DCTerms.title, title);

		Literal description = ResourceFactory.createPlainLiteral(TestData.DE2);
		model.addLiteral(dataset, DCTerms.description, description);

		GeoData geoData = new GeoData();
		model = geoData.process(model, datasetUri);

		NodeIterator spatialObjIt = model
				.listObjectsOfProperty(ResourceFactory.createProperty("http://purl.org/dc/terms/spatial"));
		Assert.assertTrue(spatialObjIt.hasNext());

		Resource location = spatialObjIt.next().asResource();
		Resource locationType = location.getProperty(RDF.type).getObject().asResource();
		Assert.assertEquals("http://purl.org/dc/terms/Location", locationType.getURI());

		NodeIterator geometryObjIt = model
				.listObjectsOfProperty(ResourceFactory.createProperty("http://www.w3.org/ns/dcat#centroid"));
		Assert.assertTrue(geometryObjIt.hasNext());

		// Write file for comparisons
		if (Boolean.FALSE) {
			File file = File.createTempFile(this.getClass().getName() + ".", ".txt");
			FileHandler.export(file, model);
			System.out.println("Wrote: " + file.getAbsolutePath() + " " + this.getClass().getName());
		}
	}

	/**
	 * Tests, if processing is skipped if spatial data exists.
	 */
	@Test
	public void testSkipOnSpatialExist() throws Exception {
		Model model = ModelFactory.createDefaultModel();

		// Create data with title, which would be processed

		String datasetUri = "http://projekt-opal.de/skipTest";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Literal title = ResourceFactory.createPlainLiteral("Berlin and Paderborn");
		model.addLiteral(dataset, DCTerms.title, title);

		Model modelWithtitle = ModelFactory.createDefaultModel().add(model);
		Model inputModel = ModelFactory.createDefaultModel().add(modelWithtitle);

		// Check if model would be processed

		Model modelWithtitleProcessed = new GeoData().process(inputModel, datasetUri);
		Assert.assertNotEquals(modelWithtitle.size(), modelWithtitleProcessed.size());

		// Add spatial data to create model NOT to process

		Literal spatialDummy = ResourceFactory.createPlainLiteral("Dummy");
		model.addLiteral(dataset, DCTerms.spatial, spatialDummy);

		// Check if model is NOT processed

		inputModel = ModelFactory.createDefaultModel().add(model);
		Model processedModel = new GeoData().process(inputModel, datasetUri);
		Assert.assertEquals(model.size(), processedModel.size());
	}
}