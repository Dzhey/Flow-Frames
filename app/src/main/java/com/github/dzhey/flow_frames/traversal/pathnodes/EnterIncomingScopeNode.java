package com.github.dzhey.flow_frames.traversal.pathnodes;

import android.view.ViewGroup;

import mortar.MortarScope;
import com.github.dzhey.flow_frames.HistoryFrame;
import com.github.dzhey.flow_frames.Screen;
import com.github.dzhey.flow_frames.traversal.TraversalContext;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class EnterIncomingScopeNode extends IncomingScreenPathNode {

    public EnterIncomingScopeNode() {
        super(PathNode.Names.ENTER_INCOMING_SCOPE);
    }

    @Override
    protected void onApplyToScreen(TraversalContext traversalContext,
                                   HistoryFrame historyFrame,
                                   Screen screen,
                                   OnAppliedCallback onAppliedCallback) {

        final ViewGroup containerView = traversalContext.getContainerView();
        final MortarScope scope = traversalContext.getScreenScoper()
                .getScreenScope(containerView.getContext(), screen);

        if (scope == null || scope.isDestroyed()) return;

        scope.register(screen);
        if (!screen.isInScope()) {
            screen.onEnterScope(scope);
        }

        onAppliedCallback.onApplied();
    }
}
