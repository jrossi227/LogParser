package ca.apachegui.sendlog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
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
		String url="http://localhost:" + ReadInput.port + "/ApacheGUI/ReceiveLogData";
		HttpURLConnection   urlConn;
		DataOutputStream    printout;
		try 
		{
			String data = "logData=" + URLEncoder.encode(logData, "UTF-8");
			StringWriter output = new StringWriter();
			
			log.trace("Sending: " + data);
			HTTPRequestPoster.postData(new StringReader(data), new URL(url), output, "application/x-www-form-urlencoded; charset=UTF-8");
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
