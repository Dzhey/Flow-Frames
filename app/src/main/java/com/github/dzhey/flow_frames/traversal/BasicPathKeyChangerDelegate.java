package com.github.dzhey.flow_frames.traversal;

import com.github.dzhey.flow_frames.Logger;
import com.github.dzhey.flow_frames.traversal.pathnodes.AttachIncomingScreenNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.DetachOutgoingScreenNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.EnterIncomingScopeNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.ExitOutgoingScopeNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.PathNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.PrepareIncomingViewsNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.RegisterOutgoingViewsNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.RestoreIncomingViewsNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.RetainOutgoingViewsNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.SaveOutgoingViewStateNode;
import com.github.dzhey.flow_frames.traversal.pathnodes.SwapScreenViewsNode;

import flow.State;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class BasicPathKeyChangerDelegate extends BasePathKeyChangerDelegate
        implements TraversalPathVisitor {

    private static final TraversalFrameChanger DEFAULT_VIEW_CHANGER =
            new SimpleTraversalFrameChanger();

    private final TraversalFrameChangerResolver mFrameChangerResolver =
        (traversalContext, outgoingHistoryFrame, incomingHistoryFrame) -> DEFAULT_VIEW_CHANGER;

    @Override
    protected TraversalPath onBuildTraversalPath(TraversalContext traversalContext) {
        final TraversalPath path = new BasicTraversalPath();

        path.addPathNode(new RegisterOutgoingViewsNode());
        path.addPathNode(new SaveOutgoingViewStateNode());
        path.addPathNode(new EnterIncomingScopeNode());
        path.addPathNode(new PrepareIncomingViewsNode());
        path.addPathNode(new AttachIncomingScreenNode());
        path.addPathNode(new RestoreIncomingViewsNode());
        path.addPathNode(new SwapScreenViewsNode(mFrameChangerResolver));
        path.addPathNode(new DetachOutgoingScreenNode());
        path.addPathNode(new ExitOutgoingScopeNode());
        path.addPathNode(new RetainOutgoingViewsNode());

        Logger.trace(this, "built traversal path");

        final State outgoingState = traversalContext.getOutgoingState();
        final State incomingState = traversalContext.getIncomingState();

        Logger.trace(this, "pending traversal: %s -> %s",
                outgoingState == null ? "()" : outgoingState.getKey(),
                incomingState.getKey());

        return path;
    }

    @Override
    protected TraversalPathVisitor getPathVisitor() {
        return this;
    }

    @Override
    public void visit(final TraversalContext traversalContext,
                      final PathNode pathNode,
                      final PathNodeAppliedCallback callback) {

        Logger.trace(this, "performing traversal: [%s]", pathNode.getName());
        pathNode.apply(traversalContext, () -> {
            Logger.trace(BasicPathKeyChangerDelegate.this,
                    "finished traversal: [%s]", pathNode.getName());
            callback.onPathNodeApplied(traversalContext, pathNode);
        });
    }
}
