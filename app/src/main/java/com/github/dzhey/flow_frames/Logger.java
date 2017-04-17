package com.github.dzhey.flow_frames;

import com.github.dzhey.flow_frames.internal.TraversalLoggerImpl;

/**
 * @author Eugene Byzov gdzhey@gmail.com
 *         Created on 17.04.17.
 *
 */
public final class Logger {

    private static TraversalLogger sLogger = new TraversalLoggerImpl();

    public static TraversalLogger getLogger() {
        return sLogger;
    }

    public static void setLogger(TraversalLogger logger) {
        sLogger = logger;
    }

    public static void trace(Object tag, String msg, Object... args) {
        sLogger.trace(tag, msg, args);
    }

    public static void debug(Object tag, String msg, Object... args) {
        sLogger.debug(tag, msg, args);
    }

    public static void warn(Object tag, String msg, Object... args) {
        sLogger.warn(tag, msg, args);
    }

    public static void warn(Object tag, Object object) {
        sLogger.warn(tag, object);
    }

    private Logger() {
    }
}
