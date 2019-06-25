package com.github.dzhey.flow_frames;

import androidx.annotation.IntDef;

/**
 * @author Eugene Byzov gdzhey@gmail.com
 *         Created on 18.04.17.
 */
@IntDef({
        Consts.LOG_LEVEL_TRACE,
        Consts.LOG_LEVEL_DEBUG,
        Consts.LOG_LEVEL_WARN,
        Consts.LOG_LEVEL_DISABLE
})
public @interface LogLevel {
}
