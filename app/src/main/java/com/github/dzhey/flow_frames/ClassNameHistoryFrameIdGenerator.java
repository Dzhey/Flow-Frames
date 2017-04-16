package com.github.dzhey.flow_frames;

/**
 * Generate {@link HistoryFrameId} for each {@link HistoryFrame} using screen class names
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 26-Sep-16.
 */
public class ClassNameHistoryFrameIdGenerator implements HistoryFrameIdGenerator {

    @Override
    public HistoryFrameId generateHistoryFrameId(HistoryFrame historyFrame) {
        return new ClassNameHistoryFrameId.Builder()
                .withHistoryFrame(historyFrame)
                .build();
    }
}
