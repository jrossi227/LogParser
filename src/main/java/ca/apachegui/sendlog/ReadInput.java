package ca.apachegui.sendlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import net.apachegui.db.SettingsDao;
import org.apache.log4j.Logger;

public class ReadInput {
    private static Logger log = Logger.getLogger(ReadInput.class);
    public static String tomcatDirectory;

    public static void main(String[] args) throws IOException {
        if (!(args.length > 0)) {
            log.info("The tomcat path was not defined");
            return;
        }

        log.info(args[0]);
        tomcatDirectory = args[0];

        if (!new File(tomcatDirectory).exists()) {
            log.info("The tomcat path does not exist");
            return;
        }

        System.setProperty("catalina.base", tomcatDirectory);

        // turn off caching
        SettingsDao.getInstance().setCache(false);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = in.readLine()) != null && s.length() != 0) {
            log.trace("Received Message: " + s);
            Thread logDataThread = new AddData(s);
            logDataThread.run();
        }
    }
}
