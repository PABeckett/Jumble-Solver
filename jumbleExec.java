package jumbleSolver;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import components.map.Map;
import components.map.MapOnHashTable;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;

/**
 * JumbleSolver now operates in the console and only depends on the english
 * words file. A second file can be used to speed it up, but no more input files
 * are needed.
 * 
 * This class solves the Jumble puzzle by David Hoyt and Rob Knurek. Inputs must
 * take the following form:
 * 
 * When prompted for four jumbled words, enter them as you see in the puzzle yet
 * be sure to capitalize letters which you see in boxes that are outlined or
 * circled.
 * 
 * When prompted for an answer format, enter a 1 where a word has a letter and a
 * 0 where there is no letter. For example, an input of [][][] [][][][]
 * [][][][], three boxes followed by four and then another four, is represented
 * as 011101111011110. Note the leading and trailing zeroes.
 * 
 * Hyphenated puzzles tend to be puns and wordplay so this class performs poorly
 * when solving them.
 * 
 * More restrictive english words would be ideal.
 * 
 * @author Parker Beckett, 01/2020 -> v1.1
 */
public class jumbleExec {

	static wordRotor[] words;
	final static HashSet<String> wordMap = buildTotalMap("data/words_english.txt");
	static Map<Integer, List<String>> trimmedMap = new MapOnHashTable<Integer, List<String>>();
	static List<ArrayList<String>> solnList = new ArrayList<ArrayList<String>>();
	static List<Character> availChar = new ArrayList<Character>();
	static Map<List<Character>, String> cheapMap = shortCutMap("data/jumbleValidPartOne");
	static List<String> inputWords = new ArrayList<String>();
	static List<String> charsToUse = new ArrayList<String>();
	static String answerFormat;

//	final static Map<List<Character>, String> anaMap = buildUniqueMap("data/words_english.txt");
//	/**
//	 * Initializes a map of words with no anagrams, used to unscramble initial
//	 * words. Commented out to avoid conflict with shortcutMap.
//	 * 
//	 * @param fileIn a file containing all words in the english language
//	 * @return a map populated only by words which have no anagrams
//	 */
//	public static Map<List<Character>, String> buildUniqueMap(String fileIn) {
//		HashSet<List<Character>> dupSet = new HashSet<List<Character>>();
//		Map<List<Character>, String> interDict = new MapOnHashTable<List<Character>, String>();
//		SimpleReader textIn = new SimpleReader1L(fileIn);
//		while (!textIn.atEOS()) {
//			String word = textIn.nextLine();
//			List<Character> chars = insertionSort(word);
//			if (!interDict.hasKey(chars) && !dupSet.contains(chars)) {
//				if (chars.size() > 4 && chars.size() < 7)
//					interDict.add(chars, word);
//				dupSet.add(chars);
//			} else if (interDict.hasKey(chars)) {
//				interDict.remove(chars);
//			}
//		}
//		textIn.close();
//		return interDict;
//	}

	/**
	 * Makes use of the fact that jumble words are only ever five or six words long
	 * and only have one anagram to take a shortcut to the map needed in part one.
	 * 
	 * @param fileIn a file containing english words that have no anagrams and
	 *               either five or six letters long
	 * @return a map populated only by valid jumble words, saving time on map
	 *         construction
	 */
	public static Map<List<Character>, String> shortCutMap(String fileIn) {
		Map<List<Character>, String> interDict = new MapOnHashTable<List<Character>, String>();
		SimpleReader read = new SimpleReader1L(fileIn);
		while (!read.atEOS()) {
			String str = read.nextLine();
			interDict.add(insertionSort(str), str);
		}
		read.close();
		return interDict;
	}

