package ca.apachegui.sendlog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class HTTPRequestPoster
{
    private static Logger log = Logger.getLogger(HTTPRequestPoster.class);

    /**
     * Reads data from the data reader and posts it to a server via POST request.
     * data - The data you want to send
     * endpoint - The server's address
     * output - writes the server's response to output
     * @throws Exception
     */
    public static void postData(String data, URL endpoint) throws Exception
    {
        HttpURLConnection urlc = null;

        urlc = (HttpURLConnection) endpoint.openConnection();
        urlc.setRequestMethod("POST");
        urlc.setDoOutput(true);
        urlc.setDoInput(true);
        urlc.setUseCaches(false);
        urlc.setAllowUserInteraction(false);
        urlc.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        urlc.setRequestProperty("Content-Length", Integer.toString(data.length()));

        OutputStream out = urlc.getOutputStream();

        OutputStreamWriter outStream = new OutputStreamWriter(out, "UTF-8");

        log.trace("Sending data: " + data);
        outStream.write(data);
        outStream.close();


        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        
        log.trace("Response code: " + urlc.getResponseCode());
        
        log.trace("result: " + result);
        
        bufferedReader.close();
    }

}
