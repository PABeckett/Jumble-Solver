# Jumble-Solver
Solves the jumble puzzle found in many newspapers. Based on the format of the puzzle by David L. Hoyt and Jeff Knurek (all puzzle credit goes to them). For help setting up the solver, search "Hoyt Knurek Jumble" on the internet.

Feel free to use my code to solve jumble puzzles on your own, but you may not sell my code without my permission. If you reference any parts of this project academically, please reference appropriately or, if you are not allowed to reference any material, please turn back now.

Add components.jar to the build path an as external JAR.

//userInput soon

Running the jumbleExec.java file will execute the code. wordRotor.java is a helper class that is needed but is not executable on its own. All classes were made in Eclipse, so the reader may wish to comment out package names if they are using a different IDE.

To input a jumble puzzle, you will need to edit three files, all of which can be found in the 'data' directory:

  1) jumbleInputList should contain strings of the scrambled words, separated by new lines.
  
  2) jumbleCharsToChoose should contain binary sequences that indicate whether the character of an unscrambled word is circled. For example, if the first unscrambled word is "fidtr" and the characters circled are f, i, and r, the first line in jumbleCharsToChoose should be "11001".
  
  3) jumbleAnswerFormat should contain a single line of binary digits, with leading and trailing 0s, to represent the sentence structure of the final solution. A 1 should be used if there is a character in the position, and a 0 should be  used to denote spaces. For example, if the final answer is "[][][][] [][][][][][][]" this is a four letter word followed by a seven letter word. This should be represented as "01111011111110".

The output will be a file (called JumblePartTwoSoln in the 'data' folder) with all possible solutions, so it will be up to the reader to decide which solution matches well with the picture hint provided.
