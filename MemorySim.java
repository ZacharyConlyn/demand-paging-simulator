// File name: MemorySim.java
// Author: Zachary Conlyn
// Date: December 17 2016
// Class: CMSC 412
// Purpose: Memory simulation object that holds information about physical
// and virtual memory of a system, and has methods to run and print results of
// various algorithms. Designed to work with Pagesim.java

import java.util.Scanner;

class MemorySim {
	RefString rs; // reference string object
	int[] removed; // keep track of removed pages
	int[] pageChanged; // keep track of physical page that was swapped/changed
	int rsLen; // length of the reference string (number of calls to virtual memory)
	int numOfPhysicalFrames;
	int numOfVirtualFrames;
	int[][] physicalMemory; // first dimension represents "time", 2nd is the
							// phyiscal memory at that time
	Frame[] frameArray; // keep track of all the virtual frames in this array
	String algoType; // keep track of which algorithm the simulation ran

	MemorySim(RefString refs, int phys, int virt) {
		rs = refs;
		rsLen = rs.getLength();
		removed = new int[rsLen];
		pageChanged = new int[rsLen];
		numOfPhysicalFrames = phys;
		numOfVirtualFrames = virt;
		physicalMemory = new int[rs.getLength()][phys];
		frameArray = new Frame[virt];
	}
	// the "generateXXX()" methods use the reference string and supplied information
	// about the virtual and physical memory to run through simulations.
	void generateFifo() {
		initialize();
		algoType = "FIFO";
		int currentSlice = 0;
		int frameToInsert;
		int empty;
		int frameToReplace;
		int[] listOfFrames;
		// the while loops step through each call of the simulation
		while (currentSlice < rsLen) {
			frameToInsert = rs.getAtIndex(currentSlice);
			empty = findIndex(physicalMemory[currentSlice], -1);
			// if the page we need is already in physical memory...
			if (findIndex(physicalMemory[currentSlice], frameToInsert) != -1) {
			}
			// if it's not in memory but there's an empty space for it...
			else if (empty >= 0) {
				pageChanged[currentSlice] = empty;
				physicalMemory[currentSlice][empty] = frameToInsert;
				frameArray[frameToInsert].setInserted(currentSlice);
			}
			// not in memory and no empty space
			else {
				// find the oldest frame
				frameToReplace = findOldest(physicalMemory[currentSlice]);
				// record removed frame
				removed[currentSlice] = physicalMemory[currentSlice][frameToReplace];
				// record new frame spot
				pageChanged[currentSlice] = frameToReplace;
				// put the new frame in that spot
				physicalMemory[currentSlice][frameToReplace] = frameToInsert;
				// update insertion time
				frameArray[frameToInsert].setInserted(currentSlice);


			}
			// make the physical memory for the next call a copy of the physical
			// memory at the end of this call
			if ((currentSlice + 1) < rsLen) {
				for (int i = 0; i < numOfPhysicalFrames; i ++) {
					physicalMemory[currentSlice +1][i] = physicalMemory[currentSlice][i];
				}
			}
			currentSlice += 1;
		}
	}

	void generateOpt() {
		initialize();
		algoType = "OPT";
		int currentSlice = 0;
		int frameToInsert;
		int empty;
		int frameToReplace;
		int[] listOfFrames;

		while (currentSlice < rsLen) {
			// calculate next use for every frame
			calculateNextUse(currentSlice);

			frameToInsert = rs.getAtIndex(currentSlice);
			empty = findIndex(physicalMemory[currentSlice], -1);
			// if it's already in memory...
			if (findIndex(physicalMemory[currentSlice], frameToInsert) != -1) {
			}
			// if it's not in memory but there's an empty space for it...
			else if (empty >= 0) {
				pageChanged[currentSlice] = empty;
				physicalMemory[currentSlice][empty] = frameToInsert;
				frameArray[frameToInsert].setInserted(currentSlice);
			}
			// if it's not in memory and there's no empty space...
			else {
				// find the least optimal page
				frameToReplace = findLeastOptimal(physicalMemory[currentSlice]);
				// record replaced frame
				removed[currentSlice] = physicalMemory[currentSlice][frameToReplace];
				// record new frame spot
				pageChanged[currentSlice] = frameToReplace;
				// put in the new frame at that spot
				physicalMemory[currentSlice][frameToReplace] = frameToInsert;
			}
			// make the physical memory for the next call a copy of the physical
			// memory at the end of this call
			if ((currentSlice + 1) < rsLen) {
				for (int i = 0; i < numOfPhysicalFrames; i ++) {
					physicalMemory[currentSlice +1][i] = physicalMemory[currentSlice][i];
				}
			}
			currentSlice += 1;
		}
	}

