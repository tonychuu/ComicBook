package comic;

import java.util.ArrayList;

/**
 * The container for an array of comic books
 */
public class Data {
	private int total;
	private int count;
	private ArrayList<Comic> results;
	
	public Comic getResults(int i) {
		return results.get(i);
	}
	
	public int getCount() {
		return count;
	}
	
	public int getTotal() {
		return total;
	}
}