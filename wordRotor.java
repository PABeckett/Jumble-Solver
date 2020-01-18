package jumbleSolver;

import java.util.List;
import java.util.ArrayList;

//simply the rotor, still need an array

public class wordRotor {
	String[] rep;
	int initPointer;
	int indivPointer;
	
	public wordRotor(List<String> wordsAtNum) {
		rep = new String[wordsAtNum.size()];
		for(String s : wordsAtNum) {
			rep[initPointer] = s;
			initPointer++;
		}
	}
	
	int safeOverflow(int n) {
		if(n == rep.length)
			n %= rep.length;
		return n;
	}
	
	public String rotateOverflow(int outerCount) {
		indivPointer += outerCount;
		if(indivPointer >= rep.length)
			indivPointer %= rep.length;
		
		return rep[indivPointer];
	}
}
