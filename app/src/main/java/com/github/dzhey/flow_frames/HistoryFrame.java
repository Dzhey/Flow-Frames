package com.github.dzhey.flow_frames;

import java.util.List;

import flow.MultiKey;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface HistoryFrame extends MultiKey, HandlesBack {
    List<Screen> getScreens();
}
