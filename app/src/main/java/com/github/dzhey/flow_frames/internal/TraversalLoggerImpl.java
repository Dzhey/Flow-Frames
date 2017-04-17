package com.github.dzhey.flow_frames.internal;

import android.util.Log;

import com.github.dzhey.flow_frames.Consts;
import com.github.dzhey.flow_frames.LogLevel;
import com.github.dzhey.flow_frames.TraversalLogger;

import java.util.Locale;

/**
 * @author Eugene Byzov gdzhey@gmail.com
 *         Created on 17.04.17.
 */
public class TraversalLoggerImpl implements TraversalLogger {

    private @LogLevel
    int mLogLevel = Consts.LOG_LEVEL;

    @Override
    public void trace(Object tag, String msg, Object... args) {
        if (mLogLevel >= Consts.LOG_LEVEL_TRACE) {
            Log.v(tag.getClass().getName(), String.format(Locale.US, msg, args));
        }
    }

    @Override
    public void debug(Object tag, String msg, Object... args) {
        if (mLogLevel >= Consts.LOG_LEVEL_DEBUG) {
            Log.d(tag.getClass().getName(), String.format(Locale.US, msg, args));
        }
    }

    @Override
    public void warn(Object tag, String msg, Object... args) {
        if (mLogLevel >= Consts.LOG_LEVEL_WARN) {
            Log.w(tag.getClass().getName(), String.format(Locale.US, msg, args));
        }
    }

    @Override
    public void warn(Object tag, Object object) {
        if (mLogLevel >= Consts.LOG_LEVEL_WARN) {
            Log.w(tag.getClass().getName(), String.valueOf(object));
        }
    }

    @Override
    public void setLevel(@LogLevel int level) {
        mLogLevel = level;
    }

    @Override
    public @LogLevel
    int getLogLevel() {
        return mLogLevel;
    }
}
