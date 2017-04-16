package com.github.dzhey.flow_frames;

import java.util.Collection;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 13-Oct-16.
 */
public interface HistoryFrameFactory {
    HistoryFrame newHistoryFrame(Collection<Screen> screens);
}
