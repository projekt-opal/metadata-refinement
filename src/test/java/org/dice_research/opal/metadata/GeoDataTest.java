package org.dice_research.opal.metadata;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
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
		Assert.assertEquals("geo data size", linesOfPlacesFile / 3, geoData.geoContainers.size());
		System.out.println((linesOfPlacesFile / 3) + " places " + GeoDataTest.class.getName());

		String firstKey = geoData.geoContainers.keySet().iterator().next();
		float firstLat = geoData.geoContainers.get(firstKey).lat;
		float firstLon = geoData.geoContainers.get(firstKey).lon;
		Assert.assertEquals("geo data first label", "Heiligenstedtenerkamp", firstKey);
		Assert.assertEquals("geo data first lat", 53.9f, firstLat, 0);
		Assert.assertEquals("geo data first lon", 9.46667f, firstLon, 0);
	}

	@Test
	public void testExtraction() throws Exception {
		Model model = ModelFactory.createDefaultModel();

		String datasetUri = "http://projekt-opal.de/example";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.dataset);

		Literal title = ResourceFactory.createPlainLiteral(TestData.DE6);
		model.addLiteral(dataset, DCTerms.title, title);

		Literal description = ResourceFactory.createPlainLiteral(TestData.DE2);
		model.addLiteral(dataset, DCTerms.description, description);

		// TODO
		GeoData geoData = new GeoData();
		geoData.process(model, datasetUri);
	}
}