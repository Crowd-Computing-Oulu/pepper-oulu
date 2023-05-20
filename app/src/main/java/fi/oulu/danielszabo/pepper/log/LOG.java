package fi.oulu.danielszabo.pepper.log;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LOG {

    public static List<LogEntry> logEntryList = new ArrayList<>();

    public static Map<String, LogListener> logListenerHashMap = new HashMap<>();


    public static void addListener(String key, LogListener listener) {
        logListenerHashMap.put(key, listener);
    }

    private static void notifyListeners(LogEntry logEntry) {
        for (LogListener listener : logListenerHashMap.values()) {
            listener.accept(logEntry);
        }
    }

    public static void info(Object context, String message) {
        if (context instanceof Class) {
            info(((Class) context).getSimpleName(), message);
        } else {
            info(context.getClass().getSimpleName(), message);
        }
    }

    public static void debug(Object context, String message) {
        if (context instanceof Class) {
            debug(((Class) context).getSimpleName(), message);
        } else {
            debug(context.getClass().getSimpleName(), message);
        }
    }

    public static void warning(Object context, String message) {
        if (context instanceof Class) {
            warning(((Class) context).getSimpleName(), message);
        } else {
            warning(context.getClass().getSimpleName(), message);
        }
    }

    public static void error(Object context, String message) {
        if (context instanceof Class) {
            error(((Class) context).getSimpleName(), message);
        } else {
            error(context.getClass().getSimpleName(), message);
        }
    }

    public static void info(String tag, String message) {
        android.util.Log.i(tag, message);

        LogEntry logEntry = new LogEntry(
                LogEntry.LogLevel.INFO,
                tag,
                message,
                new Date().getTime()
        );

        LOG.logEntryList.add(
                logEntry
        );

        notifyListeners(logEntry);
    }

    public static void debug(String tag, String message) {
        android.util.Log.d(tag, message);

        LogEntry logEntry = new LogEntry(
                LogEntry.LogLevel.DEBUG,
                tag,
                message,
                new Date().getTime()
        );

        LOG.logEntryList.add(
                logEntry
        );

        notifyListeners(logEntry);
    }

    public static void warning(String tag, String message) {
        android.util.Log.w(tag, message);

        LogEntry logEntry = new LogEntry(
                LogEntry.LogLevel.WARNING,
                tag,
                message,
                new Date().getTime()
        );

        LOG.logEntryList.add(
                logEntry
        );

        notifyListeners(logEntry);
    }

    public static void error(String tag, String message) {
        android.util.Log.e(tag, message);

        LogEntry logEntry = new LogEntry(
                LogEntry.LogLevel.ERROR,
                tag,
                message,
                new Date().getTime()
        );

        LOG.logEntryList.add(
                logEntry
        );

        notifyListeners(logEntry);
    }


    public static void removeListener(String key) {
        logListenerHashMap.remove(key);
    }
}
