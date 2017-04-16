package com.github.dzhey.flow_frames.traversal.pathnodes;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class DetachOutgoingScreenNode extends OutgoingScreenPathNode {

    public DetachOutgoingScreenNode() {
        super(PathNode.Names.DETACH_OUTGOING_SCREEN);
    }

    @Override
    protected void onApplyToScreen(TraversalContext traversalContext,
                                   HistoryFrame incomingFrame,
                                   Screen screen,
                                   OnAppliedCallback onAppliedCallback) {

        if (isScreenChanges(traversalContext, screen)) {
            screen.onDetach();
        }

        onAppliedCallback.onApplied();
    }
}
