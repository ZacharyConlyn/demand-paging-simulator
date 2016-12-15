class MemorySim {
	RefString rs;
	int[] removed;
	int[] pageChanged;
	int rsLen;
	int numOfPhysicalFrames;
	int numOfVirtualFrames;
	PageSlice[] sliceArray;
	Frame[] frameArray;
	
	MemorySim(RefString rs, int phys, int virt) {
		rs = rs;
		algo = algo;
		rsLen = rs.getLength();
		removed = new int[rsLen];
		pageChanged = new int[rsLen];
		numOfPhysicalFrames = phys;
		numOfVirtualFrames = virt;
		sliceArray = new PageSlice[rsLen];
		frameArray = new Frame[virt];
	}
	
	void generateFifo() {
		self.initialize();
	}
	
	void initialize() {
		// set removed to -1s
		for (int i = 0; i < removed.length(), i++) {
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
}
