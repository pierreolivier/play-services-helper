package com.playserviceshelper.lib.utils;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class Logger {
    public interface LoggerInterface {
        void e(String t, String s);
        void v(String t, String s);
    }
    private static LoggerInterface mLogger;

    public static void e(String t, String s) {
        mLogger.e(t, s);
    }

    public static void v(String t, String s) {
        mLogger.v(t, s);
    }

    public static void setLogger(LoggerInterface mLogger) {
        Logger.mLogger = mLogger;
    }
}
