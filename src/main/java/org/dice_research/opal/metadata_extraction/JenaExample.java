package org.dice_research.opal.metadata_extraction;

import java.io.IOException;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.dice_research.opal.metadata_extraction.fox.Fox;
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetector;

/**
 * Prototypical example code for combining OPAL graph, language detection, and
 * entity recognition.
 * 
 * @author Adrian Wilke
 */
public class JenaExample {

	public static final String QUERY_DATASET = "PREFIX dct: <http://purl.org/dc/terms/> "

			+ "PREFIX dcat: <http://www.w3.org/ns/dcat#> "

			+ "SELECT DISTINCT ?dataset ?title ?description "

			+ "FROM <http://projekt-opal.de> "

			+ "WHERE { "

			+ "?dataset a dcat:Dataset . "

			+ "?dataset dct:title ?title . "

			+ "?dataset dct:description ?description "

			+ "} LIMIT 1";

	public static final String QUERY_DISTRIBUTION = "PREFIX dct: <http://purl.org/dc/terms/> "

			+ "PREFIX dcat: <http://www.w3.org/ns/dcat#> "

			+ "SELECT DISTINCT ?distribution ?title ?description "

			+ "FROM <http://projekt-opal.de> "

			+ "WHERE { "

			+ "?distribution a dcat:Distribution . "

			+ "?distribution dct:title ?title . "

			+ "?distribution dct:description ?description "

			+ "} LIMIT 1";

	public static String FOX_API_ENDPOINT = "http://fox.cs.upb.de:4444/fox";
	public static String graph = "<http://projekt-opal.de>";
	public static String endpoint = "http://opalpro.cs.upb.de:3030/civet/sparql";

	/**
	 * (1) Gets the text (title and description) of a random dataset.
	 * 
	 * (2) Detects the language of the text.
	 * 
	 * (3) Gets URIs of entities.
	 * 
	 * Repeats the steps for a distribution instead of a dataset.
	 * 
	 * TODO:
	 * 
	 * The final webservise may get (A) a Jena model and (B) a distribution URI. It
	 * should add the URIs of recognized geo locations to the model and return it.
	 * 
	 * In a test, '<http://aksw.org/notInWiki/Berlin>' was returned instead of a
	 * dbpedia URI. Check why.
	 */
	public static void main(String[] args) throws IOException {
		String text, lang, entities;

		// Dataset example
		// Hack to ensure to get a location
		text = "Berlin " + new JenaExample().getDatasetText();

		lang = new LangDetector().detectLanguageString(text);

		// The model of the language detector uses other language abbreviations than
		// FOX, e.g. 'deu' and 'de'. This has to be mapped.
		if (lang.equals("deu")) {
			entities = new org.dice_research.opal.metadata_extraction.fox.Fox().setEndpoint(FOX_API_ENDPOINT).send(text,
					Fox.LANG_DE);
		} else {
			entities = "-";
		}

		System.out.println("TEXT");
		System.out.println(text);
		System.out.println();

		System.out.println("LANG");
		System.out.println(lang);
		System.out.println();

		System.out.println("ENTITIES");
		System.out.println(entities);
		System.out.println();

		// Distribution example

		text = new JenaExample().getDistributionText();
		lang = new LangDetector().detectLanguageString(text);
		if (lang.equals(LangDetector.LANG_DEU)) {
			entities = new org.dice_research.opal.metadata_extraction.fox.Fox().setEndpoint(FOX_API_ENDPOINT).send(text,
					Fox.LANG_DE);
		} else {
			entities = "-";
		}

		System.out.println("TEXT");
		System.out.println(text);
		System.out.println();

		System.out.println("LANG");
		System.out.println(lang);
		System.out.println();

		System.out.println("ENTITIES");
		System.out.println(entities);
		System.out.println();
	}

	/**
	 * Gets the title and description of a random dataset.
	 */
	public String getDatasetText() {
		QueryExecution request = QueryExecutionFactory.sparqlService(endpoint, QUERY_DATASET);
		ResultSet resultSet = request.execSelect();
		StringBuilder stringBuilder = new StringBuilder();
		if (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			System.out.println("Dataset: " + querySolution.get("dataset"));
			stringBuilder.append(querySolution.get("title"));
			stringBuilder.append(System.lineSeparator());
			stringBuilder.append(querySolution.get("description"));
		}
		return stringBuilder.toString();
	}

	/**
	 * Gets the title and description of a random distribution.
	 */
	public String getDistributionText() {
		QueryExecution request = QueryExecutionFactory.sparqlService(endpoint, QUERY_DISTRIBUTION);
		ResultSet resultSet = request.execSelect();
		StringBuilder stringBuilder = new StringBuilder();
		if (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			System.out.println("Dataset: " + querySolution.get("distribution"));
			stringBuilder.append(querySolution.get("title"));
			stringBuilder.append(System.lineSeparator());
			stringBuilder.append(querySolution.get("description"));
		}
		return stringBuilder.toString();
	}
}