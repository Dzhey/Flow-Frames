package com.github.dzhey.flow_frames.traversal;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.github.dzhey.flow_frames.BasicHistoryFrame;
import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.Logger;
import com.github.dzhey.flow_frames.R;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;
import com.github.dzhey.flow_frames.utils.ScreenViewUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * View changer which simply adds and removes views
 * without animations to immediately reflect changes
 * </p>
 * <p>
 * Subclasses may override it's methods to manually change views.
 * <br>
 * Don't forget to call
 * {@link com.github.dzhey.flow_frames.traversal.pathnodes.PathNode.OnAppliedCallback} once
 * necessary changes are performed.
 * </p>
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 25-Sep-16.
 */
public class SimpleTraversalFrameChanger implements TraversalFrameChanger {

    @Override
    public void performViewChanges(TraversalContext traversalContext,
                                   @Nullable HistoryFrame outgoingFrame,
                                   HistoryFrame incomingFrame,
                                   PathNode.OnAppliedCallback onAppliedCallback) {

        TraversalFrameChanger delegate = null;

        switch (traversalContext.getDirection()) {
            case FORWARD:
                if (incomingFrame instanceof BasicHistoryFrame) {
                    delegate = ((BasicHistoryFrame) incomingFrame).getEnterFrameChanger();
                }
                break;
            case BACKWARD:
                if (outgoingFrame instanceof BasicHistoryFrame) {
                    delegate = ((BasicHistoryFrame) outgoingFrame).getExitFrameChanger();
                }
                break;

            case REPLACE:
                if (incomingFrame instanceof BasicHistoryFrame) {
                    delegate = ((BasicHistoryFrame) incomingFrame).getReplaceFrameChanger();
                }

            default:
                break;
        }

        if (delegate != null && delegate != this) {
            delegate.performViewChanges(traversalContext,
                    outgoingFrame,
                    incomingFrame,
                    onAppliedCallback);

            return;
        }

        final Map<Integer, SwapTransitionSpec> pendingTransitions = new LinkedHashMap<>();

        if (outgoingFrame != null) {
            final List<InflatedLayoutMapping> outgoingMappings = getInflatedMappingsFromScreens(
                    traversalContext, outgoingFrame.getScreens());

            updateViews(outgoingMappings, pendingTransitions, true);
        }

        final List<InflatedLayoutMapping> incomingMappings = getInflatedMappingsFromScreens(
                traversalContext, incomingFrame.getScreens());

        updateViews(incomingMappings, pendingTransitions, false);

        final Collection<TransitionSpec> pendingTransitionValues =
                new ArrayList<>(pendingTransitions.values().size());
        pendingTransitionValues.addAll(pendingTransitions.values());

        performPendingViewChanges(traversalContext,
                traversalContext.getContainerView(),
                outgoingFrame,
                incomingFrame,
                pendingTransitionValues,
                onAppliedCallback);
    }

    protected void performPendingViewChanges(TraversalContext traversalContext,
                                             ViewGroup rootView,
                                             @Nullable HistoryFrame outgoingFrame,
                                             HistoryFrame incomingFrame,
                                             Collection<TransitionSpec> pendingTransitions,
                                             PathNode.OnAppliedCallback onAppliedCallback) {

        if (pendingTransitions.isEmpty()) {
            onAppliedCallback.onApplied();
            return;
        }

        performTransitions(traversalContext, pendingTransitions, onAppliedCallback);
    }

    protected View findOutgoingViewChecked(Collection<TransitionSpec> pendingTransitions,
                                           @LayoutRes int viewLayoutId) {

        final View view = findOutgoingView(pendingTransitions, viewLayoutId);

        if (view == null) {
            throw new IllegalArgumentException("can't find specified outgoing view");
        }

        return view;
    }

    protected @Nullable View findOutgoingView(Collection<TransitionSpec> pendingTransitions,
                                              @LayoutRes int viewLayoutId) {

        for (TransitionSpec spec : pendingTransitions) {
            for (InflatedLayoutMapping mapping : spec.outgoingMappings()) {
                if (mapping.layoutId() == viewLayoutId) {
                    return mapping.getView();
                }
            }
        }

        return null;
    }

    protected View findIncomingViewChecked(Collection<TransitionSpec> pendingTransitions,
                                           @LayoutRes int viewLayoutId) {

        final View view = findIncomingView(pendingTransitions, viewLayoutId);

        if (view == null) {
            throw new IllegalArgumentException("can't find specified incoming view");
        }

        return view;
    }

