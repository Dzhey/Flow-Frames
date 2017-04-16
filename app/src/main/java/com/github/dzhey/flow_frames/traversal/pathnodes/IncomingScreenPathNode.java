package com.github.dzhey.flow_frames.traversal.pathnodes;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public abstract class IncomingScreenPathNode extends ScreenPathNode {

    public IncomingScreenPathNode(String name) {
        super(name);
    }

    @Override
    protected HistoryFrame getHistoryFrame(TraversalContext traversalContext) {
        return traversalContext.getIncomingState().getKey();
    }

    /**
     * Check whether specified incoming <code>screen</code> should be added as a result
     * of traversal. Thus specified screen is not present in outgoing state.
     *
     * @param traversalContext current traversal context
     * @param screen incoming screen
     * @return true if specified <code>screen</code> should be added
     */
    protected boolean isScreenChanges(TraversalContext traversalContext, Screen screen) {
        final HistoryFrame incomingFrame = getHistoryFrame(traversalContext);

        if (incomingFrame == null || !incomingFrame.getScreens().contains(screen)) {
            throw new IllegalArgumentException("specified screen is not incoming screen");
        }

        if (traversalContext.getOutgoingState() == null) {
            return true;
        }

        final HistoryFrame outgoingFrame = traversalContext.getOutgoingState().getKey();

        return !outgoingFrame.getScreens().contains(screen);
    }
}
