package com.github.dzhey.flow_frames;

import android.os.Parcelable;
import androidx.annotation.Nullable;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 13-Oct-16.
 */
public interface RestorableHistoryFrame extends HistoryFrame, HandlesHome {
    @Nullable Parcelable onSave();
    void onRestore(@Nullable Parcelable parcelable);
}