    protected @Nullable View findIncomingView(Collection<TransitionSpec> pendingTransitions,
                                              @LayoutRes int viewLayoutId) {

        for (TransitionSpec spec : pendingTransitions) {
            for (InflatedLayoutMapping mapping : spec.incomingMappings()) {
                if (mapping.layoutId() == viewLayoutId) {
                    return mapping.getView();
                }
            }
        }

        return null;
    }

    protected Collection<View> getAddedViews(TransitionSpec spec) {
        final Set<View> addedViews = new LinkedHashSet<>();

        for (InflatedLayoutMapping mapping : spec.incomingMappings()) {
            addedViews.add(mapping.getView());
        }

        // if view presented as both outgoing and incoming then it should not be re-added
        for (InflatedLayoutMapping mapping : spec.outgoingMappings()) {
            addedViews.remove(mapping.getView());
        }

        return addedViews;
    }

    protected Collection<View> getAddedViews(Collection<TransitionSpec> pendingTransitions) {
        final Set<View> addedViews = new LinkedHashSet<>();

        for (TransitionSpec spec : pendingTransitions) {
            addedViews.addAll(getAddedViews(spec));
        }

        return addedViews;
    }

    protected Collection<View> getRemovedViews(TransitionSpec spec) {
        final Set<View> removedViews = new LinkedHashSet<>();

        for (InflatedLayoutMapping mapping : spec.outgoingMappings()) {
            removedViews.add(mapping.getView());
        }

        // if view presented as both outgoing and incoming then it should not be removed
        for (InflatedLayoutMapping mapping : spec.incomingMappings()) {
            removedViews.remove(mapping.getView());
        }

        return removedViews;
    }

    protected Collection<View> getRemovedViews(Collection<TransitionSpec> pendingTransitions) {
        final Set<View> removedViews = new LinkedHashSet<>();

        for (TransitionSpec spec : pendingTransitions) {
            removedViews.addAll(getRemovedViews(spec));
        }

        return removedViews;
    }

    protected void performTransitions(final TraversalContext traversalContext,
                                      final Collection<TransitionSpec> pendingTransitions,
                                      final PathNode.OnAppliedCallback onAppliedCallback) {

        for (TransitionSpec transitionSpec : pendingTransitions) {
            addViews(traversalContext.getContainerView(), transitionSpec);
        }

        // remove views in reverse order to prevent
        // removing container views before children
        final TransitionSpec[] specs = new TransitionSpec[pendingTransitions.size()];

        pendingTransitions.toArray(specs);

        for (int i = 0; i < specs.length; i++) {
            removeViews(traversalContext.getContainerView(),
                    specs[specs.length - i - 1]);
        }

        Logger.trace(this, "screen views swap finished");
        onAppliedCallback.onApplied();
    }

    protected void addViews(ViewGroup root, TransitionSpec spec) {
        final Collection<View> addedViews = getAddedViews(spec);

        final ViewGroup container = findContainerView(root, spec.containerId());

        for (View view : addedViews) {
            // view already added
            if (view.getParent() == container) {
                continue;
            }

            container.addView(view);
        }
    }

