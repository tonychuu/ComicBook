package comic;

import java.util.ArrayList;

/**
 * The data structure for a comic book
 */
public class Comic {
	private int id;
	private String title;
	private double issueNumber;
	private String description;
	private String isbn;
	private String upc;
	private String diamondCode;
	private String ean;
	private String issn;
	private String format;
	private int pageCount;
	private ArrayList<Price> prices;
	private ArrayList<ComicCoverImage> images;
	private Creators creators;
	
	public Comic() {
		
	}
	
	public int getId() {
		return id;
	}
	public Creators getCreators() {
		return creators;
	}
	public String getUPC() {
		return upc;
	}
	public String getDiamondCode() {
		return diamondCode;
	}
	public String getEAN() {
		return ean;
	}
	public String getISSN() {
		return issn;
	}
	public int getPageCount() {
		return pageCount;
	}
	public String getDescription() {
		return description;
	}
	public String getISBN() {
		return isbn;
	}
	public double getIssueNum() {
		return issueNumber;
	}
	public String getTitle() {
		return title;
	}
	public Price getPrice() {
		if (prices.size() > 0) {
			return prices.get(0);
		} else {
			return null;
		}
	}
	public String getFormat() {
		return format;
	}
	
	public ComicCoverImage getImage() {
		if (images.size() > 0) {
			return images.get(0);
		} else {
			return null;
		}
	}
}