	void generateLru() {
		initialize();
		algoType = "LRU";
		int currentSlice = 0;
		int frameToInsert;
		int empty;
		int frameToReplace;
		int[] listOfFrames;
		while (currentSlice < rsLen) {
			// calculate next use for every frame
			frameToInsert = rs.getAtIndex(currentSlice);
			empty = findIndex(physicalMemory[currentSlice], -1);
			// if it's already in memory...
			if (findIndex(physicalMemory[currentSlice], frameToInsert) != -1) {
			}
			// if it's not in memory but there's an empty space for it...
			else if (empty >= 0) {
				pageChanged[currentSlice] = empty;
				physicalMemory[currentSlice][empty] = frameToInsert;
				frameArray[frameToInsert].setInserted(currentSlice);
			}
			// if it's not in memory and there's no empty space...
			else {
				// find least recently used
				frameToReplace = findLru(physicalMemory[currentSlice]);
				// record removed
				removed[currentSlice] = physicalMemory[currentSlice][frameToReplace];
				// record new frame spot
				pageChanged[currentSlice] = frameToReplace;
				// replace it
				physicalMemory[currentSlice][frameToReplace] = frameToInsert;
			}
			// make the physical memory for the next call a copy of the physical
			// memory at the end of this call
			if ((currentSlice + 1) < rsLen) {
				for (int i = 0; i < numOfPhysicalFrames; i ++) {
					physicalMemory[currentSlice +1][i] = physicalMemory[currentSlice][i];
				}
			}
			// update information for last use of the frame just called/
			frameArray[frameToInsert].setLastUse(currentSlice);
			currentSlice += 1;
		}
	}

	void generateLfu() {
		initialize();
		algoType = "LFU";
		int currentSlice = 0;
		int frameToInsert;
		int empty;
		int frameToReplace;
		int[] listOfFrames;
		while (currentSlice < rsLen) {
			// calculate next use for every frame
			frameToInsert = rs.getAtIndex(currentSlice);
			empty = findIndex(physicalMemory[currentSlice], -1);
			// if it's already in memory...
			if (findIndex(physicalMemory[currentSlice], frameToInsert) != -1) {
			}
			// if it's not in memory but there's an empty space for it...
			else if (empty >= 0) {
				pageChanged[currentSlice] = empty;
				physicalMemory[currentSlice][empty] = frameToInsert;
				frameArray[frameToInsert].setInserted(currentSlice);
			}
			// if it's not in memory and there's no empty space...
			else {
				// find least recently used
				frameToReplace = findLfu(physicalMemory[currentSlice]);
				// record it
				removed[currentSlice] = physicalMemory[currentSlice][frameToReplace];
				// record new frame spot
				pageChanged[currentSlice] = frameToReplace;
				// replace it
				physicalMemory[currentSlice][frameToReplace] = frameToInsert;
			}
			if ((currentSlice + 1) < rsLen) {
				for (int i = 0; i < numOfPhysicalFrames; i ++) {
					physicalMemory[currentSlice +1][i] = physicalMemory[currentSlice][i];
				}
			}
			// make the physical memory for the next call a copy of the physical
			// memory at the end of this call
			frameArray[frameToInsert].incrementTimesUsed();
			currentSlice += 1;
		}
	}

	// find least frequently used frame, given an array containing frame numbers
	int findLfu(int[] a) {
		int lfuIndex = 0;
		int lfuTimesUsed = frameArray[a[lfuIndex]].getTimesUsed();

		for (int i = 1; i < a.length; i++) {
			int temp = a[i];
			int tempTimesUsed = frameArray[a[i]].getTimesUsed();

			if (tempTimesUsed < lfuTimesUsed) {
				lfuIndex = i;
				lfuTimesUsed = tempTimesUsed;
			}
		}

		return lfuIndex;
	}

	// find least recently used frame, given an array containing frame numbers
	int findLru(int[] a) {
		int lruIndex = 0;
		int lruLastUse = frameArray[a[lruIndex]].getLastUse();

		for (int i = 1; i < a.length; i++) {
			int temp = a[i];
			int tempLastUse = frameArray[a[i]].getLastUse();

			if (tempLastUse < lruLastUse) {
				lruIndex = i;
				lruLastUse = tempLastUse;

			}
		}

		return lruIndex;
	}

