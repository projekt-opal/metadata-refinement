# OPAL metadata refinement

* **Language Detection** based on [Apache OpenNLP](https://opennlp.apache.org/)


## Example

```Java
import java.io.File;
import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.common.utilities.FileHandler;
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
}

```



## Note

Version 1 can be found at [branch metadata-version-1](https://github.com/projekt-opal/metadata-extraction/tree/metadata-version-1).



## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/)

This work has been supported by the German Federal Ministry of Transport and Digital Infrastructure (BMVI) in the project [Open Data Portal Germany (OPAL)](http://projekt-opal.de/) (funding code 19F2028A).