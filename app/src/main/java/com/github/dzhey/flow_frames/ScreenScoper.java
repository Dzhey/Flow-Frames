package com.github.dzhey.flow_frames;

import android.content.Context;

import mortar.MortarScope;
import mortar.MortarScopeDevHelper;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class ScreenScoper {

    public MortarScope getScreenScope(Context context, Screen screen) {
        final MortarScope parentScope = MortarScope.getScope(context);

        final MortarScope childScope = parentScope.findChild(screen.getScopeName());
        if (childScope != null) {
            return childScope;
        }

        if (BuildConfig.DEBUG && !(screen instanceof ComponentFactory)) {
            throw new IllegalStateException(String.format(
                    "Screen %s should implement %s in order to use scopes",
                    screen.getClass().getName(),
                    ComponentFactory.class.getName()));
        }

        final Object parentComponent = parentScope.getService(MortarServices.DAGGER_COMPONENT);

        @SuppressWarnings("unchecked")
        final Object component = ((ComponentFactory) screen).createComponent(parentComponent);

        final MortarScope.Builder builder = parentScope.buildChild()
                .withService(MortarServices.DAGGER_COMPONENT, component);

        return builder.build(screen.getScopeName());
    }

    public static void destroyScreenScope(Context context, Screen screen) {
        final String scopeName = screen.getScopeName();

        final MortarScope scope = MortarScope.findChild(context, scopeName);

        if (scope != null) {
            scope.destroy();
            Logger.trace(ScreenScoper.class, "destroyed scope '%s'", scopeName);
        } else {
            Logger.warn(ScreenScoper.class,
                    "unable to destroy screen '%s' scope: child scope not found",
                    scopeName);

            if (BuildConfig.DEBUG) {
                Logger.warn(ScreenScoper.class, "Current scope hierarchy:");
                Logger.warn(ScreenScoper.class, MortarScopeDevHelper.scopeHierarchyToString(
                        MortarScope.getScope(context)));
            }
        }
    }
}