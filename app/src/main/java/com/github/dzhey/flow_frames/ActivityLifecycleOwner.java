package com.github.dzhey.flow_frames;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Pass along the Android lifecycle events.
 * Tip to http://stackoverflow.com/questions/21927990/mortar-flow-with-third-party-libraries-hooked-to-activity-lifecycle/21959529?noredirect=1#21959529
 * on this one, mostly.
 */
public class ActivityLifecycleOwner implements ActivityLifecycleListener {

    private final List<ActivityLifecycleListener> mListeners;
    private Bundle mSavedInstanceState;
    private boolean mIsActivityCreated;
    private boolean mIsActivityStarted;
    private boolean mIsActivityResumed;

    public ActivityLifecycleOwner() {
        mListeners = new ArrayList<>();
    }

    public void register(ActivityLifecycleListener listener) {
        mListeners.add(listener);
    }

    public void unregister(ActivityLifecycleListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        mIsActivityCreated = true;
        mIsActivityStarted = false;
        mIsActivityResumed = false;

        mSavedInstanceState = savedInstanceState;

        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityCreate(savedInstanceState);
        }
    }

    @Override
    public void onActivityResume() {
        mIsActivityResumed = true;

        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityResume();
        }
    }

    @Override
    public void onActivityPause() {
        mIsActivityResumed = false;

        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityPause();
        }
    }

    @Override
    public void onActivityStart() {
        mIsActivityStarted = true;

        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityStart();
        }
    }

    @Override
    public void onActivityStop() {
        mIsActivityStarted = false;

        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState) {
        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivitySaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onLowMemory();
        }
    }

    @Override
    public void onActivityDestroy(boolean isFinishing) {
        mIsActivityCreated = false;
        mIsActivityStarted = false;
        mIsActivityResumed = false;

        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityDestroy(isFinishing);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ActivityLifecycleListener[] lifecycleListeners = getArrayCopyOfRegisteredListeners();
        for (ActivityLifecycleListener c : lifecycleListeners) {
            c.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Creates a copy of the {@link #mListeners} list in order to safely iterate the
     * listeners. This was added to avoid {@link java.util.ConcurrentModificationException} that may
     * be triggered when listeners are added / removed during one of the lifecycle events.
     */
    private ActivityLifecycleListener[] getArrayCopyOfRegisteredListeners() {
        ActivityLifecycleListener[] registeredListenersArray =
                new ActivityLifecycleListener[mListeners.size()];
        mListeners.toArray(registeredListenersArray);
        return registeredListenersArray;
    }

    public boolean isActivityStarted() {
        return mIsActivityStarted;
    }

    public boolean isActivityResumed() {
        return mIsActivityResumed;
    }

    public boolean isActivityCreated() {
        return mIsActivityCreated;
    }

    public @Nullable Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }
}
