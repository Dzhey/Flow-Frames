package com.github.dzhey.flow_frames;

/**
 * Creates {@link HistoryFrameId} instances
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 26-Sep-16.
 */
public interface HistoryFrameIdGenerator {
    HistoryFrameId generateHistoryFrameId(HistoryFrame historyFrame);
}