    protected List<View> removeViews(ViewGroup root, Collection<TransitionSpec> specs) {
        final ArrayList<View> removed = new ArrayList<>();
        for (TransitionSpec spec : specs) {
            final Collection<View> viewsToRemove = getRemovedViews(spec);

            if (viewsToRemove.isEmpty()) {
                continue;
            }
            for (View view : viewsToRemove) {
                ViewParent parent = view.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(view);
                    removed.add(view);
                }
            }
        }
        return removed;
    }

    protected @Nullable ViewGroup tryFindContainerView(
            ViewGroup rootView, @IdRes int containerId) {

        if (containerId == 0) {
            return rootView;
        }

        if (containerId == rootView.getId()) {
            return rootView;
        }

        return (ViewGroup) rootView.findViewById(containerId);
    }

    protected void removeViews(ViewGroup root, TransitionSpec spec) {
        final Collection<View> removedViews = getRemovedViews(spec);

        if (removedViews.isEmpty()) {
            return;
        }
        boolean viewsDetached = true;
        for (View view : removedViews) {
            viewsDetached = viewsDetached && !view.isAttachedToWindow() && view.getParent() == null;
        }
        if (viewsDetached) return;

        final ViewGroup container = findContainerView(root, spec.containerId());

        for (View view : removedViews) {
            container.removeView(view);
        }
    }

    protected void hideViews(TransitionSpec spec) {
        for (View view : getRemovedViews(spec)) {
            view.setVisibility(View.GONE);
        }
    }

    protected ViewGroup findContainerView(ViewGroup rootView, @IdRes int containerId) {
        final ViewGroup container = tryFindContainerView(rootView, containerId);
        if (container == null) {
            throw new IllegalArgumentException(String.format(
                "unable to find container for views inside %s", rootView));
        }
        return container;
    }

    private void updateViews(Collection<InflatedLayoutMapping> mappings,
                             Map<Integer, SwapTransitionSpec> pendingTransitions,
                             boolean outgoing) {

        for (InflatedLayoutMapping mapping : mappings) {
            final View mappedView = mapping.getView();

            if (mappedView == null) {
                continue;
            }

            final SwapTransitionSpec transitionSpec;
            if (pendingTransitions.containsKey(mapping.containerId())) {
                transitionSpec = pendingTransitions.get(mapping.containerId());

            } else {
                transitionSpec = new SwapTransitionSpec(mapping.containerId());
                pendingTransitions.put(mapping.containerId(), transitionSpec);
            }

            if (outgoing) {
                transitionSpec.addOutgoingMapping(mapping);

            } else {
                transitionSpec.addIncomingMapping(mapping);
            }
        }
    }

    protected List<InflatedLayoutMapping> getInflatedMappingsFromScreens(
            TraversalContext traversalContext, Collection<Screen> screens) {

        final List<InflatedLayoutMapping> result = new ArrayList<>();

        for (Screen screen : screens) {
            result.addAll(getInflatedMappingsFromScreen(traversalContext, screen));
        }

        return result;
    }

    protected List<InflatedLayoutMapping> getInflatedMappingsFromScreen(
            TraversalContext traversalContext, Screen screen) {

        final ViewGroup rootView = traversalContext.getContainerView();
        final LayoutSpec layoutSpec = screen.getLayoutSpec();
        final List<InflatedLayoutMapping> mappings =
                new ArrayList<>(layoutSpec.getLayoutMappings().size());

        for (LayoutSpec.LayoutMapping mapping : layoutSpec.getLayoutMappings()) {
            if (mapping.layoutId() == 0) {
                mappings.add(new InflatedLayoutMapping(
                        null, mapping.containerId(), mapping.layoutId(), mapping.layoutTag()));

                continue;
            }

            final InflatedLayoutMapping inflatedMapping =
                    traversalContext.findInflatedLayoutMapping(mapping);

            if (inflatedMapping != null) {
                // view already registered within traversal context
                mappings.add(inflatedMapping);
                continue;
            }

            final ViewGroup containerView;
            if (mapping.containerId() == 0) {
                containerView = rootView;
            } else {
                containerView = (ViewGroup) rootView.findViewById(mapping.containerId());
            }

            if (containerView == null) {
                throw new IllegalStateException(String.format(
                        "unable to find view container for screen %s view", screen));
            }

            final View mappedView = ScreenViewUtils.findViewByTag(containerView,
                    R.id.__screens_key_changer_view_layout_id,
                    ScreenViewUtils.makeViewLayoutTag(mapping));

            mappings.add(new InflatedLayoutMapping(mappedView,
                    mapping.containerId(), mapping.layoutId(), mapping.layoutTag()));
        }

        return mappings;
    }

    static class SwapTransitionSpec implements TransitionSpec {

        private final int mContainerId;
        private final ArrayList<InflatedLayoutMapping> mOutgoingMappings;
        private final ArrayList<InflatedLayoutMapping> mIncomingMappings;

        public SwapTransitionSpec(int containerId) {
            mContainerId = containerId;
            mOutgoingMappings = new ArrayList<>();
            mIncomingMappings = new ArrayList<>();
        }

        @Override
        public Collection<InflatedLayoutMapping> outgoingMappings() {
            return mOutgoingMappings;
        }

        @Override
        public Collection<InflatedLayoutMapping> incomingMappings() {
            return mIncomingMappings;
        }

        @Override
        public int containerId() {
            return mContainerId;
        }

        private void addIncomingMapping(InflatedLayoutMapping mapping) {
            mIncomingMappings.add(mapping);
        }

        private void addOutgoingMapping(InflatedLayoutMapping mapping) {
            mOutgoingMappings.add(mapping);
        }

        private boolean hasIncomingMapping(InflatedLayoutMapping mapping) {
            return mIncomingMappings.contains(mapping);
        }

        private boolean hasOutgoingMapping(InflatedLayoutMapping mapping) {
            return mOutgoingMappings.contains(mapping);
        }

        public boolean hasIncomingView(View view) {
            if (view == null) {
                return false;
            }

            for (InflatedLayoutMapping mapping : mIncomingMappings) {
                if (mapping.getView() == view) {
                    return true;
                }
            }

            return false;
        }

        public boolean hasOutgoingView(View view) {
            if (view == null) {
                return false;
            }

            for (InflatedLayoutMapping mapping : mOutgoingMappings) {
                if (mapping.getView() == view) {
                    return true;
                }
            }

            return false;
        }
    }
}
