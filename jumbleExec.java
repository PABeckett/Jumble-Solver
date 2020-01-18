package jumbleSolver;

import java.util.ArrayList;
import java.util.List;
import components.map.Map;
import components.map.MapOnHashTable;
import components.map.Map.Pair;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This class solves the jumble puzzle provided in many newspapers. The user
 * must edit three files, "jumbleInputList" (scrambled words) "jumbleCharsToUse"
 * (characters which will becom final sentence) and "jumbleAnswerFormat" (the
 * amount of words/letters per word in the final sentence.) The latter two
 * should be only 1's and 0's, with a 1 meaning 'use this character' or 'there
 * is a character here' while 0 means 'don't use this character' or 'there is a
 * space here'.
 * 
 * @author Parker Beckett, 01/2020
 */
public class jumbleExec {

	static wordRotor[] words;
	static boolean partOneError = false;
	static Map<List<Character>, String> uniqueAna = new MapOnHashTable<List<Character>, String>();
	final static Map<List<Character>, String> partOneSolnMap = buildUniqueMap("data/words_english.txt");
	final static Map<String, Integer> wordMap = buildTotalMap("data/words_english.txt");
	final static Map<Integer, List<String>> partTwoMap = new MapOnHashTable<Integer, List<String>>();
	static Map<Integer, List<String>> trimmedMap = new MapOnHashTable<Integer, List<String>>();
	static List<ArrayList<String>> solnList = new ArrayList<ArrayList<String>>();
	static List<Character> availChar = new ArrayList<Character>();

	/**
	 * Initializes a map of words with no anagrams, used to unscramble initial
	 * words.
	 * 
	 * @param fileIn a file containing all words in the english language
	 * @return a map populated only by words which have no anagrams
	 */
	public static Map<List<Character>, String> buildUniqueMap(String fileIn) {
		Map<List<Character>, String> interDict = new MapOnHashTable<List<Character>, String>();
		SimpleReader textIn = new SimpleReader1L(fileIn);
		while (!textIn.atEOS()) {
			String word = textIn.nextLine();
			List<Character> chars = insertionSort(word);
			if (!interDict.hasKey(chars))
				interDict.add(chars, word);
			else {
				interDict.remove(chars);
			}
		}
		textIn.close();
		return interDict;
	}

	/**
	 * Initializes the entire english dictionary file as a map for easy access.
	 * 
	 * @param fileIn a file containing all words in the english language
	 * @return a map populated by every english word
	 */
	public static Map<String, Integer> buildTotalMap(String fileIn) {
		Map<String, Integer> interDict = new MapOnHashTable<String, Integer>();
		SimpleReader read = new SimpleReader1L(fileIn);
		int count = 0;
		while (!read.atEOS()) {
			String word = read.nextLine();
			interDict.add(word, count);
			count++;
		}
		read.close();
		return interDict;
	}

	/**
	 * Helper method that sorts a string and returns a sorted list of characters.
	 * 
	 * @param word a string whose characters are to be sorted
	 * @return a list of characters that represents its sorted state
	 */
	public static List<Character> insertionSort(String word) {
		List<Character> chars = new ArrayList<Character>();
		for (int j = 0; j < word.length(); j++) {
			chars.add(word.charAt(j));
		}
		for (int i = 0; i < chars.size(); i++) {
			Character sorted = chars.get(i);
			int j = i - 1;
			while (j >= 0 && chars.get(j).compareTo(sorted) > 0) {
				chars.set(j + 1, chars.get(j));
				j = j - 1;
			}
			chars.set(j + 1, sorted);
		}
		return chars;
	}

	/**
	 * Unscrambles words with help from insertionSort and the map of words with no
	 * anagrams.
	 * 
	 * @param word a word to be unscrambled
	 * @return any matches found in the unique map, or "more than one solution" if
	 *         the word has any anagrams
	 */
	public static String unscramble(String word) {
		List<Character> chars = insertionSort(word);
		if (partOneSolnMap.hasKey(chars))
			return partOneSolnMap.value(chars);
		return "more than one solution";
	}

