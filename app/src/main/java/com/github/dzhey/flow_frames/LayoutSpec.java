package com.github.dzhey.flow_frames;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import java.util.Collection;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface LayoutSpec {
    interface LayoutMapping {
        @IdRes int containerId();
        @LayoutRes int layoutId();
        @Nullable String layoutTag();
    }

    Collection<LayoutMapping> getLayoutMappings();
}
