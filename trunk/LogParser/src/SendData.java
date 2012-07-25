import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
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
		URLConnection   urlConn;
		DataOutputStream    printout;
		try 
		{
			String data = "logData=" + URLEncoder.encode(logData, "UTF-8");
			urlConn=new URL(url).openConnection();
		    log.trace("Connected");
		    urlConn.setDoOutput(true);
		    log.trace("Sending " + data);
		    OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		    String line;
		    log.trace("Reading Response");
		    while ((line = rd.readLine()) != null) 
		    {
		        log.trace("line");
		    }
		    wr.close();
		    rd.close();
		} 
		catch (IOException e) 
		{
		    log.trace("ApacheGUI is not running, unable to log requests");
		    ReadInput.port= ReadInput.getTomcatPortFromConfigXml(new File(ReadInput.serverFile));
		    log.trace("Reading port again: " + ReadInput.port);
		}

	}
}
