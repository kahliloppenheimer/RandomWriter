/**
 * TextAnalyzer.java
 *
 * Reads through a text file and both analyzes the document for various
 * statistics (average word length, average sentence length, # of unique
 * words, etc.) and generates random text based on the style of the
 * author of the source text file.
 *
 * @author Kahlil Oppenheimer
 *
 * @since May 12, 2013
 * */

package koppenheimer.randomwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class TextAnalyzer {

	private File sourceFile;
	private StringBuilder sourceText;
	private String generatedText;
	private Random rand;
	private int numChars; // total # of alphabetic and numeric chars
	private int numWords;
	private int numSentences; // sentence as defined by seperation by '.'
	private int numUniqueWords;

	private Set<String> uniqueWords; // number of different words used

	public TextAnalyzer(File f) throws FileNotFoundException {
		sourceFile = f;
		rand = new Random();
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
	 * Reads in a file, analyes the frequencies of characters, and returns
	 * randomly generated text based on the sourceText.
	 **/

	public String generateText(int numGenChars, int seedLength)
			throws FileNotFoundException {

		Scanner in = new Scanner(sourceFile);
		sourceText = new StringBuilder();

		while( in.hasNextLine() ) {
			String s = in.nextLine();
			sourceText.append(s);
			sourceText.append(' '); // new lines don't include spaces
		}
		in.close();

		/*Starts with random seed, then uses the last seedLength characters
          of the generated text as the next seed*/
		StringBuilder generatedText = new StringBuilder();
		String seed = getRandSeed(seedLength);
		generatedText.append(seed);

		for(int i = 0; i < numGenChars; i++) {
			char nextChar = getNextChar(seed);
			generatedText.append(nextChar);

			int genLength = generatedText.length();    
			int newSeedStart = genLength - seedLength + 1; 
			seed = generatedText.substring( newSeedStart, genLength );
		}
		String genTextString = generatedText.toString();
		return genTextString;
	}

	/**
	 * Takes a seed, searches the sourceText for every instance of that seed,
	 * then adds each character that immediately follows that seed into
	 * an ArrayList of Characters; the method then returns a random 
	 * character from that list.
	 **/
	private char getNextChar(String seed) {
		ArrayList<Character> key = new ArrayList<Character>();
		LinkedList<Character> temp = new LinkedList<Character>();
		int i = 0; //index
		int lastIndex = sourceText.lastIndexOf(seed);
		while(i < lastIndex) {
			/*char c becomes the character immediately following the next
              instance of the seed*/
			int afterSeedIndex = sourceText.indexOf(seed, i) + 
					seed.length();
			if(afterSeedIndex > i) i = afterSeedIndex;
			char c = sourceText.charAt(i);
			temp.add(c);
		}
		//copies all values from the linked list into the array list
		key.ensureCapacity(temp.size());
		for(char c: temp) {
			key.add(c);
		}
		char genChar;
		int keySize = key.size();
		if(keySize > 0) {
			genChar = key.get( rand.nextInt(key.size()) );
		}
		else {
			genChar = getNextChar( getRandSeed(seed.length()) );
		}
		return genChar;
	}

	/**
	 * Returns a random seed from the source text to be used to begin
	 * randomly generating text.
	 **/
	private String getRandSeed(int seedLength) {
		int randIndex = rand.nextInt(sourceText.length() - seedLength + 1);
		return sourceText.substring(randIndex, 
				randIndex + seedLength - 1);
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

	/**
	 * Returns a File object containing the source file
	 **/	
	public File getFile() {
		return sourceFile;
	}
}
