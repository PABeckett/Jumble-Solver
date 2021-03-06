# Jumble-Solver
Solves the jumble puzzle found in many newspapers. Based on the format of the puzzle by David L. Hoyt and Jeff Knurek (all puzzle credit goes to them). For help setting up the solver, search "Hoyt Knurek Jumble" on the internet.

Feel free to use my code to solve jumble puzzles on your own, but you may not sell my code without my permission. If you reference any parts of this project academically, please reference appropriately or, if you are not allowed to reference any material, please turn back now.

v1.2 02/01/20

Packaged into runnable jar file which is found in the jumbleSolverApp folder. Should open from command line fine, opening with a double click might require registry editing (in registry editor app, locate jarfile/shell/open/command and change {\bin\javaw.exe" -jar "%1" %*} to {\bin\java.exe" -jar "%1" %*}. This will likely become unnecessary after a GUI is implemented.

To input a jumble puzzle, you will need to supply two pieces of information through the console:

  1) When prompted to enter scrambled words, enter them without being unscrambled and capitalize letters on squares which have an outline or background. For example, if the word is'pakln'  and the second and third characters are highlighted, the input for this word should be 'pAKln'.
  
  2) When prompted to enter an answer format, refer to the last set of boxes at the bottom. Enter a 1 for a box and a 0 for a space, taking care to include leading and trailing zeroes. For example, if the answer format is [][][] [][][][] [][][] then this should be represented as 01110111101110.

All operations take place in main for the time being with pseudo-exceptions causing exits => will continue to work towards a standalone executable. Consolidated english words text is also desirable.
