package comic;

/**
 * The container for the entire comic data structure
 */
public class RequestResult {
	int code;
	String status;
	String copyright;
	Data data;
	
	public int getCode() {
		return code;
	}
	
	public Data getData() {
		return data;
	}
}