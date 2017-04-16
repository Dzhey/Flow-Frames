package com.github.dzhey.flow_frames.traversal;

import android.support.annotation.Nullable;

import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface TraversalPath extends Iterable<PathNode> {
    PathNode getFirstPathNode();
    void addPathNode(PathNode pathNode);
    @Nullable PathNode getNextNode(PathNode pathNode);
}
