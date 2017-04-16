package com.github.dzhey.flow_frames.traversal;

import com.github.dzhey.flow_frames.Logger;
import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public abstract class BasePathKeyChangerDelegate implements ContextKeyChanger.Delegate,
        PathNodeAppliedCallback {

    private static final String TRAVERSAL_APPLIED_NODES_SET = "applied_nodes";
    private static final TraversalPathWatcher[] WATCHERS_EMPTY = new TraversalPathWatcher[0];
    private final List<TraversalPathWatcher> mTraversalPathWatchers;

    private final TraversalPathWatcher mWatcherEventDispatcher = new TraversalPathWatcher() {
        @Override
        public void onEnterTraversal(TraversalContext traversalContext) {
            for (TraversalPathWatcher watcher : getPathWatchersArray()) {
                watcher.onEnterTraversal(traversalContext);
            }
        }

        @Override
        public void onNextPathNode(TraversalContext traversalContext, PathNode nextNode) {
            for (TraversalPathWatcher watcher : getPathWatchersArray()) {
                watcher.onNextPathNode(traversalContext, nextNode);
            }
        }

        @Override
        public void onPathNodeApplied(TraversalContext traversalContext, PathNode pathNode) {
            for (TraversalPathWatcher watcher : getPathWatchersArray()) {
                watcher.onPathNodeApplied(traversalContext, pathNode);
            }
        }

        @Override
        public void onExitTraversal(TraversalContext traversalContext) {
            for (TraversalPathWatcher watcher : getPathWatchersArray()) {
                watcher.onExitTraversal(traversalContext);
            }
        }
    };

    protected BasePathKeyChangerDelegate() {
        mTraversalPathWatchers = new ArrayList<>();
    }

    public void addPathWatcher(TraversalPathWatcher watcher) {
        mTraversalPathWatchers.add(watcher);
    }

    public void removePathWatcher(TraversalPathWatcher watcher) {
        mTraversalPathWatchers.remove(watcher);
    }

    @Override
    public void changeKey(TraversalContext context) {
        final TraversalContextWrapper wrapper = TraversalContextWrapper.wrap(context);
        final TraversalPath traversalPath = buildTraversalPath(context);

        wrapper.setTraversalPath(traversalPath);

        final TraversalPathVisitor visitor = getPathVisitor();

        Logger.trace(this, "traversal started with first path node");
        mWatcherEventDispatcher.onEnterTraversal(wrapper);

        final PathNode pathNode = traversalPath.getFirstPathNode();

        mWatcherEventDispatcher.onNextPathNode(wrapper, pathNode);
        visitor.visit(wrapper, pathNode, this);
    }

    @Override
    public void onPathNodeApplied(TraversalContext traversalContext, PathNode pathNode) {
        final TraversalContextWrapper wrapper = (TraversalContextWrapper) traversalContext;
        final TraversalPath path = wrapper.getTraversalPath();

        HashSet<PathNode> appliedNodes = wrapper.getTraversalValue(TRAVERSAL_APPLIED_NODES_SET);
        if (appliedNodes == null) {
            appliedNodes = new HashSet<>();
            wrapper.setTraversalValue(TRAVERSAL_APPLIED_NODES_SET, appliedNodes);

        } else if (appliedNodes.contains(pathNode)) {
            throw new IllegalStateException(String.format("Path node %s already applied; " +
                    "make sure you invoke callback only once", pathNode));
        }

        appliedNodes.add(pathNode);

        mWatcherEventDispatcher.onPathNodeApplied(wrapper, pathNode);

        final PathNode nextNode = path.getNextNode(pathNode);

        if (nextNode != null) {
            mWatcherEventDispatcher.onNextPathNode(wrapper, pathNode);
            getPathVisitor().visit(wrapper, nextNode, this);
        } else {
            Logger.trace(this, "traversal completed, calling completion callback");
            mWatcherEventDispatcher.onExitTraversal(wrapper);
            wrapper.getTraversalCallback().onTraversalCompleted();
        }
    }

    protected abstract TraversalPath onBuildTraversalPath(TraversalContext traversalContext);

    protected abstract TraversalPathVisitor getPathVisitor();

    private TraversalPath buildTraversalPath(TraversalContext traversalContext) {
        final TraversalPath path = onBuildTraversalPath(traversalContext);

        if (path == null) {
            throw new IllegalStateException("built traversal path may not be null");
        }

        return path;
    }

    private TraversalPathWatcher[] getPathWatchersArray() {
        if (mTraversalPathWatchers.isEmpty()) {
            return WATCHERS_EMPTY;
        }

        final TraversalPathWatcher[] watchers =
                new TraversalPathWatcher[mTraversalPathWatchers.size()];
        mTraversalPathWatchers.toArray(watchers);

        return watchers;
    }

    public interface TraversalPathWatcher {
        void onEnterTraversal(TraversalContext traversalContext);
        void onNextPathNode(TraversalContext traversalContext, PathNode nextNode);
        void onPathNodeApplied(TraversalContext traversalContext, PathNode pathNode);
        void onExitTraversal(TraversalContext traversalContext);
    }
}
