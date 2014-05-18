package edu.nanoracket.nprforandroid.podcast;

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

public class PodcastFetcher {
	
	    public static final String TAG = "NPRPodcastFetcher";
	    Podcast podcast = new Podcast();
	    
	    public Podcast fetchPodcast() {
	        
	        try {
	            String url = "http://www.npr.org/rss/podcast.php?id=500005";
	            String xmlString = getUrl(url);
	            Log.i(TAG, "Received xml: " + xmlString);
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            XmlPullParser parser = factory.newPullParser();
	            parser.setInput(new StringReader(xmlString));
	            
	            parseItems(podcast, parser);
	        } catch (IOException ioe) {
	            Log.e(TAG, "Failed to fetch podcast", ioe);
	        } catch (XmlPullParserException xppe) {
	            Log.e(TAG, "Failed to parse podcast", xppe);
	        }
	        return podcast;
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

	            int bytesRead;
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
	    
	   
	     void parseItems(Podcast item, XmlPullParser parser) throws XmlPullParserException, IOException {
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
								podcast.setTitle(title);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("pubDate")){
							String pubDate;
							try{
								pubDate = parser.nextText();
								podcast.setPubDate(pubDate);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("link")){
							String link;
							try{
								link = parser.nextText();
								podcast.setUrl(link);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("guid")){
							String guid;
							try{
								guid= parser.nextText();
								podcast.setGuid(guid);
							} catch (IOException e){
								e.printStackTrace();
							}
						}
						
						if(tagName != null && tagName.equals("itunes:duration")){
							String duration;
							try{
								duration= parser.nextText();
								podcast.setDuration(duration);
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
	        