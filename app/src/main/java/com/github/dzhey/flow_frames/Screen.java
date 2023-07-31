package com.github.dzhey.flow_frames;

import mortar.Scoped;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface Screen extends Scoped, HasScopeName {

    LayoutSpec getLayoutSpec();

    void onAttach();

    void onDetach();

    boolean isAttached();

    boolean isInScope();
}
