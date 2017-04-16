package com.github.dzhey.flow_frames;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 01-Oct-16.
 */
public class SimpleActivityLifecycleListener implements ActivityLifecycleListener {

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onActivityResume() {
    }

    @Override
    public void onActivityPause() {
    }

    @Override
    public void onActivityStart() {
    }

    @Override
    public void onActivityStop() {
    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onActivityDestroy(boolean isFinishing) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }
}
