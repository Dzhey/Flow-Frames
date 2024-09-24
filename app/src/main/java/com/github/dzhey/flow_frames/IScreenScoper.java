package com.github.dzhey.flow_frames;

import android.content.Context;

import mortar.MortarScope;

public interface IScreenScoper {
    MortarScope getScreenScope(Context context, Screen screen);
}