	/**
	 * Initializes the entire english dictionary file as a map for easy access.
	 * 
	 * @param fileIn a file containing all words in the english language
	 * @return a map populated by every english word
	 */
	public static HashSet<String> buildTotalMap(String fileIn) {
		HashSet<String> interSet = new HashSet<String>();
		SimpleReader read = new SimpleReader1L(fileIn);
		while (!read.atEOS()) {
			String word = read.nextLine();
			interSet.add(word);
		}
		read.close();
		return interSet;
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
		if (cheapMap.hasKey(chars))
			return cheapMap.value(chars);
//		if (anaMap.hasKey(chars))
//			return anaMap.value(chars);
		String error = "";
		for (int i = 0; i < word.length(); i++)
			error += 'x';
		return error;
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
	public static List<Character> getAvailChar(List<String> unscrambled, List<String> pattern) {
		List<Character> availChar = new ArrayList<Character>();
		for (int i = 0; i < unscrambled.size(); i++) {
			for (int j = 0; j < unscrambled.get(i).length(); j++) {
				if (pattern.get(i).charAt(j) == '1')
					availChar.add(unscrambled.get(i).charAt(j));
			}
		}
		return availChar;
	}

	/**
	 * Solves part one (unscrambling the four initial words) while also initializing
	 * instance variables necessary to solving part two (ascertaining characters).
	 * 
	 * @param fileIn  a file containing words to be unscrambled
	 * @param fileOut a file containing the unscrambled words
	 */
	public static List<String> solvePartOne() {
		List<String> unscrambled = new ArrayList<String>();
		for (String word : inputWords) {
			String soln = unscramble(word);
			unscrambled.add(soln);
		}
		availChar = getAvailChar(unscrambled, charsToUse);
		return unscrambled;
	}

	/**
	 * Eliminates words from the map of English words that contain a character that
	 * is not allowed. Also organizes words by their size for easier permutation.
	 * 
	 * @param chars a list of characters which defines the new map
	 * @return a map only containing words which do not contain any characters that
	 *         are not defined by the parameter
	 */
	public static Map<Integer, List<String>> trimChars(HashSet<Character> chars) {
		Map<Integer, List<String>> solnMap = new MapOnHashTable<Integer, List<String>>();
		for (String s : wordMap) {
			boolean validPair = true;
			String str = s;
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
	 * Determines the structure of the solution sentence (how many words, length of
	 * each word). Uses a user-defined field.
	 *
	 * @return an integer list that represents the words of the solution, with the
	 *         value at each index corresponding to the length of that word
	 */
	public static List<Integer> sentenceStruct() {
		List<Integer> sentForm = new ArrayList<Integer>();
		int counter = 0;
		for (int i = 0; i < answerFormat.length(); i++) {
			char c = answerFormat.charAt(i);
			if (c == '1')
				counter++;
			else if (counter != 0) {
				sentForm.add(counter);
				counter = 0;
			}
		}
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
	 * @param fileIn  the answer format for the puzzle
	 * @param fileOut a list of potential solutions
	 */
	public static List<ArrayList<String>> solvePartTwo() {
		List<Integer> format = sentenceStruct();
		HashSet<Character> availChars = new HashSet<Character>();
		for (Character c : availChar)
			availChars.add(c);
		trimChars(availChars);
		List<ArrayList<String>> wordsToPerm = validWords(format);
		constructSentRotor(wordsToPerm);
		permuteRecurse(0, words);
		return solnList;
	}

	/**
	 * Driver function to solve puzzle and time algorithms.
	 * 
	 * @param args N/A
	 */
	public static void main(String[] args) {
		Scanner readIn = new Scanner(System.in);
		System.out.println("ENTER THE FOUR JUMBLED WORDS" + "\n" + "WITH CAPITALS AT THE CIRCLED SQUARES" + "\n");
		for (int i = 0; i < 4; i++) {
			StringBuilder s = new StringBuilder();
			String str = readIn.nextLine();
			inputWords.add(str.toLowerCase());
			for (int j = 0; j < str.length(); j++) {
				Character c = str.charAt(j);
				if (c.compareTo('a') < 0) {
					s.append('1');
				} else
					s.append('0');
			}
			charsToUse.add(s.toString());
		}
		System.out.println("\n" + "working..." + "\n");
		long startOne = System.nanoTime();

		List<String> list = solvePartOne();
		System.out.println(list);
		long partOneDone = System.nanoTime() - startOne;
		for (String s : list)
			if (s.charAt(1) == 'x' && s.charAt(2) == 'x') {
				System.out.println("\n" + "JUMBLE WORDS MUST BE ANAGRAMS" + "\n" + "\n" + "exiting...");
				readIn.close();
				return;
			}
		System.out.println("\n" + "ENTER THE ANSWER FORMAT" + "\n");
		answerFormat = readIn.nextLine();
		System.out.println("\n" + "working..." + "\n");
		long startTwo = System.nanoTime();
		List<ArrayList<String>> solnList = (solvePartTwo());
		long partTwoDone = System.nanoTime() - startTwo;
		if (solnList.size() == 0) {
			System.out.println("\n" + "SOLUTION MUST BE MADE UP OF PROPER WORDS" + "\n" + "exiting...");

		}
		for (ArrayList<String> printList : solnList)
			System.out.println(printList);
		readIn.close();
		System.out.println("part One mS: " + partOneDone / (double) 1000000 + "\n" + "part Two mS: "
				+ partTwoDone / (double) 1000000);
	}
}