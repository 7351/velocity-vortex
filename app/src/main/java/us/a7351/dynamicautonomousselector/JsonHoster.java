package us.a7351.dynamicautonomousselector;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Leo on 10/8/2016.
 */
public class JsonHoster extends NanoHTTPD {

    private String jsonData;

    public JsonHoster() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args) {
        try {
            new JsonHoster();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(jsonData);
    }

    void setData(String jsonData) {
        this.jsonData = jsonData;
    }
}