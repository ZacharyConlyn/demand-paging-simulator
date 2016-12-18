// File name: RefString.java
// Author: Zachary Conlyn
// Date: December 17, 2016
// Class: CMSC 412
// Purpose: Class to represent a virtual page reference string, of variable
// length. Designed to work with Pagesim.java

import java.util.ArrayList;

class RefString {
	ArrayList<Integer> refString; // our actual string of numbers

	// this base constructor is not used in Pagesim.java
	RefString() {
		refString = new ArrayList<Integer>();
	}

	// this constructor lets you pass in a premade ArrayList of ints
	RefString(ArrayList<Integer> rs) {
		refString = rs;
	}

	// accessor methods
	int getLength() {
		return refString.size();
	}
	int getAtIndex(int i) {
		return refString.get(i);
	}
	void print() {
		int i;
		for (i = 0; i < refString.size() - 1; i++) {
			System.out.print(refString.get(i) + ", ");
		}
		System.out.print(refString.get(i));
	}
}
