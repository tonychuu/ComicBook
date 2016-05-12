package comic;

/**
 * The data structure for a comic book image
 */
public class ComicCoverImage {
	private String path;
	private String extension;
	
	public String getURL() {
		return path + "/portrait_fantastic" + "." + extension;
	}
}