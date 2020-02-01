package jumbleSolver;

import java.util.List;

import components.map.Map;
import components.map.MapOnHashTable;

import java.util.ArrayList;

/**
 * Circular array object that assists in permutation.
 * 
 * @author Parke Beckett 01/20
 */
public class wordRotor {
	String[] rep;
	int initPointer;
	int indivPointer;

	/**
	 * Constructor that defines backing string array and points initial pointer to a
	 * specific string.
	 * 
	 * @param wordsAtNum
	 */
	public wordRotor(List<String> wordsAtNum) {
		rep = new String[wordsAtNum.size()];
		for (String s : wordsAtNum) {
			rep[initPointer] = s;
			initPointer++;
		}
	}

	/**
	 * Wraps index around when it is large enough to cause an ArrayOutOfBouds
	 * exception.
	 * 
	 * @param n index to overflow
	 * @return index overflowed to avoid being out of bounds
	 */
	int safeOverflow(int n) {
		if (n == rep.length)
			n %= rep.length;
		return n;
	}

	/**
	 * Rotates circularly around limit of array.
	 * 
	 * @param outerCount how many units to rotate
	 * @return String at the new pointer
	 */
	public String rotateOverflow(int outerCount) {
		indivPointer += outerCount;
		if (indivPointer >= rep.length)
			indivPointer %= rep.length;

		return rep[indivPointer];
	}
}
