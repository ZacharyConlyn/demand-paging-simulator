Class VirtFrame {
	int number;
	int inserted;
	int nextUse;
	int lastUse;
	int timesUsed;

	VirtFrame(int n) {
		number = n;
	}
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
