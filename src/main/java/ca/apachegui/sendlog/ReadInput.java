package ca.apachegui.sendlog;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class ReadInput 
{
    private static Logger log = Logger.getLogger(ReadInput.class);
    public static String port;
    public static String serverFile;

    //should pass in the server.xml file
    public static void main(String[] args) throws IOException
    {
        if(!(args.length>0))
        {
            log.info("The server.xml path was not defined");
            return;
        }
        if(!new File(args[0]).exists())
        {
            log.info("The server.xml does not exist");
            return;
        }

        log.info(args[0]);
        serverFile=args[0];
        port=getTomcatPortFromConfigXml(new File(serverFile));
        if(port==null)
        {
            log.info("Unable to find the port that Tomcat is listening on, exiting!!");
            return;
        }

        log.info("port: " + port);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = in.readLine()) != null && s.length() != 0)
        {
            log.trace("Received Message: " + s);
            Thread logDataThread=new SendData(s);
            logDataThread.run();
        }
    }

    public static String getTomcatPortFromConfigXml(File serverXml)
    {
        String port;
        try
        {
           DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
           domFactory.setNamespaceAware(true); // never forget this!
           DocumentBuilder builder = domFactory.newDocumentBuilder();
           Document doc = builder.parse(serverXml);
           XPathFactory factory = XPathFactory.newInstance();
           XPath xpath = factory.newXPath();
           XPathExpression expr = xpath.compile("/Server/Service[@name='Catalina']/Connector[count(@scheme)=0]/@port[1]");
           String result = (String) expr.evaluate(doc, XPathConstants.STRING);
           port =  result != null && result.length() > 0 ? result : null;
        }
        catch (Exception e)
        {
           port = null;
        }
        return port;
    }
}
