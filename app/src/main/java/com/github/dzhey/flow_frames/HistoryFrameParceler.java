package com.github.dzhey.flow_frames;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import flow.KeyParceler;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public final class HistoryFrameParceler implements KeyParceler {

    private final HistoryFrameFactory mHistoryFrameFactory;

    public HistoryFrameParceler(HistoryFrameFactory historyFrameFactory) {
        mHistoryFrameFactory = historyFrameFactory;
    }

    @NonNull
    @Override
    public Parcelable toParcelable(@NonNull Object key) {
        final List<Screen> screens = ((HistoryFrame) key).getScreens();
        final List<ScreenKeyParcel> screenParcels = new ArrayList<>(screens.size());

        for (Screen screen : screens) {
            screenParcels.add(newScreenKeyParcel(screen));
        }

        final HistoryFrameParcel parcel = new HistoryFrameParcel();
        parcel.mScreenKeyParcels = screenParcels;

        if (key instanceof RestorableHistoryFrame) {
            parcel.mHistoryFrameData = ((RestorableHistoryFrame) key).onSave();
        }

        return Parcels.wrap(parcel);
    }

    @NonNull
    @Override
    public Object toKey(@NonNull Parcelable parcelable) {
        final HistoryFrameParcel parcel = Parcels.unwrap(parcelable);
        final List<ScreenKeyParcel> screenParcels = parcel.mScreenKeyParcels;

        final List<Screen> screens = new ArrayList<>(screenParcels.size());

        for (ScreenKeyParcel screenParcel : screenParcels) {
            screens.add(newScreen(screenParcel));
        }

        final HistoryFrame frame = mHistoryFrameFactory.newHistoryFrame(screens);

        if (frame instanceof RestorableHistoryFrame) {
            ((RestorableHistoryFrame) frame).onRestore(parcel.mHistoryFrameData);
        }

        return frame;
    }

    protected ScreenFactory newScreenFactory(Screen screen) {
        return new ReflectiveScreenFactory(screen.getClass().getName());
    }

    private ScreenFactory newScreenFactoryChecked(Screen screen) {
        final ScreenFactory factory = newScreenFactory(screen);

        if (BuildConfig.DEBUG) {
            if (factory.getClass().getAnnotation(Parcel.class) == null) {
                throw new IllegalArgumentException(
                        "screen factory should be annotated with @Parcel");
            }
        }

        return factory;
    }

    private ScreenKeyParcel newScreenKeyParcel(Screen screen) {
        final ScreenKeyParcel keyParcel = new ScreenKeyParcel();

        if (screen instanceof HasState) {
            keyParcel.mScreenState = ((HasState) screen).getState();
        }

        keyParcel.mScreenFactory = newScreenFactoryChecked(screen);

        return keyParcel;
    }

    private Screen newScreen(ScreenKeyParcel keyParcel) {
        final Screen screen = keyParcel.mScreenFactory.createScreen();

        if (screen instanceof HasState) {
            ((HasState) screen).setState(keyParcel.mScreenState);
        }

        return screen;
    }

    @Parcel
    static class ScreenKeyParcel {
        @ParcelPropertyConverter(ScreenFactoryParcelConverter.class)
        ScreenFactory mScreenFactory;
        Parcelable mScreenState;
    }

    @Parcel
    static class HistoryFrameParcel {
        List<ScreenKeyParcel> mScreenKeyParcels;
        Parcelable mHistoryFrameData;
    }
}
