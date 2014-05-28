package edu.nanoracket.npr.podcast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Log;

public class PodcastListFetcher {
	
	 public static final String TAG = "NPRPodcastListFetcher";
	 //ArrayList<NPRPodcast> podcastList;
	 PodcastList sPodcastList = PodcastList.get();
	 
	 public ArrayList<Podcast> fetchPodcasts(String url) {
		    sPodcastList.clearNPRPodcastList();
		    //podcastList = new ArrayList<NPRPodcast>();
	        try {
	            String xmlString = getUrl(url);
	            Log.i(TAG, "Received xml: " + xmlString);
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            XmlPullParser parser = factory.newPullParser();
	            parser.setInput(new StringReader(xmlString));
	            
	            parseItems(parser);
	        } catch (IOException ioe) {
	            Log.e(TAG, "Failed to fetch podcast", ioe);
	        } catch (XmlPullParserException xppe) {
	            Log.e(TAG, "Failed to parse podcast", xppe);
	        }
	        return sPodcastList.getNPRPodcastsList();
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
	    
	   
	  private void parseItems(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	 int eventType = parser.next();
	    	 Podcast podcast = new Podcast();
	    	 
	    	 while (eventType != XmlPullParser.END_DOCUMENT) {
	    		 switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.END_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						String tagName = parser.getName();
						
						if(tagName != null && tagName.equals("item")){
							podcast = new Podcast();
						}else if(tagName != null && tagName.equals("title")){
							String title;
							title = parser.nextText();
							podcast.setTitle(title);
						}else if(tagName != null && tagName.equals("pubDate")){
							String pubDate;
							pubDate = parser.nextText();
							podcast.setPubDate(pubDate);
						}else if(tagName != null && tagName.equals("guid")){
							String guid;
							guid= parser.nextText();
							podcast.setGuid(guid);
						
						}else if(tagName != null && tagName.equals("itunes:duration")){
							String duration;
							duration= parser.nextText();
							podcast.setDuration(duration);
						 }else if(tagName != null && tagName.equals("enclosure")){
							String url;
						    url = parser.getAttributeValue(null, "url");
						    podcast.setUrl(url);
						    Log.i(TAG, "url:" + url);
						}
						break;
					case XmlPullParser.END_TAG:
						if("item".equals(parser.getName()))
							sPodcastList.addNPRPodcast(podcast);
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
