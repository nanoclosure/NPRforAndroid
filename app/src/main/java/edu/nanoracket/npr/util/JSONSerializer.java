package edu.nanoracket.npr.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import edu.nanoracket.npr.program.Program;

public class JSONSerializer {

    private static final String TAG = "JSONSerizer";
    private Context mContext;
    private String mFileName;

    public JSONSerializer(Context context, String fileName){
        mContext = context;
        mFileName = fileName;
    }

    public void savePrograms(ArrayList<Program> programs) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for(Program program : programs){
            array.put(program.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    public void saveJSONString(String jsonStr) throws IOException {
        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonStr);
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    public String loadJSONString(){
        BufferedReader reader = null;
        StringBuilder jsonString = new StringBuilder();

        try{
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }
        }catch (FileNotFoundException e){
            Log.e(TAG, "can not find the file.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonString.toString();
    }

    public ArrayList<Program> loadPrograms() throws IOException, JSONException {
        ArrayList<Program> programs = new ArrayList<Program>();
        BufferedReader reader = null;

        try{
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine())!= null){
                jsonString.append(line);
            }
            Log.i(TAG, "The json string is: " + jsonString.toString());
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for(int i = 0; i < array.length(); i++){
                programs.add(new Program(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            Log.i(TAG, "Can not find the file.");
        }finally {
            if(reader != null){
                reader.close();
            }
        }

        return programs;
    }
}
