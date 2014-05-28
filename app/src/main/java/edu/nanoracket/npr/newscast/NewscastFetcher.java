package edu.nanoracket.npr.newscast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Log;

public class NewscastFetcher {
	
	    public static final String TAG = "NewscastFetcher";
	    Newscast newscast = new Newscast();
	    
	    public Newscast fetchNewscast() {
	        
	        try {
	            String url = "http://www.npr.org/rss/podcast.php?id=500005";
	            String xmlString = getUrl(url);
	            Log.i(TAG, "Received xml: " + xmlString);
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            XmlPullParser parser = factory.newPullParser();
	            parser.setInput(new StringReader(xmlString));
	            
	            parseItems(newscast, parser);
	        } catch (IOException ioe) {
	            Log.e(TAG, "Failed to fetch podcast", ioe);
	        } catch (XmlPullParserException xppe) {
	            Log.e(TAG, "Failed to parse podcast", xppe);
	        }
	        return newscast;
	     }


	  public byte[] getUrlBytes(String urlSpec) throws IOException {
	        URL url = new URL(urlSpec);
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

	        try {
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            InputStream in = connection.getInputStream();
	            
	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                return null;
	            }

	            int bytesRead = 0;
	            byte[] buffer = new byte[1024];
	            while ((bytesRead = in.read(buffer)) > 0) {
	                out.write(buffer, 0, bytesRead);
	            }
	            out.close();
	            return out.toByteArray();
	          } finally {
	            connection.disconnect();
	            }
	       }

	    public String getUrl(String urlSpec) throws IOException {
	        return new String(getUrlBytes(urlSpec));
	      }
	    
	   
	     void parseItems(Newscast item, XmlPullParser parser) throws XmlPullParserException, IOException {
	    	 int eventType = parser.next();
	    	 while (eventType != XmlPullParser.END_DOCUMENT) {
	    		 switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.END_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						String tagName = parser.getName();
						/*
						if(tagName != null && tagName.equals("item")){
							podcast = new NPRPodcast();
						}
						*/
						
						if(tagName != null && tagName.equals("title")){
							String title;
							try{
								title = parser.nextText();
								newscast.setTitle(title);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("pubDate")){
							String pubDate;
							try{
								pubDate = parser.nextText();
								newscast.setPubDate(pubDate);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("link")){
							String link;
							try{
								link = parser.nextText();
								newscast.setUrl(link);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("guid")){
							String guid;
							try{
								guid= parser.nextText();
								newscast.setGuid(guid);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("itunes:duration")){
							String duration;
							try{
								duration= parser.nextText();
								newscast.setDuration(duration);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					case XmlPullParser.TEXT:
						break;
					default:
						break;
		        	}
	    		 eventType = parser.next();
	    	 }
	     }
}
	        