	/**
	 * Determines which characters can be used to construct the final sentence.
	 * 
	 * @param fileWords   a file denoting the unscrambled words
	 * @param filePattern a file denoting the characters that should be used from
	 *                    each word
	 * @return a list of characters that encompasses each character used in the
	 *         solution
	 */
	public static List<Character> getAvailChar(String fileWords, String filePattern) {
		List<Character> availChar = new ArrayList<Character>();
		SimpleReader readWords = new SimpleReader1L(fileWords);
		SimpleReader readPattern = new SimpleReader1L(filePattern);
		while (!readWords.atEOS()) {
			String word = readWords.nextLine();
			String pattern = readPattern.nextLine();
			for (int i = 0; i < pattern.length(); i++)
				if (pattern.charAt(i) == '1')
					availChar.add(word.charAt(i));
		}
		readWords.close();
		readPattern.close();
		return availChar;
	}

	/**
	 * Solves part one (unscrambling the four initial words) while also initializing
	 * instance variables necessary to solving part two (constructing solution
	 * sentence).
	 * 
	 * @param fileIn  a file containing words to be unscrambled
	 * @param fileOut a file containing the unscrambled words
	 */
	public static void solvePartOne(String fileIn, String fileOut) {
		SimpleReader read = new SimpleReader1L(fileIn);
		SimpleWriter write = new SimpleWriter1L(fileOut);
		while (!read.atEOS()) {
			String word = read.nextLine();
			String soln = unscramble(word);
			if (soln.equals("more than one solution"))
				partOneError = true;
			write.println(soln);
		}
		List<Character> chars = getAvailChar("data/jumblePartOneSolns", "data/jumbleCharsToUse");
		read.close();
		write.close();
		availChar = chars;
	}

