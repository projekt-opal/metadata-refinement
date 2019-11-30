package org.dice_research.opal.metadata.geo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.dice_research.opal.common.utilities.FileHandler;
import org.dice_research.opal.metadata.GeoData;

/**
 * Extracts LauNuts data (place-label, latitude, longitude) to file.
 * 
 * This is a batch process, which has to be executed only once.
 * 
 * @see https://hobbitdata.informatik.uni-leipzig.de/OPAL/LauNuts/
 * @see https://github.com/projekt-opal/LauNuts
 *
 * @author Adrian Wilke
 */
public class LauNutsExtractor {

	public static final String VERSION_0_3_0 = "0.3.0";
	public static final boolean RESTRICT_TO_ONE_WORD = true;
	public static final boolean LABELS_TO_LOWER_CASE = false;

	public static final String NS_LAUNUTS = "http://projekt-opal.de/launuts/";
	public static final Resource RES_NUTS = ResourceFactory.createResource(NS_LAUNUTS + "NUTS");
	public static final Resource RES_LAU = ResourceFactory.createResource(NS_LAUNUTS + "LAU");

	public static final String NS_GEO = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	public static final Property PROP_LAT = ResourceFactory.createProperty(NS_GEO, "lat");
	public static final Property PROP_LONG = ResourceFactory.createProperty(NS_GEO, "long");

	/**
	 * Main entry point.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("No file provided.");
			System.exit(1);
		} else {
			File inFile = new File(args[0]);
			File outFile = new File("src/main/resources/" + GeoData.PLACES_FILE);
			if (inFile.canRead()) {
				new LauNutsExtractor().extract(inFile, outFile, VERSION_0_3_0);
			} else {
				System.err.println("Can not read: " + inFile.getAbsolutePath());
				System.exit(1);
			}
		}
	}

	/**
	 * Extracts data from file.
	 */
	private void extract(File inFile, File outFile, String version) {
		if (version != VERSION_0_3_0) {
			throw new RuntimeException("Version not supported: " + version);
		}

		System.out.println("Input file: " + inFile.getAbsolutePath());
		System.out.println("Output file: " + outFile.getAbsolutePath());

		Model model = FileHandler.importModel(inFile);

		// Dev: Print types
		// http://dbpedia.org/ontology/Place 10093
		// http://projekt-opal.de/launuts/LAU 11087
		// http://projekt-opal.de/launuts/NUTS 456
		// http://projekt-opal.de/launuts/NUTS-1 16
		// http://projekt-opal.de/launuts/NUTS-0 1
		// http://projekt-opal.de/launuts/NUTS-3 401
		// http://projekt-opal.de/launuts/NUTS-2 38
		if (Boolean.FALSE) {
			for (Entry<String, Integer> type : getTypes(model).entrySet()) {
				System.out.println(type.getKey() + "  " + type.getValue());
			}
		}

		// Map for containers to ensure every label is unique
		Map<String, GeoContainer> containerMap = new HashMap<>();

		// Extract NUTS
		ResIterator resIterator = model.listSubjectsWithProperty(RDF.type, RES_NUTS);
		while (resIterator.hasNext()) {
			GeoContainer container = extract(resIterator.next());
			if (container.isComplete()) {
				containerMap.put(container.label, container);
			}
		}

		// Extract LAU
		resIterator = model.listSubjectsWithProperty(RDF.type, RES_LAU);
		while (resIterator.hasNext()) {
			GeoContainer container = extract(resIterator.next());
			if (container.isComplete()) {
				containerMap.put(container.label, container);
			}
		}

		// Longer labels first
		ArrayList<GeoContainer> containers = new ArrayList<>(containerMap.values());
		Collections.sort(containers);

		// Lazy transform
		List<String> lines = new ArrayList<>(containers.size() * 3);
		for (GeoContainer container : containers) {
			lines.add(container.label);
			lines.add(String.valueOf(container.lat));
			lines.add(String.valueOf(container.lon));
		}

		// Write
		try {
			FileUtils.writeLines(outFile, lines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Extracts data to container.
	 */
	private GeoContainer extract(Resource resource) {
		GeoContainer container = new GeoContainer();

		if (resource.hasProperty(SKOS.prefLabel)) {
			String label = resource.getProperty(SKOS.prefLabel).getObject().asLiteral().getString().trim();

			if (LABELS_TO_LOWER_CASE) {
				label = label.toLowerCase();
			}

			if (RESTRICT_TO_ONE_WORD) {
				if (LABELS_TO_LOWER_CASE) {
					if (!label.matches("[a-z\\x7f-\\xff]{3,}")) {
						return container;
					}
				} else {
					if (!label.matches("[A-Za-z\\x7f-\\xff]{3,}")) {
						return container;
					}
				}
			}

			container.label = label;
		}

		if (resource.hasProperty(SKOS.relatedMatch))

		{
			RDFNode placeNode = resource.getProperty(SKOS.relatedMatch).getObject();
			if (placeNode.isResource()) {
				Resource place = placeNode.asResource();
				container.lat = place.getProperty(PROP_LAT).getObject().asLiteral().getFloat();
				container.lon = place.getProperty(PROP_LONG).getObject().asLiteral().getFloat();
			}
		}

		return container;
	}

	/**
	 * Gets types and number of subjects with related type.
	 */
	protected Map<String, Integer> getTypes(Model model) {
		Map<String, Integer> map = new HashMap<>();
		NodeIterator typeNodes = model.listObjectsOfProperty(RDF.type);
		while (typeNodes.hasNext()) {
			RDFNode typeNode = typeNodes.next();
			if (typeNode.isURIResource()) {
				ResIterator resIterator = model.listSubjectsWithProperty(RDF.type, typeNode);
				int counter = 0;
				while (resIterator.hasNext()) {
					resIterator.next();
					counter++;
				}
				map.put(typeNode.asResource().getURI(), Integer.valueOf(counter));
			}
		}
		return map;
	}

}