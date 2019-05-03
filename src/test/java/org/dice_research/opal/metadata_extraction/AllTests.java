package org.dice_research.opal.metadata_extraction;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FoxExtractionTest.class, FoxRemoteTest.class, LangDetectorTest.class })
public class AllTests {
}