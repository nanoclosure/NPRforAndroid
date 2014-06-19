package edu.nanoracket.npr.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class StubHttpURLConnection extends HttpURLConnection{
    private boolean isInput = true;
    protected StubHttpURLConnection(URL url) {
        super(url);
    }

    public InputStream getInputStream() throws IOException{
        if(!isInput){
            throw new ProtocolException(
                    "Can't read from URLConnection" +
                            " if doInput=false (call setDoInput(true))"
            );
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(
                new String("It works").getBytes()
        );
        return bais;
    }

    @Override
    public void disconnect() { }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException { }
}

