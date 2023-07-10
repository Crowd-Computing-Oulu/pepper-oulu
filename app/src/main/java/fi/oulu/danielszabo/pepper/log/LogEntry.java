package fi.oulu.danielszabo.pepper.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {

    public enum LogLevel {
        INFO, DEBUG, WARNING, ERROR;

        public static LogLevel extractLogLevelFromString(String logEntry) {
            if (logEntry.contains("INFO")) {
                return INFO;
            } else if (logEntry.contains("DEBUG")) {
                return DEBUG;
            } else if (logEntry.contains("WARNING")) {
                return WARNING;
            } else if (logEntry.contains("ERROR")) {
                return ERROR;
            }

            return DEBUG; // Default log level if extraction fails
        }
    }

    private LogLevel logLevel;
    private String tag;
    private String message;
    private long time;

    public LogEntry(LogLevel logLevel, String tag, String message, long time) {
        this.logLevel = logLevel;
        this.tag = tag;
        this.message = message;
        this.time = time;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        String formattedTime = dateFormat.format(new Date(time));

        return "[" + formattedTime + " " + logLevel + " " + tag + "] " + message;
    }
}
