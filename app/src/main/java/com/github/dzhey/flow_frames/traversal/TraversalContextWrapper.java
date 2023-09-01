package com.github.dzhey.flow_frames.traversal;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.RetainedViewPool;
import com.github.dzhey.flow_frames.ScreenScoper;

import java.util.Collection;
import java.util.Map;

import flow.Direction;
import flow.State;
import flow.TraversalCallback;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class TraversalContextWrapper implements TraversalContext {

    private TraversalPath mTraversalPath;
    private final TraversalContext mTraversalContext;

    public static TraversalContextWrapper wrap(TraversalContext traversalContext) {
        return new TraversalContextWrapper(traversalContext);
    }

    protected TraversalContextWrapper(TraversalContext traversalContext) {
        mTraversalContext = traversalContext;
    }

    public TraversalContext getTraversalContext() {
        return mTraversalContext;
    }

    @Override
    public boolean isDestroyed() {
        return mTraversalContext.isDestroyed();
    }

    @Override
    public void destroy() {
        mTraversalPath = null;
        mTraversalContext.destroy();
    }

    @Override
    public State getIncomingState() {
        return mTraversalContext.getIncomingState();
    }

    @Override
    public State getOutgoingState() {
        return mTraversalContext.getOutgoingState();
    }

    @Override
    public Direction getDirection() {
        return mTraversalContext.getDirection();
    }

    @Override
    public Map<Object, Context> getIncomingContexts() {
        return mTraversalContext.getIncomingContexts();
    }

    @Override
    public TraversalCallback getTraversalCallback() {
        return mTraversalContext.getTraversalCallback();
    }

    @Override
    public ViewGroup getContainerView() {
        return mTraversalContext.getContainerView();
    }

    @Override
    public ScreenScoper getScreenScoper() {
        return mTraversalContext.getScreenScoper();
    }

    @Override
    public void acquire(String tag) {
        mTraversalContext.acquire(tag);
    }

    @Override
    public void release(String tag) {
        mTraversalContext.release(tag);
    }

    @Override
    public TraversalPath getTraversalPath() {
        return mTraversalPath;
    }

    @Override
    public boolean hasTraversalValue(Object key) {
        return mTraversalContext.hasTraversalValue(key);
    }

    @Override
    public <T> T getTraversalValue(Object key) {
        return mTraversalContext.getTraversalValue(key);
    }

    @Override
    public <T> T getTraversalValue(Object key, T defaultValue) {
        return mTraversalContext.getTraversalValue(key, defaultValue);
    }

    @Override
    public void setTraversalValue(Object key, Object value) {
        mTraversalContext.setTraversalValue(key, value);
    }

    @Override
    public void registerInflatedLayoutMapping(InflatedLayoutMapping spec) {
        mTraversalContext.registerInflatedLayoutMapping(spec);
    }

    @Override
    @Nullable
    public InflatedLayoutMapping findInflatedLayoutMapping(LayoutSpec.LayoutMapping mapping) {
        return mTraversalContext.findInflatedLayoutMapping(mapping);
    }

    @Override
    public Collection<InflatedLayoutMapping> getInflatedLayoutMappings() {
        return mTraversalContext.getInflatedLayoutMappings();
    }

    void setTraversalPath(TraversalPath path) {
        mTraversalPath = path;
    }

    @Nullable
    @Override
    public RetainedViewPool getRetainedViewPool() {
        return mTraversalContext.getRetainedViewPool();
    }

}
