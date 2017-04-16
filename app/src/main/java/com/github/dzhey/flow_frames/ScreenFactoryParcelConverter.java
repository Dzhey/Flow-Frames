package com.github.dzhey.flow_frames;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class ScreenFactoryParcelConverter implements ParcelConverter<ScreenFactory> {

    @Override
    public void toParcel(ScreenFactory input, Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public ScreenFactory fromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(ScreenFactory.class.getClassLoader()));
    }
}
