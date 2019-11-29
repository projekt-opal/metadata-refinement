package org.dice_research.opal.metadata.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.Test;

/**
 * Minimal working example.
 *
 * @author Adrian Wilke
 */
public class ExampleTest {

	@Test
	public void test() throws Exception {

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

}