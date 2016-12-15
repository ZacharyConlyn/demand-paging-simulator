import java.util.ArrayList;

class PageSlice {
	Frame[] frameArray;
	
	PageSlice(int n) {
		frameArray = new Frame[n];
	}
	void insertFrame(int n, Frame v) {
		frameArray[n] = v;
	}
	VirtFrame getFrame(int n) {
		return frameArray[n];
	}
	VirtFrame[] getAllFrames() {
		return frameArray;
	}
}
