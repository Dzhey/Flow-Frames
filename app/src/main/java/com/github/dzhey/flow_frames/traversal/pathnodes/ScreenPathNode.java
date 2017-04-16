package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.support.annotation.Nullable;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

/**
 * Applies the same traversal for each screen defined by history frame
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public abstract class ScreenPathNode extends BasePathNode {

    public ScreenPathNode(String name) {
        super(name);
    }

    @Override
    public void apply(TraversalContext traversalContext,
                      OnAppliedCallback onAppliedCallback) {

        final HistoryFrame frame = getHistoryFrame(traversalContext);

        if (frame == null) {
            onAppliedCallback.onApplied();
            return;
        }

        final OnAppliedCallback compoundCallback = new CompoundOnAppliedCallback(
                frame.getScreens().size(), onAppliedCallback);

        for (Screen screen : frame.getScreens()) {
            onApplyToScreen(traversalContext, frame, screen, compoundCallback);
        }
    }

    protected abstract void onApplyToScreen(TraversalContext traversalContext,
                                            HistoryFrame historyFrame,
                                            Screen screen,
                                            OnAppliedCallback onAppliedCallback);

    protected abstract @Nullable HistoryFrame getHistoryFrame(TraversalContext traversalContext);
}
