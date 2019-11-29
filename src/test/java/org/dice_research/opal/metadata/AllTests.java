package org.dice_research.opal.metadata;

import org.dice_research.opal.metadata.example.ExampleTest;
import org.dice_research.opal.metadata.lang.LangDetectorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Minimal working example
		ExampleTest.class,

		// LanguageDetection component
		LanguageDetectionTest.class,

		// LanguageDetection library
		LangDetectorTest.class })

public class AllTests {
}