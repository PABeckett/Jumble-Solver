# Jumble-Solver
Solves the jumble puzzle found in many newspapers. Based on the format of the puzzle by David L. Hoyt and Jeff Knurek (all puzzle credit goes to them). For help setting up the solver, search "Hoyt Knurek Jumble" on the internet.

Feel free to use my code to solve jumble puzzles on your own, but you may not sell my code without my permission. If you reference any parts of this project academically, please reference appropriately or, if you are not allowed to reference any material, please turn back now.

Add components.jar to the build path an as external JAR.
Exception will be thrown if "words_english.txt" file is not accessible to the class. This file or an equivalent english dictionary is crucial to the operation of this class.
Exception will be thrown initially is "jumbleValidPartOne" is not accessible. This is a 'helper file' that restricts the available unscrambling words to unique anagrams of size either five or six. Since this is an unofficial rule of the jumble puzzle, no exceptions or errors are likely to surface. Using this file is recommended as it improves performance, but it can be commented out in favor of the anaMap field and associated methods. This provides the advantage of relying on a single file class-wide. If this route is preferred, take care to address the unscramble() method at line 143.

v1.1 01/31/20

Running the jumbleExec.java file will execute the code. wordRotor.java is a helper class that is needed but is not executable on its own. All classes were made in Eclipse, so the reader may wish to comment out package names if they are using a different IDE.

To input a jumble puzzle, you will need to supply two pieces of information through the console:

  1) When prompted to enter scrambled words, enter them without being unscrambled and capitalize letters on squares which have an outline or background. For example, if the word is'pakln'  and the second and third characters are highlighted, the input for this word should be 'pAKln'.
  
  2) When prompted to enter an answer format, refer to the last set of boxes at the bottom. Enter a 1 for a box and a 0 for a space, taking care to include leading and trailing zeroes. For example, if the answer format is [][][] [][][][] [][][] then this should be represented as 01110111101110.
  
  Consult the supplied data file "jumbleInputReference" for more clarification.
  
  
All operations take place in main for the time being with pseudo-exceptions causing exits => will continue to work towards a standalone executable. Consolidated english words text is also desirable.
