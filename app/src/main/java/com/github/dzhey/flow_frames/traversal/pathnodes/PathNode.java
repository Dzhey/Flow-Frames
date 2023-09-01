package com.github.dzhey.flow_frames.traversal.pathnodes;

import com.github.dzhey.flow_frames.traversal.TraversalContext;

/**
 * Applies certain traversal step
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface PathNode {

    interface OnAppliedCallback {
        void onApplied();
    }

    interface Names {
        String SAVE_OUTGOING_VIEWS = "save_outgoing_views";
        String ENTER_INCOMING_SCOPE = "enter_incoming_scope";
        String PREPARE_INCOMING_VIEWS = "prepare_incoming_views";
        String ATTACH_INCOMING_SCREEN = "attach_incoming_screen";
        String RESTORE_INCOMING_VIEWS = "restore_incoming_views";
        String DETACH_OUTGOING_SCREEN = "detach_outgoing_screen";

        String SWAP_SCREEN_VIEWS = "swap_screen_views";
        String EXIT_OUTGOING_SCOPE = "exit_outgoing_scope";
        String REGISTER_OUTGOING_VIEWS = "register_ougoing_views";
        String RETAIN_OUTGOING_VIEWS = "retain_outgoing_views";
    }

    String getName();
    void apply(TraversalContext context, OnAppliedCallback onAppliedCallback);
}
