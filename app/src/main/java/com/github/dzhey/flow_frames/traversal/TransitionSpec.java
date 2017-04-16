package com.github.dzhey.flow_frames.traversal;

import android.support.annotation.IdRes;
import android.view.View;

import java.util.Collection;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface TransitionSpec {

    Collection<InflatedLayoutMapping> outgoingMappings();
    Collection<InflatedLayoutMapping> incomingMappings();
    @IdRes int containerId();
}
