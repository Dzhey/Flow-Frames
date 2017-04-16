package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.support.annotation.Nullable;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public abstract class OutgoingScreenPathNode extends ScreenPathNode {

    public OutgoingScreenPathNode(String name) {
        super(name);
    }

    @Nullable
    @Override
    protected HistoryFrame getHistoryFrame(TraversalContext traversalContext) {
        if (traversalContext.getOutgoingState() != null) {
            return traversalContext.getOutgoingState().getKey();
        }

        return null;
    }

    /**
     * Check whether specified outgoing <code>screen</code> will be removed as a result of traversal
     *
     * @param traversalContext current traversal context
     * @param screen outgoing screen
     * @return true if specified <code>screen</code> will be removed
     */
    protected boolean isScreenChanges(TraversalContext traversalContext, Screen screen) {
        final HistoryFrame outgoingFrame = getHistoryFrame(traversalContext);

        if (outgoingFrame == null || !outgoingFrame.getScreens().contains(screen)) {
            throw new IllegalArgumentException("specified screen is not outgoing screen");
        }

        final HistoryFrame incomingFrame = traversalContext.getIncomingState().getKey();

        return !incomingFrame.getScreens().contains(screen);
    }
}
