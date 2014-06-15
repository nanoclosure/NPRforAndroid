package edu.nanoracket.npr.util;

import android.content.Context;
import edu.nanoracket.npr.model.Program;
import junit.framework.Assert;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class JSONParserTest {

    @Test
    public void testParseProgramJson() throws IOException, JSONException {
        Context context = Robolectric.application;
        String url = "http://www.npr.org/services/apps/iphone/news/programs.json";
        String jsonStr = new HttpHelper().sendURLConnectionRequest(url);
        ArrayList<Program> programs = new JSONParser(context).parseProgramJson(jsonStr);
        Assert.assertEquals(19, programs.size());
    }
}
