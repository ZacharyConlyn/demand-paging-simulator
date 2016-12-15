class MemorySim {
	RefString rs;
	int[] removed;
	int[] pageChanged;
	int rsLen;
	int numOfPhysicalFrames;
	int numOfVirtualFrames;
	PageSlice[] sliceArray;
	Frame[] frameArray;
	
	MemorySim(RefString refs, int phys, int virt) {
		rs = refs;
		rsLen = rs.getLength();
		removed = new int[rsLen];
		pageChanged = new int[rsLen];
		numOfPhysicalFrames = phys;
		numOfVirtualFrames = virt;
		sliceArray = new PageSlice[rsLen];
		frameArray = new Frame[virt];
	}
	
	void generateFifo() {
		initialize();
		// for every virtual frame referenced in the string:
		int currentSlice = 0;
		int frameToInsert;
		int empty;
		int frameToReplace;
		int[] listOfFrames;
		int firstIn;
		int[] tempArray = new int[numOfPhysicalFrames];
		while (currentSlice < rsLen) {
			frameToInsert = rs.getAtIndex(currentSlice);
			empty = sliceArray[currentSlice].indexOfEmpty();
			if (sliceArray[currentSlice].contains(frameToInsert)) {
			}
			else if (empty >= 0) {
				pageChanged[currentSlice] = empty;
				sliceArray[currentSlice].insertFrame(empty, frameToInsert);
				frameArray[frameToInsert].setInserted(currentSlice);
			} else {
				listOfFrames = sliceArray[currentSlice].getAllFrames();
				firstIn = findOldest(listOfFrames);
				removed[currentSlice] = firstIn;
				pageChanged[currentSlice] = sliceArray[currentSlice].indexOf(firstIn);
				frameArray[frameToInsert].setInserted(currentSlice);
			}
			// set up next PageSlice;
			if ((currentSlice + 1) < rsLen) {
				System.arraycopy(sliceArray[currentSlice].getAllFrames(), 0, tempArray, 0, numOfPhysicalFrames);
				sliceArray[currentSlice + 1].setAllFrames(tempArray);
			}
			currentSlice += 1;
		}
	}
	
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
			sliceArray[i] = new PageSlice(numOfPhysicalFrames);
		}
	}
	
	int findOldest(int[] a) {
		int oldest = frameArray[a[0]].getInserted();
		int checking;
		for (int i = 1; i < a.length; i++) {
			checking = frameArray[a[i]].getInserted();
			if (checking < oldest) {
				oldest = checking;
			}
		}
		return oldest;
	}
}
