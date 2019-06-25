package com.github.dzhey.flow_frames.traversal;

import androidx.annotation.Nullable;

import com.github.dzhey.flow_frames.HistoryFrame;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 25-Sep-16.
 */
public interface TraversalFrameChangerResolver {
    TraversalFrameChanger resolveTraversalFrameChanger(TraversalContext traversalContext,
                                                       @Nullable HistoryFrame outgoingHistoryFrame,
                                                       HistoryFrame incomingHistoryFrame);
}
