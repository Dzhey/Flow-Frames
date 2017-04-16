package com.github.dzhey.flow_frames;

import android.view.ViewGroup;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface HasViewGroup extends HasView {
    ViewGroup getView();
}
