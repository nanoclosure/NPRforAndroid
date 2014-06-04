package edu.nanoracket.npr.program;

import org.json.JSONException;
import org.json.JSONObject;

public class Program {

    private static final String JSON_NAME = "name";
    private static final String JSON_SOURCE = "source";
    private static final String JSON_ID = "id";

    private String mName;
    private String mSource;
    private String mId;

    public Program() {}

    public Program(JSONObject json) throws JSONException {
        mName = json.getString(JSON_NAME);
        mId = json.getString(JSON_ID);
        mSource = json.getString(JSON_SOURCE);
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getSource() {
        return mSource;
    }
    public void setSource(String source) {
        mSource = source;
    }
    public String getId() {
        return mId;
    }
    public void setId(String Id) {
        mId = Id;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_NAME, mName);
        json.put(JSON_ID, mId);
        json.put(JSON_SOURCE, mSource);
        return json;
    }

    @Override
    public String toString(){
        return mName;
    }
}

