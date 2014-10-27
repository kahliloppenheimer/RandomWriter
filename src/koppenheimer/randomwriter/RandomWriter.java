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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class RandomWriter {

	private final File sourceFile;
	private StringBuilder sourceText;
	private static final Random rand = new Random();
	private Map<String, Character> cMap;

	public RandomWriter(File f) throws IOException {
		sourceFile = f;
		cMap = new HashMap<String, Character>();
	}
	
	/**
	 * Reads a file into a String and sends it along to buildCharacterMap()
	 * 
	 * @param f File to be read/sent along
	 * @throws IOException
	 */
	private String loadCharacterMap(File f) throws IOException {
		if(!f.canRead()) {
			throw new IOException("Can't read file!");
		}
		// Read file into String then send it along to buildCharacterMap()
		BufferedReader reader = new BufferedReader(new FileReader(f));
		StringBuilder builder = new StringBuilder();
		String nextLine = null;
		while((nextLine = reader.readLine()) != null) {
			builder.append(nextLine);
		}
		reader.close();
		return builder.toString();
	}
	
	/**
	 * Goes through a String and pre-computes the map of all strings of length seedLength
	 * to 
	 * @param text The source text that the random generation will derive from
	 * @param seedLength The specified length of string that will be analyzed
	 */
	private void buildCharacterMap(String text, int seedLength) {
		preProcess(text);
		
	}
	
	/**
	 * Goes through a StringBuilder and cleans out some detritus (i.e. white space)
	 * @param builder
	 */
	private void preProcess(String text) {
		// Replace all white space characters with a single space
		text.replaceAll("\\s+", " ");
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
	 * Returns a File object containing the source file
	 **/	
	public File getFile() {
		return sourceFile;
	}
}
