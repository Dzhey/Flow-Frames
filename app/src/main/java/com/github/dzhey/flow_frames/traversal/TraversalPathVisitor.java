package com.github.dzhey.flow_frames.traversal;

import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface TraversalPathVisitor {
    void visit(TraversalContext traversalContext,
               PathNode pathNode, PathNodeAppliedCallback callback);
}
