package main;
import java.io.IOException;
import java.security.*;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.*;

import comic.RequestResult;

public class Backend {
	private int currentoffset = 0;
	final private String publickey = "026ab997f3b126d7326e1e4d5e5360cb";
	final private String privatekey = "6744167d708a393c2110673509dd2f2db920e6fc";
	final int comicsperrequest = 15;
	private int totalcomics = 1; //temporary value, will be updated after first communication with the API
	
	public Backend() {
		
	}
	/**
	 * Connects with the Marvel API and deserializes the JSON string into the comic data structure
	 *
	 * @param  offset	The offset of the comic books to retrieve. 0 would mean to get the first 15 comic books from the Marvel API.
	 * @return      the comic data structure
	 */
	private RequestResult getResponse(int offset) {
		String url = "http://gateway.marvel.com/v1/public/comics";
		CloseableHttpClient hc = HttpClients.createDefault();
		try {
			Date date = new Date();
			long timestamp = date.getTime();
			String digest = getHash(timestamp);
			String urlfinal = url + "?" + "offset=" + offset + "&limit=" + comicsperrequest + "&ts=" + timestamp + "&apikey=" + publickey + "&hash=" + digest; 
			
			HttpGet httpget = new HttpGet(urlfinal);
			
			System.out.println("Excecuting request " + httpget.getRequestLine());
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            
            String responseBody = hc.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            
            Gson gson = new Gson();
            RequestResult rr = gson.fromJson(responseBody, RequestResult.class);
            totalcomics = rr.getData().getTotal();
            return rr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Creates the hash that the Marvel API requires
	 *
	 * @param  timestamp  the current time
	 * @return      the hashed string
	 */
	private String getHash(long timestamp) {
		try {
			String hash = timestamp + privatekey + publickey;
			byte[] bytehash = hash.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytehash);
			byte[] digest = md.digest();
			hash = byteToHex(digest);
			return hash;
		} catch (Exception e) {
			System.err.println("Error, couldn't create hash");
			return null;
		}
	}
	
	/**
	 * Converts a byte-valued hash into a hex-valued hash
	 *
	 * @param  bytearray  the byte-valued hash
	 * @return      the equivalent byte-valued hash in hex form
	 */
	private String byteToHex(byte[] bytearray) {
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytearray.length; i++) {
         sb.append(Integer.toString((bytearray[i] & 0xff) + 0x100, 16).substring(1));
        }
		return sb.toString();
	}
	
	/**
	 * Calculates the total number of pages that are available
	 *
	 * @return      The total number of pages that the UI can display  
	 */
	public int getTotalPages() {
		double totalcomicsdouble = totalcomics;
		totalcomicsdouble = Math.ceil((double) totalcomics / comicsperrequest);
		return (int) totalcomicsdouble;
	}
	
	/**
	 * Generates the first page
	 *
	 * @return      The comic data structure for the initial page
	 */
	public RequestResult initPage() {
		int initialoffset = 0;
		return getResponse(initialoffset);
	}
	
	/**
	 * Generates the next page 
	 *
	 * @param	currpage  The current page 
	 * @return      The comic data structure for that page
	 */
	public RequestResult nextPage(int currpage) {
		currpage = currpage + 1;
		currentoffset = (currpage - 1) * comicsperrequest; //pages start from 1 to n, not 0 to n
		return getResponse(currentoffset);
	}
	
	/**
	 * Generates the previous page
	 *
	 * @param 	currpage  The current page
	 * @return      The comic data structure for that page
	 */
	public RequestResult prevPage(int currpage) {
		currpage = currpage - 1;
		currentoffset = (currpage - 1) * comicsperrequest; //pages start from 1 to n, not 0 to n
		return getResponse(currentoffset);
	}
	
	/**
	 * Generates the first page
	 *
	 * @param	wantpage  The page to retrieve
	 * @return      The comic data structure for that page
	 */
	public RequestResult getPage(int wantpage) {
		if (wantpage > 0) {
			currentoffset = (wantpage - 1) * comicsperrequest; //pages start from 1 to n, not 0 to n
			return getResponse(currentoffset);
		} else {
			return initPage();
		}
	}
}
