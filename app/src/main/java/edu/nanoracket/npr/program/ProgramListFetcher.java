package edu.nanoracket.npr.program;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProgramListFetcher {

    private static final String TAG = "ProgramsListFetcher";

    private static final String programURL =
            "http://www.npr.org/services/apps/iphone/news/programs.json";

    public ArrayList<Program> fetchPrograms(){

        ArrayList<Program> programs = new ArrayList<Program>();
        try {
            String jsonString = getNewsString(programURL);
            Log.i(TAG, "Received Program List JSON String: " + jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            Log.i(TAG,"Received Program JSON Array: " + jsonArray);
            parseJson(programs, jsonArray);
            Log.i(TAG,"Program List: " + programs);
        }catch(IOException ioe){
            Log.e(TAG,"Failed to fetch program list",ioe);
        }catch (JSONException e) {
            Log.e(TAG,"Error converting json data",e);
        }
        return programs;
    }

    public String getNewsString (String urlSpec) throws IOException {
        return new String(getURLBytes(urlSpec));
    }

    byte[] getURLBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead=in.read(buffer))>0){
                out.write(buffer, 0,bytesRead);
            }

            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }



    private void parseJson(ArrayList<Program> programs,JSONArray jsonArray){
        try {
            for(int i=0; i <jsonArray.length();i++){
                JSONObject t = jsonArray.getJSONObject(i);
                if(t.has("src")){
                    String nameString  = t.getString("name");
                    String sourceString = t.getString("src");
                    //String IdString = t.getString("nprId");

                    Program program = new Program();
                    program.setName(nameString);
                    program.setSource(sourceString);
                    //program.setNPRId(IdString);
                    programs.add(program);
                }
            }
        }catch(JSONException e) {
            Log.e(TAG,"Error parsing json data",e);
        }
    }


}
