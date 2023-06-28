package fi.oulu.danielszabo.pepper.log;


import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {

    enum LogLevel {
        INFO, DEBUG, WARNING, ERROR
    }

    LogLevel logLevel;
    String tag, message;
    long time;

    public LogEntry(LogLevel logLevel, String tag, String message, long time) {
        this.logLevel = logLevel;
        this.tag = tag;
        this.message = message;
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date(this.time);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        simpleDateFormat.format(now, stringBuffer, new FieldPosition(0));

        return "[" + stringBuffer + " " + this.logLevel + " " + this.tag + "] " + this.message;
    }
}
