package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.content.Context;

import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.Logger;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

import java.util.HashSet;
import java.util.Set;

import mortar.MortarScope;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class ExitOutgoingScopeNode extends BasePathNode {

    public ExitOutgoingScopeNode() {
        super(PathNode.Names.EXIT_OUTGOING_SCOPE);
    }

    @Override
    public void apply(TraversalContext traversalContext,
                      OnAppliedCallback onAppliedCallback) {

        if (traversalContext.getOutgoingState() == null) {
            onAppliedCallback.onApplied();
            return;
        }

        final HistoryFrame outgoingFrame = traversalContext.getOutgoingState().getKey();
        final HistoryFrame incomingFrame = traversalContext.getIncomingState().getKey();
        final Set<String> outgoingScopes = new HashSet<>(outgoingFrame.getScreens().size());

        for (Screen screen : outgoingFrame.getScreens()) {
            outgoingScopes.add(screen.getScopeName());
        }

        for (Screen screen : incomingFrame.getScreens()) {
            outgoingScopes.remove(screen.getScopeName());
            Logger.trace(this, "retained scope: %s", screen.getScopeName());
        }

        final Context context = traversalContext.getContainerView().getContext();

        for (String leftScopeName : outgoingScopes) {
            final MortarScope scope = MortarScope.findChild(context, leftScopeName);

            if (scope != null && !scope.isDestroyed()) {
                scope.destroy();
                Logger.trace(this, "destroyed outgoing scope: [%s]", leftScopeName);
            }
        }

        onAppliedCallback.onApplied();
    }
}
