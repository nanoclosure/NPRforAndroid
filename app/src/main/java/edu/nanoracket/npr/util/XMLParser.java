package edu.nanoracket.npr.util;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import edu.nanoracket.npr.newscast.Newscast;
import edu.nanoracket.npr.podcast.Podcast;
import edu.nanoracket.npr.podcast.PodcastLab;

public class XMLParser {

    public static final String TAG = "XMLParser";
    private Context context;

    public XMLParser(Context context){
        this.context = context;
    }

    public Newscast parseNewscast(String xmlStr) throws XmlPullParserException, IOException {
        Newscast newscast = new Newscast();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlStr));

        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.END_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
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
        return newscast;
    }

    public ArrayList<Podcast> parsePodcast(String xmlStr) throws XmlPullParserException, IOException{
        PodcastLab.getInstance().clearPodcastList();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlStr));

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
                        PodcastLab.getInstance().addPodcast(podcast);
                    break;
                case XmlPullParser.TEXT:
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return PodcastLab.getInstance().getPodcastsList();
    }
}
