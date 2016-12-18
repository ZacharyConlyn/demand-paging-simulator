// File name: Frame.java
// Author: Zachary Conlyn
// Date: December 17, 2016
// Class: CMSC 412
// Purpose: Simple class to represent a virtual page frame and meta-information
// needed to run page replacement algorithms. Designed to work with Pagesim.java

class Frame {
	int number;
	int inserted;
	int nextUse;
	int lastUse;
	int timesUsed;

	Frame(int n) {
		number = n;
		inserted = -1;
		nextUse = -1;
		lastUse = -1;
		timesUsed = 0;
	}
	// various mutator and accessor methods for attributes
	void setNum(int n) {
		number = n;
	}
	int getNum() {
		return number;
	}
	void setInserted(int n) {
		inserted = n;
	}
	int getInserted() {
		return inserted;
	}
	void setNextUse(int n) {
		nextUse = n;
	}
	int getNextUse() {
		return nextUse;
	}
	void setLastUse(int n) {
		lastUse = n;
	}
	int getLastUse() {
		return lastUse;
	}
	void incrementTimesUsed() {
		timesUsed += 1;
	}
	int getTimesUsed() {
		return timesUsed;
	}
}
