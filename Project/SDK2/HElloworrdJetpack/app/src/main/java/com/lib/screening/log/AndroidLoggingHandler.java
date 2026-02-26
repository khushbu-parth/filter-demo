package com.lib.screening.log;

import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class AndroidLoggingHandler extends Handler {
    public static void injectJavaLogger() {
        Logger logger = LogManager.getLogManager().getLogger("");
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger.addHandler(new AndroidLoggingHandler());
        Logger.getLogger("my.category").setLevel(Level.FINEST);
    }

    static int getAndroidLevel(Level level) {
        int intValue = level.intValue();
        if (intValue >= Level.SEVERE.intValue()) {
            return 6;
        }
        if (intValue >= Level.WARNING.intValue()) {
            return 5;
        }
        return intValue >= Level.INFO.intValue() ? 4 : 3;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord logRecord) {
        if (!super.isLoggable(logRecord)) {
            return;
        }
        String loggerName = logRecord.getLoggerName();
        if (loggerName.length() > 30) {
            loggerName = loggerName.substring(loggerName.length() - 30);
        }
        try {
            int androidLevel = getAndroidLevel(logRecord.getLevel());
            Log.println(androidLevel, loggerName, logRecord.getMessage());
            if (logRecord.getThrown() == null) {
                return;
            }
            Log.println(androidLevel, loggerName, Log.getStackTraceString(logRecord.getThrown()));
        } catch (RuntimeException e) {
            Log.e("AndroidLoggingHandler", "Error logging message.", e);
        }
    }
}
