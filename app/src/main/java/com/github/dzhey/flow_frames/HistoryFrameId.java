package com.github.dzhey.flow_frames;

/**
 * Identifies each {@link HistoryFrame} through {@link #equals(Object)}
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 26-Sep-16.
 */
public interface HistoryFrameId {
    HistoryFrameId NONE = new HistoryFrameId() {
        @Override
        public boolean equals(Object obj) {
            return obj == this;

        }

        @Override
        public int hashCode() {
            return 0;
        }
    };
}
