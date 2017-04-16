package com.github.dzhey.flow_frames.traversal;

import android.support.annotation.Nullable;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 25-Sep-16.
 */
public interface TraversalFrameChanger {
    void performViewChanges(TraversalContext traversalContext,
                            @Nullable HistoryFrame outgoingFrame,
                            HistoryFrame incomingFrame,
                            PathNode.OnAppliedCallback onAppliedCallback);
}
