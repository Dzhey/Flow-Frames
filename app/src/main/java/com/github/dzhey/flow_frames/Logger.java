package com.github.dzhey.flow_frames;

import android.util.Log;

import java.util.Locale;

/**
 * @author Eugene Byzov gdzhey@gmail.com
 *         Created on 17.04.17.
 *
 */
public final class Logger {

    public static void trace(Object tag, String msg, Object... args) {
        if (Consts.LOG_LEVEL >= Consts.LOG_LEVEL_TRACE) {
            Log.v(tag.getClass().getName(), String.format(Locale.US, msg, args));
        }
    }

    public static void debug(Object tag, String msg, Object... args) {
        if (Consts.LOG_LEVEL >= Consts.LOG_LEVEL_DEBUG) {
            Log.d(tag.getClass().getName(), String.format(Locale.US, msg, args));
        }
    }

    public static void warn(Object tag, String msg, Object... args) {
        if (Consts.LOG_LEVEL >= Consts.LOG_LEVEL_WARN) {
            Log.w(tag.getClass().getName(), String.format(Locale.US, msg, args));
        }
    }

    public static void warn(Object tag, Object object) {
        if (Consts.LOG_LEVEL >= Consts.LOG_LEVEL_WARN) {
            Log.w(tag.getClass().getName(), String.valueOf(object));
        }
    }

    private Logger() {
    }
}
