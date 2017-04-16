package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.support.annotation.Nullable;
import android.view.View;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.InflatedLayoutMapping;
import com.github.dzhey.flow_frames.traversal.TraversalContext;
import com.github.dzhey.flow_frames.traversal.TraversalFrameChanger;
import com.github.dzhey.flow_frames.traversal.TraversalFrameChangerResolver;
import com.github.dzhey.flow_frames.traversal.ViewSwapTraversalListener;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class SwapScreenViewsNode extends BasePathNode {

    private final TraversalFrameChangerResolver mTraversalFrameChangerResolver;

    public SwapScreenViewsNode(TraversalFrameChangerResolver traversalFrameChangerResolver) {
        super(PathNode.Names.SWAP_SCREEN_VIEWS);

        mTraversalFrameChangerResolver = traversalFrameChangerResolver;
    }

    @Override
    public void apply(final TraversalContext traversalContext, final OnAppliedCallback onAppliedCallback) {
        final HistoryFrame outgoingFrame;
        if (traversalContext.getOutgoingState() != null) {
            outgoingFrame = traversalContext.getOutgoingState().getKey();
        } else {
            outgoingFrame = null;
        }

        final HistoryFrame incomingFrame = traversalContext.getIncomingState().getKey();

        final TraversalFrameChanger changer = mTraversalFrameChangerResolver
                .resolveTraversalFrameChanger(traversalContext,
                        outgoingFrame,
                        incomingFrame);

        final Collection<View> outgoingViews = getViews(traversalContext, outgoingFrame, false);
        final Collection<View> incomingViews = getViews(traversalContext, incomingFrame, false);
        final Collection<View> addedViews = new LinkedHashSet<>();

        final Iterator<View> iter = incomingViews.iterator();

        while (iter.hasNext()) {
            final View incomingView = iter.next();

            if (incomingView.getParent() == null) {
                addedViews.add(incomingView);
                iter.remove();
            }

            // if view should be remained after traversal, exclude it from outgoing views
            outgoingViews.remove(incomingView);
        }

        final PathNode.OnAppliedCallback proxyCallback = new OnAppliedCallback() {
            @Override
            public void onApplied() {
                for (View view : outgoingViews) {
                    if (view instanceof ViewSwapTraversalListener) {
                        ((ViewSwapTraversalListener) view).onExitRemoveTraversal(traversalContext);
                    }
                }

                for (View view : incomingViews) {
                    if (view instanceof ViewSwapTraversalListener) {
                        ((ViewSwapTraversalListener) view).onExitTraversal(traversalContext);
                    }
                }

                for (View view : addedViews) {
                    if (view instanceof ViewSwapTraversalListener) {
                        ((ViewSwapTraversalListener) view).onExitAddTraversal(traversalContext);
                    }
                }

                onAppliedCallback.onApplied();
            }
        };

        for (View view : outgoingViews) {
            if (view instanceof ViewSwapTraversalListener) {
                ((ViewSwapTraversalListener) view).onEnterRemoveTraversal(traversalContext);
            }
        }

        for (View view : incomingViews) {
            if (view instanceof ViewSwapTraversalListener) {
                ((ViewSwapTraversalListener) view).onEnterTraversal(traversalContext);
            }
        }

        for (View view : addedViews) {
            if (view instanceof ViewSwapTraversalListener) {
                ((ViewSwapTraversalListener) view).onEnterAddTraversal(traversalContext);
            }
        }

        changer.performViewChanges(traversalContext,
                outgoingFrame,
                incomingFrame,
                proxyCallback);
    }

    private Collection<View> getViews(TraversalContext traversalContext,
                                      @Nullable HistoryFrame frame,
                                      boolean withoutParentOnly) {

        if (frame == null) {
            return Collections.emptySet();
        }

        final LinkedHashSet<View> views = new LinkedHashSet<>();

        for (Screen screen : frame.getScreens()) {
            for (LayoutSpec.LayoutMapping mapping : screen.getLayoutSpec().getLayoutMappings()) {
                final InflatedLayoutMapping inflatedMapping
                        = traversalContext.findInflatedLayoutMapping(mapping);

                if (inflatedMapping != null) {
                    final View view = inflatedMapping.getView();

                    if (view != null) {
                        if (view.getParent() != null && withoutParentOnly) {
                            continue;
                        }

                        views.add(view);
                    }
                }
            }
        }

        return views;
    }
}
