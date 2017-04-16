package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.InflatedLayoutMapping;
import com.github.dzhey.flow_frames.traversal.TraversalContext;
import com.github.dzhey.flow_frames.utils.ScreenViewUtils;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class RestoreIncomingViewsNode extends IncomingScreenPathNode {

    public RestoreIncomingViewsNode() {
        super(PathNode.Names.RESTORE_INCOMING_VIEWS);
    }

    @Override
    protected void onApplyToScreen(TraversalContext traversalContext,
                                   HistoryFrame historyFrame,
                                   Screen screen,
                                   OnAppliedCallback onAppliedCallback) {

        if (!isScreenChanges(traversalContext, screen)) {
            // screen is present as outgoing screen too
            // there are no pending changes, don't need to restore views
            onAppliedCallback.onApplied();
            return;
        }

        final LayoutSpec spec = screen.getLayoutSpec();

        for (LayoutSpec.LayoutMapping mapping : spec.getLayoutMappings()) {
            if (mapping.layoutId() == 0) {
                continue;
            }

            final InflatedLayoutMapping inflated =
                    traversalContext.findInflatedLayoutMapping(mapping);

            if (inflated == null || inflated.getView() == null) {
                throw new IllegalStateException("unable to restore incoming view state: "
                        + "incoming view was not inflated");
            }

            final Bundle incomingStateBundle = traversalContext.getIncomingState().getBundle();
            if (incomingStateBundle != null) {
                final String bundleKey = ScreenViewUtils.makeViewLayoutTag(mapping);
                final SparseArray<Parcelable> hierarchyState =
                        incomingStateBundle.getSparseParcelableArray(bundleKey);

                if (hierarchyState != null) {
                    inflated.getView().restoreHierarchyState(hierarchyState);
                }
            }
        }

        onAppliedCallback.onApplied();
    }
}
