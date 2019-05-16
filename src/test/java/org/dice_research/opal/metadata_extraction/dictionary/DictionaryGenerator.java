package org.dice_research.opal.metadata_extraction.dictionary;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import opennlp.tools.langdetect.Language;

/**
 * Creates a word list of typical lower case words.
 * 
 * @see Language files: http://www.wortschatz.uni-leipzig.de/de/download
 * 
 * @author Adrian Wilke
 */
public class DictionaryGenerator {

	public static final String DEU_100K = "/tmp/deu_mixed-typical_2011_100K-words.txt";
	public static final String GERMAN_LOWER_CASE = "/tmp/german-lowercase-words.txt";

	public static void main(String[] args) throws Exception {
		File dictionary = new File(DEU_100K);
		if (!dictionary.canRead()) {
			System.err.println("Can not read file " + dictionary.getAbsolutePath());
			System.exit(1);
		} else {
			new DictionaryGenerator().generate(dictionary);
		}
	}

	public void generate(File dictionary) throws Exception {
		List<String> lines = FileUtils.readLines(dictionary, StandardCharsets.UTF_8);
		List<String> words = new LinkedList<String>();
		for (int i = 0; i < lines.size(); i++) {
			String word = lines.get(i).split("\t")[1];

			// Remove words containing non-letters
			// \p{L} or \p{Letter}: any kind of letter from any language.
			// https://www.regular-expressions.info/unicode.html#category
			if (!word.equals(word.replaceAll("[^\\p{L}]+", ""))) {
				continue;
			}

			// Only lower case words
			if (word.substring(0, 1).toUpperCase().equals(word.substring(0, 1))) {
				continue;
			}

			words.add(word);
		}

		for (String word : words) {
			System.out.println(word);
		}

		System.out.println(words.size());

		FileUtils.writeLines(new File(GERMAN_LOWER_CASE), words);
	}

}