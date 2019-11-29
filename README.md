# OPAL metadata refinement

* **Language Detection** based on [Apache OpenNLP](https://opennlp.apache.org/)


## Usage with Apache Maven

Add the following lines to your `pom.xml` configuration file:

	<dependencies>
		<dependency>
			<groupId>org.dice-research.opal</groupId>
			<artifactId>metadata-refinement</artifactId>
			<version>[1,2)</version>
		</dependency>
	</dependencies>
	
	<repositories>
		<repository>
			<id>maven.aksw.internal</id>
			<name>AKSW Repository</name>
			<url>http://maven.aksw.org/archiva/repository/internal</url>
		</repository>
		<repository>
			<id>maven.aksw.snapshots</id>
			<name>AKSW Snapshot Repository</name>
			<url>http://maven.aksw.org/archiva/repository/snapshots</url>
		</repository>
	</repositories>
	
Available versions are listed at [maven.aksw.org](https://maven.aksw.org/archiva/#advancedsearch~internal/org.dice-research.opal~metadata-refinement~~~~~30).

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
It includes 
Language Detection based on Apache OpenNLP,
Named Entity Recognition based on FOX, and
a JavaScript word picker
as well as configurations for Docker usage and webservices.


## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/)

This work has been supported by the German Federal Ministry of Transport and Digital Infrastructure (BMVI) in the project [Open Data Portal Germany (OPAL)](http://projekt-opal.de/) (funding code 19F2028A).
