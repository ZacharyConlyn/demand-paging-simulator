# Demand Paging Simulation in Java
Final project for CMSC 412, December 2016.
Written by Zachary Conlyn.
## Introduction & Overview
Specifications:
- Between 1 and 7 physical frames, numbered from 0 to N - 1, which are to be read in as a command line argument

- 10 virtual frames, numbered from 0 to 9

- Simulation of FIFO, OPT, LRU and LFU page replacement schemes

- Reference strings (i.e. strings representing the virtual frames being called) to be entered manually or via random generation

- menu-based UI with robust error handling

## Design & Implementation
### Input validation and error handling
The first task of the program is to read in the command line argument and validate it. The method readArgs() checks the input. If there were no arguments entered, there is an error and the program exits. If it is more, a warning is printed that other arguments were ignored. If the input is not an integer, the program catches it as a NumberFormatException from Integer.ParseInt() and exits. If the integer is not between 1 and 7, the program prints an error that the required number of physical pages must be between 1 and 7 (the upper limit is controlled by the P_PG constant in the program and can be changed).
Following argument validation, the next step is to declare some variables (e.g. Scanner) that will be used in the main loop of the program, then enter the main loop, written as a "while (true)" loop. Input is read and processed in a switch statement. If the input matches one of the menu options (0 to 7), that option is carried out. If not, the input falls to the default case, which prints that the input was not understood, and the loop continues.

### Main loop
#### Case 0: Exit.
A goodbye message is printed to console and System.exit(0) is called.

#### Case 1: Read a reference string.
readRefString is called, which takes an single line of input, uses next() to read in each token, and validates it as being an integer between 0 and 9 (again, the upper limit is controlled by the constant V_PG which can be altered). If a token is not a valid integer, a warning is printed that that token was ignored. Each valid integer entered is added to an ArrayList<Int>. If 0 valid integers are entered, the user is prompted to re-enter a valid reference string. When one line has been entered that contains at least one valid integer, the ArrayList<Int> created is passed into a new RefString object, which is returned to the calling method (in this case, main()). stringConfirm() prints out each integer in the entered reference string for the user to verify.

#### Case 2: Generate a reference string.
The user is prompted to enter a length for the reference string. This input is handled/validated by getStringSize(), which prompts the user in a loop until they enter a positive integer, then returns it. This is then used as an input to generateString() along with V_PG, which creates an ArrayList<Int> of the specified length with ints between 0 and V_PG - 1, uses that to create a RefString object, and then returns that object to main(). stringConfirm() prints out the resulting reference string.

#### Case 3: Print reference string.
If no reference string has been entered, an error is printed and the main loop continues. If one has been entered, stringConfirm() prints it to stdout.

#### Cases 4, 5, 6, 7: Generate FIFO, OPT, LRU, LFU page simulations.
In each case, the program ensures that a reference string has been entered. If it hasn't, an error is printed and the loop continues. Otherwise, a new MemorySim object is created with the reference string and information about the number of physical and virtual pages. Then the MemorySim's generate() method is called for the appropriate algorithm.

This is the heart of the program; the simulation is actually run and the results stored inside the MemorySim object. MemorySim works together with the Frame class. The Frame class holds information about each virtual frame needed to implement the algorithms, including when the frame was inserted into physical memory, when it was last used, when it will be next used, and how many times it has been used total. MemorySim sets and gets this information as needed with various helper methods.

When generate() is called, a helper method, initialize(), sets everything to a pristine state. Variables are made: currentSlice (current spot in the simulation, starts at 0), frameToInsert, empty (will store the index of empty physical memory slots or -1 if physical memory is full), frameToReplace (holds index of frame that has to be removed), and listOfFrames[] (an array of ints that is sent to the various page replacement methods to find the correct frame to remove).

Generate steps through the simulation, from 0 to the end of the reference string. It finds the frame to insert, and updates the "last use" to the current slice if it is LRU. Or, if it is LFU, it increments the times the frame being called has been used. Then, generate() checks if the required virtual frame is already in physical memory. If not, it checks if there is an empty physical slot. If physical memory is "full", it calls the correct algorithm's methods (the algorithm is specified as a string in the call to generate()) to find the page to replace. This is done as a switch statement on the algo string passed to generate(). FIFO will find the oldest frame and set the new frame's insertion time. OPT will calculate all frames' time to next use, and find the frame with the longest time. LFU will find and the physical frame that has the lowest number of uses. LRU will find the frame that has the lowest "slice" for its last use attribute. After the algorithm-specific replacements, generate() records which virtual frame was removed, which physical frame was changed, and actually replaces the frame the algorithm specific methods pointed to. This loop is continued for each integer in the reference string.

When generate() returns, MemorySim's print() is called, which first prints some basic information about the simulation including the length of the reference string, number of physical and virtual frames, and algorithm used. It then steps through each call of the simulation, showing the state of physical memory at the end of each call, including which frame was removed, and which frame was added (if any). If the user is impatient, "q" will cause the simulation printout to quit prematurely.
