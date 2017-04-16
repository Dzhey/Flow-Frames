package com.github.dzhey.flow_frames.traversal.pathnodes;

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

public class RegisterOutgoingViewsNode extends OutgoingScreenPathNode {

    public RegisterOutgoingViewsNode() {
        super(Names.REGISTER_OUTGOING_VIEWS);
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
                    "unable to find scope for outgoing screen %s", screen));
        }

        for (LayoutSpec.LayoutMapping mapping : spec.getLayoutMappings()) {
            if (mapping.layoutId() == 0) {
                continue;
            }

            final ViewGroup container;
            if (mapping.containerId() == 0) {
                container = containerView;
            } else {
                container = (ViewGroup) containerView.findViewById(mapping.containerId());
            }

            if (container == null) {
                throw new IllegalStateException(String.format(
                        "unable to find container view for outgoing screen %s", screen));
            }

            final String layoutTag = ScreenViewUtils.makeViewLayoutTag(mapping);
            final View view = ScreenViewUtils.findViewByTag(container,
                    R.id.__screens_key_changer_view_layout_id,
                    layoutTag);

            traversalContext.registerInflatedLayoutMapping(new InflatedLayoutMapping(
                    view, mapping.containerId(), mapping.layoutId(), mapping.layoutTag()));
        }

        onAppliedCallback.onApplied();
    }
}
