package edu.nanoracket.npr.util;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
    private static final String TAG = "HttpHelper" ;

    public String createURL(String Id, String startNum){
        String url = Uri.parse(ApiConstants.API_URL).buildUpon()
                .appendQueryParameter(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY)
                .appendQueryParameter(ApiConstants.PARAM_ID, Id)
                .appendQueryParameter(ApiConstants.PARAM_OUTPUT, ApiConstants.OUTPUT)
                .appendQueryParameter(ApiConstants.PARAM_SORT, ApiConstants.SORT)
                .appendQueryParameter(ApiConstants.PARAM_STARTNUM, startNum)
                .appendQueryParameter(ApiConstants.PARAM_NUMRESULTS, ApiConstants.NUMRESULTS)
                .appendQueryParameter(ApiConstants.PARAM_REQUIRED_ASSETS,ApiConstants.REQUIRED_ASSETS )
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
