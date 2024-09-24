package com.github.dzhey.flow_frames.traversal;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

import com.github.dzhey.flow_frames.IScreenScoper;
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
public interface TraversalContext {

    boolean isDestroyed();

    void destroy();

    State getIncomingState();

    State getOutgoingState();

    Direction getDirection();

    Map<Object, Context> getIncomingContexts();

    TraversalCallback getTraversalCallback();

    ViewGroup getContainerView();

    IScreenScoper getScreenScoper();

    void acquire(String tag);

    void release(String tag);

    TraversalPath getTraversalPath();

    boolean hasTraversalValue(Object key);

    <T> T getTraversalValue(Object key);

    <T> T getTraversalValue(Object key, T defaultValue);

    void setTraversalValue(Object key, Object value);

    void registerInflatedLayoutMapping(InflatedLayoutMapping spec);

    @Nullable InflatedLayoutMapping findInflatedLayoutMapping(LayoutSpec.LayoutMapping mapping);

    Collection<InflatedLayoutMapping> getInflatedLayoutMappings();

    @Nullable
    RetainedViewPool getRetainedViewPool();
}