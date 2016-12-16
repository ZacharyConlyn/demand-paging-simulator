class PageSlice {
	int[] frameArray;
	
	PageSlice(int n) {
		frameArray = new int[n];
		for (int i = 0; i > n; i++) {
			frameArray[i] = -1;
		}
	}
	void insertFrame(int n, int v) {
		frameArray[n] = v;
	}
	int getFrame(int n) {
		return frameArray[n];
	}
	int[] getAllFrames() {
		return frameArray;
	}
	
	void setAllFrames(int[] fa) {
		frameArray = fa;
	}
	
	int indexOfEmpty() {
		for (int i = 0; i < frameArray.length; i++) {
			if (frameArray[i] == -1) {
				return i;
			}
		}
		return -1;
	}
	
	boolean contains(int n) {
		for (int i = 0; i < frameArray.length; i++) {
			if (frameArray[i] == n) {
				return true;
			}
		}
		return false;

	}
	
	int indexOf(int n) {
		for (int i = 0; i < frameArray.length; i++) {
			if (frameArray[i] == n) {
				return i;
			}
		}
		return -1;
	}
}
