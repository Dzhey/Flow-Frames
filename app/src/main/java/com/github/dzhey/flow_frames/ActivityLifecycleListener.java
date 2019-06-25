package com.github.dzhey.flow_frames;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

/**
 * Listen to lifecycle events.
 */
public interface ActivityLifecycleListener {

    void onActivityCreate(Bundle savedInstanceState);

    void onActivityResume();

    void onActivityPause();

    void onActivityStart();

    void onActivityStop();

    void onActivitySaveInstanceState(Bundle outState);

    void onLowMemory();

    void onActivityDestroy(boolean isFinishing);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode,
                                    @NonNull String[] permissions,
                                    @NonNull int[] grantResults);
}
