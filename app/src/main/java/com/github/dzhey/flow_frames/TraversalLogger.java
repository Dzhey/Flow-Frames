package com.github.dzhey.flow_frames;

/**
 * @author Eugene Byzov gdzhey@gmail.com
 *         Created on 17.04.17.
 */
public interface TraversalLogger {

    void trace(Object tag, String msg, Object... args);
    void debug(Object tag, String msg, Object... args);
    void warn(Object tag, String msg, Object... args);
    void warn(Object tag, Object object);
    void setLevel(@LogLevel int level);
    @LogLevel int getLogLevel();
}
