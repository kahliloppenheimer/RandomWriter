package koppenheimer.randomwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TextAnalyzer {
	
	private File sourceFile;
	
	private int numChars; // total # of alphabetic and numeric chars
	private int numWords;
	private int numSentences; // sentence as defined by seperation by '.'
	private int numUniqueWords;
	
	public TextAnalyzer(File f) throws FileNotFoundException {
		sourceFile = f;
		analyze();
	}
	
	/**
	 * Determines the number of sentences, words, and characters in the
	 * document, as well as the number of unique words.
	 **/
	private void analyze() throws FileNotFoundException {
		Scanner in = new Scanner(sourceFile);
		Set<String> wordSet = new HashSet<String>();
		while(in.hasNext()) {
			String s = in.next().toLowerCase();
			wordSet.add(onlyLetters(s));
			if(s.charAt(s.length() - 1) == '.') numSentences++;
			for(int i = 0; i < s.length(); i++) {
				if(Character.isLetterOrDigit( s.charAt(i) )) {
					numChars++;
				}
			}
			numWords++;
		}
		numUniqueWords = wordSet.size();
		in.close();
	}
	
	/**
	 * Takes a string and returns only its alphabetic characters
	 * (no punctuation).
	 **/
	private String onlyLetters(String s) {
		String temp = "";
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(Character.isLetterOrDigit( c )) {
				temp += c;
			}
		}
		return temp;
	}
	
	/**
	 * Returns the total number of sentences in the source text.
	 **/
	public int getNumSentences() {
		return numSentences;
	}

	/**
	 * Returns the total number of words in the source text.
	 **/
	public int getNumWords() {
		return numWords;
	}

	/**
	 * Returns the total number of characters in the source text.
	 **/
	public int getNumChars() {
		return numChars;
	}

	/**
	 * Returns the total number of unique words (size of vocabulary)
	 * in the source text.
	 **/
	public int getNumUniqueWords() {
		return numUniqueWords;
	}


}
