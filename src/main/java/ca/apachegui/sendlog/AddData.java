package ca.apachegui.sendlog;

import java.util.ArrayList;
import java.util.Date;

import net.apachegui.db.LogData;
import net.apachegui.db.LogDataDao;
import net.apachegui.db.SettingsDao;
import net.apachegui.db.Timestamp;
import org.apache.log4j.Logger;


public class AddData extends Thread {
    private static Logger log = Logger.getLogger(AddData.class);
    private String logData;

    public static int buffer = 0;
    public static ArrayList<LogData> currentLogData = new ArrayList<LogData>();

    public AddData(String logData) {
        this.logData = logData;
    }

    public void run() {

        try {
            String historyBuffer = SettingsDao.getInstance().getSetting("historyBuffer");

            //ApacheGUI isn't setup if this happens
            if (historyBuffer == null) {
                return;
            }

            synchronized (AddData.class) {
                buffer++;

                currentLogData.add(extractLogDataFromApacheStatement());

                if (buffer >= Integer.parseInt(historyBuffer)) {
                    buffer = 0;
                    LogDataDao.getInstance().commitLogData(currentLogData.toArray(new LogData[currentLogData.size()]));
                    currentLogData.clear();
                }
            }


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private LogData extractLogDataFromApacheStatement() throws Exception {
        String fields[] = logData.split("\",\"");

        Date date = new Date();
        Timestamp insertDate = new Timestamp(date.getTime());
        log.trace("insertDate " + insertDate.toString());

        String host = "";
        if (fields.length > 0) {
            host = fields[0];
        }
        log.trace("Host " + host);

        String userAgent = "";
        if (fields.length > 1) {
            userAgent = fields[1];
        }
        log.trace("userAgent " + userAgent);

        String requestString = "";
        if (fields.length > 2) {
            requestString = fields[2];
        }
        log.trace("requestString " + requestString);

        String status = "";
        if (fields.length > 3) {
            status = fields[3];
        }
        log.trace("status " + status);

        String contentSize = "";
        if (fields.length > 4) {
            contentSize = fields[4];
        }
        log.trace("contentSize " + contentSize);

        return new LogData(insertDate, host, userAgent, requestString, status, contentSize);
    }

}
