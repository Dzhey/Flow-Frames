package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.view.View;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.RetainedViewPool;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.InflatedLayoutMapping;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class RetainOutgoingViewsNode extends OutgoingScreenPathNode {

    public RetainOutgoingViewsNode() {
        super(Names.RETAIN_OUTGOING_VIEWS);
    }

    @Override
    protected void onApplyToScreen(TraversalContext traversalContext,
                                   HistoryFrame incomingFrame,
                                   Screen screen,
                                   OnAppliedCallback onAppliedCallback) {

        final RetainedViewPool pool = traversalContext.getRetainedViewPool();
        if (pool == null || !isScreenChanges(traversalContext, screen)) {
            onAppliedCallback.onApplied();
            return;
        }

        final LayoutSpec spec = screen.getLayoutSpec();
        if (spec == null) {
            onAppliedCallback.onApplied();
            return;
        }
        Map<LayoutSpec.LayoutMapping, View> removedViews = getRemovedViews(traversalContext, screen);
        for (Map.Entry<LayoutSpec.LayoutMapping, View> entry : removedViews.entrySet()) {
            pool.retainViewForMapping(screen, entry.getKey(), entry.getValue());
        }

        onAppliedCallback.onApplied();
    }

    private Map<LayoutSpec.LayoutMapping, View> getRemovedViews(
        TraversalContext context, Screen screen) {
        final Map<LayoutSpec.LayoutMapping, View> removedViews = new LinkedHashMap<>();
        final LayoutSpec spec = screen.getLayoutSpec();

        for (LayoutSpec.LayoutMapping mapping : spec.getLayoutMappings()) {
            if (mapping.containerId() == 0) {
                continue;
            }
            final InflatedLayoutMapping inflated = context.findInflatedLayoutMapping(mapping);
            if (inflated == null || inflated.getView() == null) {
                continue;
            }
            if (inflated.getView().getParent() != null) {
                continue;
            }
            removedViews.put(mapping, inflated.getView());
        }
        return removedViews;
    }
}
