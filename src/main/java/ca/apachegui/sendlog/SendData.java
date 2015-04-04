package ca.apachegui.sendlog;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;


public class SendData extends Thread
{
    private static Logger log = Logger.getLogger(SendData.class);
    private String logData;

    public SendData(String logData)
    {
        this.logData=logData;
    }

    public void run()
    {
        log.trace("new Thread");
        log.trace(logData);
        String url="http://localhost:" + ReadInput.port + "/ApacheGUI/pass/ReceiveLogData";
        log.trace("url: " + url);
        try
        {
            String data = "logData=" + URLEncoder.encode(logData, "UTF-8");

            log.trace("Sending: " + data);
            HTTPRequestPoster.postData(data, new URL(url));
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.trace(sw.toString());
            log.trace("ApacheGUI is not running, unable to log requests");
            ReadInput.port= ReadInput.getTomcatPortFromConfigXml(new File(ReadInput.serverFile));
            log.trace("Reading port again: " + ReadInput.port);
        }

    }


}
