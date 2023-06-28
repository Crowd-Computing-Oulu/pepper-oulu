package fi.oulu.danielszabo.pepper.log;

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

        return null;
    }
}
