package comic;

import java.util.ArrayList;

/**
 * The data structure for all creators for a comic book
 */
public class Creators {
	ArrayList<Creator> items; //each item is a creator
	
	public Creator getCreator(int i) {
		return items.get(i);
	}
	
	public int getSize() {
		return items.size();
	}
}