	// find "least optimal" frame (i.e. frame with longest time until next use),
	// given an array containing frame numbers
	int findLeastOptimal(int[] a) {
		int leastOptimal = a[0];
		int leastOptimalIndex = 0;
		int leastOptNextUse = frameArray[leastOptimal].getNextUse();
		for (int i = 1; i < a.length; i++) {
			int temp = a[i];
			int tempNextUse = frameArray[temp].getNextUse();
			if (tempNextUse > leastOptNextUse) {
				leastOptimal = temp;
				leastOptNextUse = frameArray[leastOptimal].getNextUse();
				leastOptimalIndex = i;
			}
		}
		return leastOptimalIndex;
	}

	// runs through each Frame object in the frameArray and finds the next use,
	// and updates each Frame with that information
	void calculateNextUse(int n) {
		// first it sets each Frame's next call to past the end of the reference
		// string
		for (int i = 0; i < numOfVirtualFrames; i++) {
			frameArray[i].setNextUse(rsLen + 1);
		}
		// then it works backwards from the end, updating setNextUse() for each
		// frame called
		for (int i = rsLen - 1; i >= n; i--) {
			int called = rs.getAtIndex(i);
			frameArray[called].setNextUse(i);
		}
	}

	// initialize all the arrays used in generateXXX()
	void initialize() {
		// set removed to -1s
		for (int i = 0; i < removed.length; i++) {
			removed[i] = -1;
		}
		// set pages changed to -1s
		for (int i = 0; i < pageChanged.length; i++) {
			pageChanged[i] = -1;
		}
		// set clean array of frames:
		for (int i = 0; i < numOfVirtualFrames; i++) {
			frameArray[i] = new Frame(i);
		}
		// clean array of slices
		for (int i = 0; i < rsLen; i++) {
			for (int j = 0; j < numOfPhysicalFrames; j ++) {
				physicalMemory[i][j] = -1;
			}
		}
		algoType = "";
	}

	// find the first inserted Frame, given an array of Frame numbers
	int findOldest(int[] a) {
		int oldest = frameArray[a[0]].getInserted();
		int oldestIndex = 0;
		int checking;
		for (int i = 1; i < a.length; i++) {
			checking = frameArray[a[i]].getInserted();
			if (checking < oldest) {
				oldest = checking;
				oldestIndex = i;
			}
		}
		return oldestIndex;
	}

	// print the results of the simluation, one call at a time
	void print() {
		System.out.println("Basic information: ");
		System.out.println("Algo type: " + algoType);
		System.out.println("Length of reference string: " + rsLen);
		System.out.println("Number of virtual pages: " + numOfVirtualFrames);
		System.out.println("Number of physical pages: " + numOfPhysicalFrames);
		System.out.println("---");
		System.out.println("[brackets] around a page number indicate it was changed.");
		System.out.println("Press enter to step through snapshots of physical memory after each string call. Or, enter \"q\" at any time to return to main menu.");

		Scanner sc = new Scanner(System.in);
		int steppingSlice = 0;
		String prompt;
		int frameNum;
		int removedInt;
		while (steppingSlice < rsLen) {
			prompt = sc.nextLine();
			if (prompt.equals("q")) {
				System.out.println("Quitting printout.");
				break;
			}
			System.out.println("Snapshot at call " + (steppingSlice + 1) + ":");
			System.out.println("Program called virtual frame # " + rs.getAtIndex(steppingSlice));
			for (int i = 0; i < numOfPhysicalFrames; i ++) {
				System.out.print("Physical frame " + i + ":");
				frameNum = physicalMemory[steppingSlice][i];
				if (frameNum >= 0) {
					if (i == pageChanged[steppingSlice]) {
						System.out.println("[" + frameNum + "]");
					} else {
						System.out.println(" " + frameNum);
					}
				} else {
					System.out.println("x");
				}
			}
			removedInt = removed[steppingSlice];
			System.out.println("Frame removed: " + (removedInt == -1 ? "None." : removedInt));
			steppingSlice += 1;
		}
		System.out.print("Simluation finished. Press enter to continue.");
		sc.nextLine();
	}

	int findIndex(int[] a, int n) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == n) {
				return i;
			}
		}
		return -1;
	}
}
