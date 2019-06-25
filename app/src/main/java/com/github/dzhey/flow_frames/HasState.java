package com.github.dzhey.flow_frames;

import android.os.Parcelable;
import androidx.annotation.Nullable;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public interface HasState {
    @Nullable
    Parcelable getState();

    void setState(@Nullable Parcelable state);
}
