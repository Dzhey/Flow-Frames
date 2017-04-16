package com.github.dzhey.flow_frames;

import android.content.Context;

import mortar.MortarScope;
import mortar.Scoped;

/**
 * Created by Eugene Byzov on 02/09/16.
 * gdzhey@gmail.com
 *
 */

public final class MortarServices {

    public static final String DAGGER_COMPONENT = "dagger_component";

    @SuppressWarnings("unchecked")
    public static <T> T getService(Context context, String serviceName) {
        //noinspection ResourceType
        return (T) context.getSystemService(serviceName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(MortarScope scope, String serviceName) {
        //noinspection ResourceType
        return (T) scope.getService(serviceName);
    }

    /**
     * add {@code scoped} to specified {@code scope} and return back
     * @param scope scope to register your {@code scoped} object in
     * @param scoped object to register within {@code scope}
     * @param <T> {@code scoped} object type
     * @return {@code scoped}
     */
    public static <T extends Scoped> T withinScope(MortarScope scope, T scoped) {
        scope.register(scoped);

        return scoped;
    }

    private MortarServices() {
    }
}
