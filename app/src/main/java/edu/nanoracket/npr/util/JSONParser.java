package edu.nanoracket.npr.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.nanoracket.npr.model.Story;
import edu.nanoracket.npr.lab.StoryLab;
import edu.nanoracket.npr.model.Program;

public class JSONParser {

    private static final String TAG = "JSONParser" ;
    private Context context;

    public JSONParser(Context context){
        this.context = context;
    }

    public ArrayList<Story> parseStoryJson(String jsonStr) throws JSONException {
        StoryLab storyLab = StoryLab.getInstance(context);
        JSONObject storiesJsonObject = new JSONObject(jsonStr);
        JSONArray storiesJsonArray = storiesJsonObject.getJSONObject("list").getJSONArray("story");
        for(int i = 0; i< storiesJsonArray.length(); i++){
            JSONObject storyJson = storiesJsonArray.getJSONObject(i);
            Story story = new Story();
            story.setId(storyJson.getString("id"));
            story.setTitle(storyJson.getJSONObject("title").getString("$text"));
            if(storyJson.has("teaser")){
                story.setTeaser(storyJson.getJSONObject("teaser").getString("$text"));
            }
            if(storyJson.has("slug")){
                story.setSlug(storyJson.getJSONObject("slug").getString("$text"));
            }
            story.setPubDate(storyJson.getJSONObject("pubDate").getString("$text"));
            if(storyJson.has("byline")){
                JSONArray bylineJson = storyJson.getJSONArray("byline");
                Story.Byline byline = new Story.Byline(
                        bylineJson.getJSONObject(0).getJSONObject("name").getString("$text"));
                story.setByline(byline);
            }
            if(storyJson.has("image")){
                JSONObject imageJson = storyJson.getJSONArray("image").getJSONObject(0);
                String imageId = imageJson.getString("id");
                String imageType = imageJson.getString("type");
                String imageWidth = imageJson.getString("width");
                String imageSrc = imageJson.getString("src");
                String imageHasBorder = imageJson.getString("hasBorder");
                //String imageCaption = imageJson.getJSONObject("caption").getString("$text");
                Story.Image image = new Story.Image(imageId, imageType,imageWidth, imageSrc,
                        imageHasBorder);
                story.setImage(image);
            }
/*            if(storyJson.has("text")){
                JSONArray paraJsonArray = storyJson.getJSONObject("text").getJSONArray("paragraph");
                HashMap<Integer, String> paragraphs = new HashMap<Integer, String>();
                Story.Text text = null;
                for(int j = 0; j < paraJsonArray.length(); j++){
                    String string = paraJsonArray.getJSONObject(j).getString("$text");
                    Log.i(TAG, "Text string is :" + string);
                    paragraphs.put(new Integer(j),
                                   string);
                }
                text = new Story.Text(paragraphs);
                menu_storyactivity.setText(text);
            }*/
            if(storyJson.has("textWithHtml")){
                JSONArray paraJsonArray = storyJson.getJSONObject("textWithHtml")
                                         .getJSONArray("paragraph");
                HashMap<Integer, String> paragraphsHtml = new HashMap<Integer, String>();
                Story.TextWithHtml textWithHtml;

                for(int k = 0; k < paraJsonArray.length(); k++){
                    paragraphsHtml.put(k,
                                       paraJsonArray.getJSONObject(k).getString("$text"));
                }
                textWithHtml = new Story.TextWithHtml(paragraphsHtml);
                story.setTextWithHtml(textWithHtml);
            }
            storyLab.addStory(story);
        }
        return storyLab.getStoryList();
    }

    public ArrayList<Program> parseProgramJson(String jsonStr) throws JSONException{
        ArrayList<Program> programs = new ArrayList<Program>();
        Log.i(TAG, jsonStr);

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i=0; i <jsonArray.length();i++){
                JSONObject t = jsonArray.getJSONObject(i);
                if(t.has("src")){
                    String nameString  = t.getString("name");
                    String sourceString = t.getString("src");
                    String nprId = t.getString("guid");

                    Program program = new Program();
                    program.setName(nameString);
                    program.setSource(sourceString);
                    program.setId(nprId);
                    programs.add(program);
                }
            }
        }catch(JSONException e) {
            Log.e(TAG, "Error parsing json data", e);
        }
        return programs;
    }
}
