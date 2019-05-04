package org.dice_research.opal.metadata_extraction.fox;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * Extraction of entities out of FOX results.
 *
 * @author Adrian Wilke
 */
public class Extraction {

	private static final Property TEXT_ANALYSIS_CLASS = ResourceFactory
			.createProperty("http://www.w3.org/2005/11/its/rdf#taClassRef");
	private static final Property TEXT_ANALYSIS_ID = ResourceFactory
			.createProperty("http://www.w3.org/2005/11/its/rdf#taIdentRef");
	private static final Property NIF_ANCHOR = ResourceFactory
			.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf");
	private static final Resource LOCATION = ResourceFactory.createResource("http://schema.org/Location");

	/**
	 * Extracts location names from FOX turtle.
	 */
	public List<String> extractLocationNames(String turtle) {

		// Read turtle to model

		StringReader stringReader = new StringReader(turtle);
		Model model = ModelFactory.createDefaultModel();
		model.read(stringReader, null, "TTL");

		// Extract locations

		List<String> locationNames = new LinkedList<>();
		ResIterator locationEntityIterator = model.listSubjectsWithProperty(TEXT_ANALYSIS_CLASS, LOCATION);
		while (locationEntityIterator.hasNext()) {
			Resource locationEntity = locationEntityIterator.next();
			NodeIterator nameIterator = model.listObjectsOfProperty(locationEntity, NIF_ANCHOR);
			while (nameIterator.hasNext()) {
				locationNames.add(nameIterator.next().asLiteral().getString());
			}
		}
		return locationNames;
	}

	/**
	 * Extracts location URIs from FOX turtle.
	 */
	public List<String> extractLocationUris(String turtle) {

		// Read turtle to model

		StringReader stringReader = new StringReader(turtle);
		Model model = ModelFactory.createDefaultModel();
		model.read(stringReader, null, "TTL");

		// Extract locations

		List<String> locationUris = new LinkedList<>();
		ResIterator locationEntityIterator = model.listSubjectsWithProperty(TEXT_ANALYSIS_CLASS, LOCATION);
		while (locationEntityIterator.hasNext()) {
			Resource locationEntity = locationEntityIterator.next();
			NodeIterator locationIdIterator = model.listObjectsOfProperty(locationEntity, TEXT_ANALYSIS_ID);
			while (locationIdIterator.hasNext()) {
				locationUris.add(locationIdIterator.next().asResource().getURI());
			}
		}
		return locationUris;
	}
}