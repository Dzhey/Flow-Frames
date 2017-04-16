package com.github.dzhey.flow_frames.traversal;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 30-Sep-16.
 */
public interface ViewSwapTraversalListener {

    void onEnterAddTraversal(TraversalContext traversalContext);
    void onExitAddTraversal(TraversalContext traversalContext);
    void onEnterRemoveTraversal(TraversalContext traversalContext);
    void onExitRemoveTraversal(TraversalContext traversalContext);
    void onEnterTraversal(TraversalContext traversalContext);
    void onExitTraversal(TraversalContext traversalContext);
}
