package edu.nanoracket.npr.util;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    public static final String API_URL = "http://api.npr.org/query?";
    public static final String PARAM_API_KEY = "apiKey";
    public static final String PARAM_ID = "id";
    public static final String PARAM_STARTNUM = "startNum";
    public static final String PARAM_NUMRESULTS = "numResults";
    public static final String PARAM_OUTPUT = "output";
    public static final String PARAM_SORT="sort";
    public static final String PARAM_REQUIRED_ASSETS = "requiredAssets";

    public static final String API_KEY = "MDEyMTY1NzEyMDEzNzg3Mzk1OTE5MDBkYQ001";
    public static final String NUMRESULTS = "10";
    public static final String OUTPUT = "JSON";
    public static final String SORT = "dateDesc";
    private static final String REQUIRED_ASSETS ="text,image,byline";
    private static final String TAG = "HttpHelper" ;

    public String createURL(String Id, String startNum){

        String url = Uri.parse(API_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_ID, Id)
                .appendQueryParameter(PARAM_OUTPUT, OUTPUT)
                .appendQueryParameter(PARAM_SORT, SORT)
                .appendQueryParameter(PARAM_STARTNUM, startNum)
                .appendQueryParameter(PARAM_NUMRESULTS, NUMRESULTS)
                .appendQueryParameter(PARAM_REQUIRED_ASSETS,REQUIRED_ASSETS )
                .build().toString();

        Log.i(TAG, "Created URL: " + url);

        return url;
    }

    public String sendURLConnectionRequest(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return  null;
            }

            int bytesRead;
            byte[] buffer = new byte[1024];

            while((bytesRead = in.read(buffer))>0){
                out.write(buffer, 0, bytesRead);
            }

            return new String(out.toByteArray());

        } finally {
            connection.disconnect();
        }
    }

}
