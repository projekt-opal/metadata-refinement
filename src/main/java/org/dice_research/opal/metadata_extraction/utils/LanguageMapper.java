package org.dice_research.opal.metadata_extraction.utils;

import java.util.HashMap;
import java.util.Map;

import org.dice_research.opal.metadata_extraction.fox.Fox;
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetector;

import opennlp.tools.langdetect.LanguageDetector;

/**
 * Maps language abbreviations of OpenNLP to FOX.
 * 
 * The {@link LanguageDetector} model is described at .
 * 
 * @author Adrian Wilke
 */
public abstract class LanguageMapper {

	private final static Map<String, String> languageMap = new HashMap<String, String>();

	static {
		languageMap.put(LangDetector.LANG_DEU, Fox.LANG_DE);
		languageMap.put(LangDetector.LANG_ENG, Fox.LANG_EN);
		languageMap.put(LangDetector.LANG_FRA, Fox.LANG_FR);
		languageMap.put(LangDetector.LANG_SPA, Fox.LANG_ES);
	}

	/**
	 * Gets FOX key for given OpenNLP key.
	 * 
	 * @return the language key to which the specified key is mapped, or
	 *         {@code null} if there is no mapping for the key.
	 */
	public static String opennlpToFox(String languageKey) {
		return languageMap.get(languageKey);
	}
}
