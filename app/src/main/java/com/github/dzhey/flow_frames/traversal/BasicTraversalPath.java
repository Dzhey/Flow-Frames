package com.github.dzhey.flow_frames.traversal;

import android.support.annotation.Nullable;

import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */

public class BasicTraversalPath implements TraversalPath {

    private final LinkedList<PathNode> mNodes;

    public BasicTraversalPath() {
        mNodes = new LinkedList<>();
    }

    protected BasicTraversalPath(Collection<PathNode> nodes) {
        mNodes = new LinkedList<>(nodes);
    }

    @Override
    public PathNode getFirstPathNode() {
        if (mNodes.isEmpty()) {
            throw new IllegalStateException("no path nodes available");
        }

        return mNodes.getFirst();
    }

    public void addPathNode(PathNode pathNode) {
        mNodes.add(pathNode);
    }

    @Override
    public @Nullable PathNode getNextNode(PathNode pathNode) {
        if (pathNode == null) {
            throw new IllegalArgumentException("pathNode == null");
        }

        int index = mNodes.lastIndexOf(pathNode);

        if (index < 0) {
            throw new IllegalArgumentException("no such node in path");
        }

        index++;

        if (mNodes.size() > index) {
            return mNodes.get(index);
        }

        return null;
    }

    @Override
    public Iterator<PathNode> iterator() {
        return mNodes.iterator();
    }
}
