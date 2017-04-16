package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.R;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;
import com.github.dzhey.flow_frames.traversal.ViewSwapTraversalListener;
import com.github.dzhey.flow_frames.utils.ScreenViewUtils;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class SaveOutgoingViewStateNode extends OutgoingScreenPathNode {

    public SaveOutgoingViewStateNode() {
        super(PathNode.Names.SAVE_OUTGOING_VIEWS);
    }

    @Override
    protected void onApplyToScreen(TraversalContext traversalContext,
                                   HistoryFrame historyFrame,
                                   Screen screen,
                                   OnAppliedCallback onAppliedCallback) {

        if (!isScreenChanges(traversalContext, screen)) {
            // Outgoing screen is remained after traversal, no need to save
            onAppliedCallback.onApplied();
            return;
        }

        final List<View> viewsToSave = new ArrayList<>();
        final ViewGroup rootView = traversalContext.getContainerView();
        final LayoutSpec spec = screen.getLayoutSpec();

        for (LayoutSpec.LayoutMapping mapping : spec.getLayoutMappings()) {
            if (mapping.containerId() == 0) {
                viewsToSave.addAll(ScreenViewUtils.findViewsByTag(rootView,
                        R.id.__screens_key_changer_view_layout_id,
                        ScreenViewUtils.makeViewLayoutTag(mapping)));
            } else {
                final ViewGroup layoutContainer =
                        (ViewGroup) rootView.findViewById(mapping.containerId());

                if (layoutContainer == null) {
                    throw new IllegalStateException(
                            "unable to save screen view; can't find view container");
                }

                viewsToSave.addAll(ScreenViewUtils.findViewsByTag(layoutContainer,
                        R.id.__screens_key_changer_view_layout_id,
                        ScreenViewUtils.makeViewLayoutTag(mapping)));
            }
        }

        Bundle outState = traversalContext.getOutgoingState().getBundle();

        if (outState == null) {
            outState = new Bundle();
            traversalContext.getOutgoingState().setBundle(outState);
        }

        for (View view : viewsToSave) {
            SparseArray<Parcelable> state = new SparseArray<>();
            view.saveHierarchyState(state);
            outState.putSparseParcelableArray(ScreenViewUtils.makeViewStateKey(view), state);
        }

        onAppliedCallback.onApplied();
    }
}
