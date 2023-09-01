package com.github.dzhey.flow_frames;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface RetainedViewPool {
    @Nullable View obtainViewForMapping(
        @NonNull Screen screen, @NonNull LayoutSpec.LayoutMapping mapping);
    void retainViewForMapping(
        @NonNull Screen screen, @NonNull LayoutSpec.LayoutMapping mapping, @NonNull View view);
}
