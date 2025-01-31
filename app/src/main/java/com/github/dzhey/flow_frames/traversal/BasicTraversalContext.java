package com.github.dzhey.flow_frames.traversal;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.github.dzhey.flow_frames.IScreenScoper;
import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.RetainedViewPool;
import com.github.dzhey.flow_frames.ScreenScoper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import flow.Direction;
import flow.State;
import flow.TraversalCallback;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class BasicTraversalContext implements TraversalCallback, TraversalContext {

    private ViewGroup mContainerView;
    private State mIncomingState;
    private State mOutgoingState;
    private Direction mDirection;
    private Map<Object, Context> mIncomingContexts;
    private TraversalCallback mTraversalCallback;
    private IScreenScoper mScreenScoper;
    private boolean mIsDestroyed;
    private Set<String> mAcquisitionTags;
    private Map<Object, Object> mTraversalData;
    private List<InflatedLayoutMapping> mInflatedLayoutMappings;
    private @Nullable RetainedViewPool mRetainedViewPool;

    public BasicTraversalContext(ViewGroup container,
                                 State outgoingState,
                                 State incomingState,
                                 Direction direction,
                                 Map<Object, Context> incomingContexts,
                                 TraversalCallback traversalCallback,
                                 IScreenScoper screenScoper) {

        mContainerView = container;
        mIncomingState = incomingState;
        mOutgoingState = outgoingState;
        mDirection = direction;
        mIncomingContexts = incomingContexts;
        mTraversalCallback = traversalCallback;
        mScreenScoper = screenScoper;
        mAcquisitionTags = new HashSet<>();
        mTraversalData = new HashMap<>();
        mInflatedLayoutMappings = new ArrayList<>();
    }

    @Override
    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    @Override
    public void destroy() {
        if (!mAcquisitionTags.isEmpty()) {
            throw new IllegalStateException(
                    "unable to destroy context; traversal context is still held");
        }

        mInflatedLayoutMappings = null;
        mTraversalData = null;
        mIncomingContexts = null;
        mIncomingState = null;
        mOutgoingState = null;
        mTraversalCallback = null;
        mDirection = null;
        mScreenScoper = null;
        mAcquisitionTags = null;
        mIsDestroyed = true;
        mRetainedViewPool = null;
    }

    public void destroyIfReleased() {
        if (mAcquisitionTags.isEmpty()) {
            destroy();
        }
    }

    @Override
    public void onTraversalCompleted() {
        mTraversalCallback.onTraversalCompleted();
    }

    @Override
    public State getIncomingState() {
        return mIncomingState;
    }

    protected void setIncomingState(State incomingState) {
        mIncomingState = incomingState;
    }

    @Override
    public State getOutgoingState() {
        return mOutgoingState;
    }

    protected void setOutgoingState(State outgoingState) {
        mOutgoingState = outgoingState;
    }

    @Override
    public Direction getDirection() {
        return mDirection;
    }

    protected void setDirection(Direction direction) {
        mDirection = direction;
    }

    @Override
    public Map<Object, Context> getIncomingContexts() {
        return mIncomingContexts;
    }

    protected void setIncomingContexts(Map<Object, Context> incomingContexts) {
        mIncomingContexts = incomingContexts;
    }

    @Override
    public TraversalCallback getTraversalCallback() {
        return this;
    }

    protected void setTraversalCallback(TraversalCallback traversalCallback) {
        mTraversalCallback = traversalCallback;
    }

    @Override
    public ViewGroup getContainerView() {
        return mContainerView;
    }

    protected void setContainerView(ViewGroup containerView) {
        mContainerView = containerView;
    }

    @Override
    public IScreenScoper getScreenScoper() {
        return mScreenScoper;
    }

    @Override
    public void acquire(String tag) {
        mAcquisitionTags.add(tag);
    }

    @Override
    public void release(String tag) {
        mAcquisitionTags.remove(tag);

        destroyIfReleased();
    }

    @Override
    public TraversalPath getTraversalPath() {
        throw new UnsupportedOperationException();
    }

    protected void setScreenScoper(ScreenScoper screenScoper) {
        mScreenScoper = screenScoper;
    }

    @Override
    public boolean hasTraversalValue(Object key) {
        return mTraversalData.containsKey(key);
    }

    @Override
    public <T> T getTraversalValue(Object key) {
        //noinspection unchecked
        return (T) mTraversalData.get(key);
    }

    @Override
    public <T> T getTraversalValue(Object key, T defaultValue) {
        if (mTraversalData.containsKey(key)) {
            return getTraversalValue(key);
        }

        return defaultValue;
    }

    @Override
    public void setTraversalValue(Object key, Object value) {
        mTraversalData.put(key, value);
    }

    @Override
    public void registerInflatedLayoutMapping(InflatedLayoutMapping mapping) {
        mInflatedLayoutMappings.add(mapping);
    }

    @Override
    public @Nullable InflatedLayoutMapping findInflatedLayoutMapping(
            LayoutSpec.LayoutMapping mapping) {

        for (InflatedLayoutMapping layoutMapping : mInflatedLayoutMappings) {
            if (mapping.containerId() == layoutMapping.containerId()
                    && mapping.layoutId() == layoutMapping.layoutId()) {

                //noinspection ConstantConditions
                if ((mapping.layoutTag() == null && layoutMapping.layoutTag() == null)
                        || (mapping.layoutTag() != null
                                && mapping.layoutTag().equals(layoutMapping.layoutTag()))
                        || (layoutMapping.layoutTag() != null
                                && layoutMapping.layoutTag().equals(mapping.layoutTag()))) {

                    return layoutMapping;
                }
            }
        }

        return null;
    }

    @Override
    public Collection<InflatedLayoutMapping> getInflatedLayoutMappings() {
        return Collections.unmodifiableCollection(mInflatedLayoutMappings);
    }

    public void setRetainedViewPool(@Nullable RetainedViewPool retainedViewPool) {
        mRetainedViewPool = retainedViewPool;
    }

    @Nullable
    @Override
    public RetainedViewPool getRetainedViewPool() {
        return mRetainedViewPool;
    }
}
