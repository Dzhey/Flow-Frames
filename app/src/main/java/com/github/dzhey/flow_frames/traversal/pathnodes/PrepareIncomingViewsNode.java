package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mortar.MortarScope;
import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.R;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.InflatedLayoutMapping;
import com.github.dzhey.flow_frames.traversal.TraversalContext;
import com.github.dzhey.flow_frames.utils.ScreenViewUtils;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class PrepareIncomingViewsNode extends IncomingScreenPathNode {

    public PrepareIncomingViewsNode() {
        super(Names.PREPARE_INCOMING_VIEWS);
    }

    @Override
    protected void onApplyToScreen(TraversalContext traversalContext,
                                   HistoryFrame historyFrame,
                                   Screen screen,
                                   OnAppliedCallback onAppliedCallback) {

        final LayoutSpec spec = screen.getLayoutSpec();
        final ViewGroup containerView = traversalContext.getContainerView();
        final MortarScope screenScope = MortarScope.findChild(
                containerView.getContext(), screen.getScopeName());

        if (screenScope == null) {
            throw new IllegalStateException(String.format(
                    "unable to find scope for incoming screen %s", screen));
        }

        for (LayoutSpec.LayoutMapping mapping : spec.getLayoutMappings()) {
            if (mapping.layoutId() == 0) {
                continue;
            }

            final ViewGroup container = findContainerForLayoutMapping(traversalContext, mapping);
            final String layoutTag = ScreenViewUtils.makeViewLayoutTag(mapping);
            View view = null;
            if (container != null) {
                view = ScreenViewUtils.findViewByTag(container,
                        R.id.__screens_key_changer_view_layout_id,
                        layoutTag);
            }

            if (view == null) {
                final Context context = screenScope.createContext(containerView.getContext());

                view = LayoutInflater.from(context).inflate(
                        mapping.layoutId(), containerView, false);
                view.setTag(R.id.__screens_key_changer_view_layout_id,
                        layoutTag);
            }

            traversalContext.registerInflatedLayoutMapping(new InflatedLayoutMapping(
                    view, mapping.containerId(), mapping.layoutId(), mapping.layoutTag()));
        }

        onAppliedCallback.onApplied();
    }

    private @Nullable ViewGroup findContainerForLayoutMapping(TraversalContext traversalContext,
                                                              LayoutSpec.LayoutMapping mapping) {
        ViewGroup container;
        if (mapping.containerId() == 0) {
            return traversalContext.getContainerView();

        } else {
            container = (ViewGroup) traversalContext.getContainerView()
                    .findViewById(mapping.containerId());
        }

        if (container == null) {
            // layout container may be inflated previously from another screen
            for (InflatedLayoutMapping inflatedMapping
                    : traversalContext.getInflatedLayoutMappings()) {

                final View inflatedView = inflatedMapping.getView();

                if (inflatedView != null) {
                    final View child = inflatedView.findViewById(mapping.containerId());

                    if (child == null) {
                        continue;
                    }

                    if (!(child instanceof ViewGroup)) {
                        throw new IllegalArgumentException(String.format(
                                "invalid layout mapping found; child should be mapped " +
                                        "to ViewGroup instance but got %s", inflatedView));
                    }

                    container = (ViewGroup) child;
                }
            }
        }

        return container;
    }
}
