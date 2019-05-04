package org.dice_research.opal.metadata_extraction.suites;

import org.dice_research.opal.metadata_extraction.FoxExtractionTest;
import org.dice_research.opal.metadata_extraction.LangDetectorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		FoxExtractionTest.class,

		LangDetectorTest.class })

public class LocalTests {
}