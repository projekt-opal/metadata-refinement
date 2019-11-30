package org.dice_research.opal.metadata.example;

import java.io.File;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.common.utilities.FileHandler;
import org.dice_research.opal.metadata.GeoData;
import org.dice_research.opal.metadata.LanguageDetection;

public class Example {

	/**
	 * Updates language tags of title and description literals.
	 * 
	 * @param turtleInputFile  A TURTLE file to read
	 * @param turtleOutputFile A TURTLE file to write results
	 * @param datasetUri       A URI of a dcat:Dataset inside the TURTLE data
	 * 
	 * @see https://www.w3.org/TR/turtle/
	 * @see https://www.w3.org/TR/vocab-dcat/
	 */
	public void updateLanguageTags(File turtleInputFile, File turtleOutputFile, String datasetUri) throws Exception {

		// Load TURTLE file into model
		Model model = FileHandler.importModel(turtleInputFile);

		// The call of initialize() is optional. It can be used to trigger the download
		// of the required language model (10 MB).
		LanguageDetection languageDetection = new LanguageDetection();
		languageDetection.initialize();

		// Update model
		model = languageDetection.process(model, datasetUri);

		// Write updated model into TURTLE file
		FileHandler.export(turtleOutputFile, model);
	}

	/**
	 * Creates geo data based on names of places that are found in the title and
	 * description of the specified dataset.
	 * 
	 * @param turtleInputFile  A TURTLE file to read
	 * @param turtleOutputFile A TURTLE file to write results
	 * @param datasetUri       A URI of a dcat:Dataset inside the TURTLE data
	 * 
	 * @see https://www.w3.org/TR/turtle/
	 * @see https://www.w3.org/TR/vocab-dcat/
	 */
	public void createGeoData(File turtleInputFile, File turtleOutputFile, String datasetUri) throws Exception {

		// Load TURTLE file into model
		Model model = FileHandler.importModel(turtleInputFile);

		// Update model
		model = new GeoData().process(model, datasetUri);

		// Write updated model into TURTLE file
		FileHandler.export(turtleOutputFile, model);
	}
}