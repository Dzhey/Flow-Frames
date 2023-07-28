package com.github.dzhey.flow_frames.traversal;

import android.view.View;

import androidx.annotation.NonNull;

public interface ViewSwapTraversalSubscriber {
    void onEnterAddTraversal(@NonNull View view, @NonNull TraversalContext traversalContext);
    void onExitAddTraversal(@NonNull View view, @NonNull TraversalContext traversalContext);
    void onEnterRemoveTraversal(@NonNull View view, @NonNull TraversalContext traversalContext);
    void onExitRemoveTraversal(@NonNull View view, @NonNull TraversalContext traversalContext);
    void onEnterTraversal(@NonNull View view, @NonNull TraversalContext traversalContext);
    void onExitTraversal(@NonNull View view, @NonNull TraversalContext traversalContext);
}