	/**
	 * Eliminates words from the map of English words that contain a character that
	 * is not allowed.
	 * 
	 * @param chars a list of characters which defines the new map
	 * @return a map only contatining words which do not contain any characters that
	 *         are not defined by the parameter
	 */
	public static Map<Integer, List<String>> trimChars(List<Character> chars) {
		Map<Integer, List<String>> solnMap = new MapOnHashTable<Integer, List<String>>();
		for (Pair<String, Integer> pair : wordMap) {
			boolean validPair = true;
			String str = pair.key();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (!chars.contains(c)) {
					validPair = false;
				}
			}
			if (validPair) {
				if (solnMap.hasKey(str.length())) {
					if (!solnMap.value(str.length()).contains(str))
						solnMap.value(str.length()).add(str);
				} else {
					List<String> wordsOfSize = new ArrayList<String>();
					wordsOfSize.add(str);
					solnMap.add(str.length(), wordsOfSize);
				}
			}
		}
		trimmedMap = solnMap;
		return solnMap;
	}

	/**
	 * Uses a user-supplied file to determine the structure of the solution sentence
	 * (how many words, length of each word).
	 * 
	 * @param layoutFile a file denoting the amount of words in the solution as well
	 *                   as the number of characters per word
	 * @return an integer list that represents the words of the solution, with the
	 *         value at each index corresponding to the length of that word
	 */
	public static List<Integer> sentenceStruct(String layoutFile) {
		List<Integer> sentForm = new ArrayList<Integer>();
		SimpleReader findWords = new SimpleReader1L(layoutFile);
		String format = findWords.nextLine();
		int counter = 0;
		for (int i = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '1')
				counter++;
			else if (counter != 0) {
				sentForm.add(counter);
				counter = 0;
			}
		}
		findWords.close();
		return sentForm;
	}

	/**
	 * Determines valid words that can make up solution with help of map.
	 * 
	 * @param format a list that represents the words of the solution, with the
	 *               value at each index corresponding to the length of that word
	 * @return a list of lists that defines possible words that could be part of the
	 *         solution. each list contains valid words of a particular size
	 */
	public static List<ArrayList<String>> validWords(List<Integer> format) {
		List<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < format.size(); i++) {
			int num = format.get(i);
			List<String> inner = new ArrayList<String>();
			if (trimmedMap.hasKey(num)) {
				inner = trimmedMap.value(num);
			}
			outer.add((ArrayList<String>) inner);
		}
		return outer;
	}

	/**
	 * Simple method that converts a list of characters to a String without any
	 * potential unwanted effects that result from the default List.toString().
	 * 
	 * @param chars a word in the form of a list of characters
	 * @return a string representation of a list of characters
	 */
	public static String listToString(List<Character> chars) {
		StringBuilder s = new StringBuilder();
		for (Character c : chars)
			s.append(c);
		return s.toString();
	}

	/**
	 * Constructs rotor structures to make the recursive permutation more efficient
	 * and predictable.
	 * 
	 * @param trimList a list of valid words with which to construct a possible
	 *                 sentence solution
	 */
	public static void constructSentRotor(List<ArrayList<String>> trimList) {
		words = new wordRotor[trimList.size()];
		for (int i = 0; i < trimList.size(); i++) {
			wordRotor word = new wordRotor(trimList.get(i));
			words[i] = word;
		}
	}

	/**
	 * Recursively tries every combination of valid words and records this
	 * combination as valid if there are no extra characters / none missing.
	 * 
	 * @param pointer a recursive assistant that ensures all combinations of words
	 *                are explored
	 * @param words   an array of wordRotor objects that is cycled through
	 * 
	 */
	public static void permuteRecurse(int pointer, wordRotor[] words) {
		ArrayList<String> chars = new ArrayList<String>();
		for (wordRotor wR : words) {
			chars.add(wR.rep[wR.indivPointer]);
		}
		if (testCharPartition(chars) && !solnList.contains(chars)) {
			solnList.add(chars);
			return;
		}
		if (pointer == words.length)
			return;
		else {
			for (int i = 0; i < words[pointer].initPointer; i++) {
				words[pointer].rotateOverflow(1);
				permuteRecurse(pointer + 1, words);
			}
		}
	}

	/**
	 * Tests validity of a particular word combination, particularly that each
	 * character in the list of available characters is used exactly once.
	 * 
	 * @param soln a list of strings that is a potential solution
	 * @return true if the parameter uses every character exactly once
	 */
	public static boolean testCharPartition(List<String> soln) {
		String availCharStr = listToString(availChar);
		List<Character> sortKey = insertionSort(availCharStr);
		StringBuilder s = new StringBuilder();
		for (String solnStr : soln) {
			s.append(solnStr);
		}
		List<Character> toTest = insertionSort(s.toString());
		return (toTest.equals(sortKey));
	}

	/**
	 * Solves part two (reconstructing final answer sentence).
	 * 
	 * @param fileIn the answer format for the puzzle
	 * @param fileOut a list of potential solutions
	 */
	public static void solvePartTwo(String fileIn, String fileOut) {
		List<Integer> format = sentenceStruct(fileIn);
		trimChars(availChar);
		List<ArrayList<String>> wordsToPerm = validWords(format);
		constructSentRotor(wordsToPerm);
		permuteRecurse(0, words);
		SimpleWriter out = new SimpleWriter1L(fileOut);
		for (ArrayList<String> s : solnList)
			out.println(s);
		out.close();
		return;
	}

	/**
	 * Driver function to solve puzzle and time algorithms
	 * 
	 * @param args N/A
	 */
	public static void main(String[] args) {
		long startOne = System.nanoTime();
		solvePartOne("data/jumbleInputList", "data/jumblePartOneSolns");
		long partOneDone = System.nanoTime() - startOne;
		long startTwo = System.nanoTime();
		solvePartTwo("data/jumbleAnswerFormat", "data/jumblePartTwoSoln");
		long partTwoDone = System.nanoTime() - startTwo;
		System.out.println("part One mS: " + partOneDone / (double)1000000 + "\n" + "part Two mS: " + partTwoDone / (double)1000000);
	}
}