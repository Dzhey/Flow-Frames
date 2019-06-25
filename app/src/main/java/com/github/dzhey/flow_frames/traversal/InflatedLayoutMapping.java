package com.github.dzhey.flow_frames.traversal;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.View;

import com.github.dzhey.flow_frames.LayoutSpec;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class InflatedLayoutMapping implements LayoutSpec.LayoutMapping {

    private final View mView;
    private final int mContainerId;
    private final int mLayoutId;
    private final String mLayoutTag;

    public InflatedLayoutMapping(@Nullable View view,
                                 @IdRes int containerId,
                                 @LayoutRes int layoutId) {

        this(view, containerId, layoutId, null);
    }


    public InflatedLayoutMapping(@Nullable View view,
                                 @IdRes int containerId,
                                 @LayoutRes int layoutId,
                                 @Nullable String layoutTag) {
        mView = view;
        mContainerId = containerId;
        mLayoutId = layoutId;
        mLayoutTag = layoutTag;
    }

    public @Nullable View getView() {
        return mView;
    }

    @Override
    public int containerId() {
        return mContainerId;
    }

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

        InflatedLayoutMapping that = (InflatedLayoutMapping) o;

        return mContainerId == that.mContainerId
                && mLayoutId == that.mLayoutId
                && (mView != null ? mView.equals(that.mView) : that.mView == null);

    }

    @Override
    public int hashCode() {
        int result = mView != null ? mView.hashCode() : 0;
        result = 31 * result + mContainerId;
        result = 31 * result + mLayoutId;
        return result;
    }
}
