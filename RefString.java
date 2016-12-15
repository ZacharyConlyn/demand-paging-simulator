import java.util.ArrayList;

class RefString {
	ArrayList<Integer> refString;
	int next;	

	RefString() {
		refString = new ArrayList<Integer>();
		next = 0;
	}

	RefString(ArrayList<Integer> rs) {
		refString = rs;
		next = 0;
	}
	int getLength() {
		return refString.size();
	}
	int getNext() {
		if (next < refString.size()) {
			return refString.get(next++);
		}
		return -1;
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
