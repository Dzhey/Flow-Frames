package com.github.dzhey.flow_frames;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class SimpleLayoutSpec implements LayoutSpec {

    public static SimpleLayoutSpec empty() {
        return fromLayoutMapping(0, 0);
    }

    public static SimpleLayoutSpec fromLayoutRes(@LayoutRes int layoutId) {
        return fromLayoutMapping(0, layoutId);
    }

    public static SimpleLayoutSpec fromLayoutMapping(@IdRes int containerId,
                                                     @LayoutRes int layoutId) {

        return fromLayoutMapping(containerId, layoutId, null);
    }

    public static SimpleLayoutSpec fromLayoutMapping(@IdRes int containerId,
                                                     @LayoutRes int layoutId,
                                                     @Nullable String layoutTag) {

        return new SimpleLayoutSpec(Collections.singletonList(
                new Mapping(containerId, layoutId, layoutTag)));
    }

    private final List<LayoutMapping> mMappings;

    public SimpleLayoutSpec() {
        mMappings = new ArrayList<>();
    }

    protected SimpleLayoutSpec(Collection<? extends LayoutMapping> mappings) {
        mMappings = new ArrayList<>(mappings);
    }

    @Override
    public Collection<LayoutMapping> getLayoutMappings() {
        return mMappings;
    }

    public static class Builder {

        private final List<LayoutMapping> mMappings;

        public Builder() {
            mMappings = new ArrayList<>();
        }

        public Builder setMapping(@IdRes int containerId, @LayoutRes int layoutId) {
            setMapping(containerId, layoutId, null);

            return this;
        }

        public Builder setMapping(@IdRes int containerId,
                                  @LayoutRes int layoutId,
                                  @Nullable String layoutTag) {

            final Mapping mapping = new Mapping(containerId, layoutId, layoutTag);

            if (!mMappings.contains(mapping)) {
                mMappings.add(mapping);
            }

            return this;
        }

        public Builder setLayoutId(@LayoutRes int layoutId) {
            setMapping(0, layoutId);

            return this;
        }

        public SimpleLayoutSpec build() {
            return new SimpleLayoutSpec(new ArrayList<>(mMappings));
        }
    }

    private static class Mapping implements LayoutMapping {

        private final int mContainerId;
        private final int mLayoutId;
        private final String mLayoutTag;

        Mapping(@IdRes int containerId, @LayoutRes int layoutId) {
            this(containerId, layoutId, null);
        }

        Mapping(@IdRes int containerId, @LayoutRes int layoutId, @Nullable String layoutTag) {
            mContainerId = containerId;
            mLayoutId = layoutId;
            mLayoutTag = layoutTag;
        }

        @IdRes
        @Override
        public int containerId() {
            return mContainerId;
        }

        @LayoutRes
        @Override
        public int layoutId() {
            return mLayoutId;
        }

        @Nullable
        @Override
        public String layoutTag() {
            return mLayoutTag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Mapping mapping = (Mapping) o;

            return mContainerId == mapping.mContainerId
                    && mLayoutId == mapping.mLayoutId
                    && (mLayoutTag != null
                        ? mLayoutTag.equals(mapping.mLayoutTag)
                        : mapping.mLayoutTag == null);

        }

        @Override
        public int hashCode() {
            int result = mContainerId;
            result = 31 * result + mLayoutId;
            result = 31 * result + (mLayoutTag != null ? mLayoutTag.hashCode() : 0);
            return result;
        }
    }
}
