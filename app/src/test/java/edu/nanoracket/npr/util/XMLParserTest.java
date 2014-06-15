package edu.nanoracket.npr.util;

import android.content.Context;
import edu.nanoracket.npr.model.Newscast;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class XMLParserTest {
    Context context;

    @Before
    public void setUp(){
        context = Robolectric.application;
    }

    @Test
    public void testParseNewscast() throws IOException, XmlPullParserException {
        String url = "http://www.npr.org/rss/podcast.php?id=500005";
        String xmlStr = new HttpHelper().sendURLConnectionRequest(url);
        Newscast newscast = new XMLParser(context).parseNewscast(xmlStr);
        Assert.assertNotNull(newscast.getTitle());
    }
}
