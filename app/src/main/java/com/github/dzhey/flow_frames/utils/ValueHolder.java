package com.github.dzhey.flow_frames.utils;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class ValueHolder<T> {

    private T mValue;
    private boolean mIsSet;

    public static <T> ValueHolder<T> of(T value) {
        return new ValueHolder<>(value);
    }

    public static <T> ValueHolder<T> of() {
        return new ValueHolder<>();
    }

    public static <T> ValueHolder<T> of(ValueHolder<T> other) {
        return new ValueHolder<>(other);
    }

    public static <T> ValueHolder<T> empty() {
        final ValueHolder<T> holder = of();

        holder.mIsSet = true;

        return holder;
    }

    public ValueHolder() {
    }

    public ValueHolder(ValueHolder<T> other) {
        mValue = other.mValue;
        mIsSet = other.mIsSet;
    }

    public ValueHolder(T value) {
        mValue = value;
        mIsSet = true;
    }

    public T getValue() {
        return mValue;
    }

    public T getValue(T defaultValue) {
        return mIsSet ? mValue : defaultValue;
    }

    public void setValue(T value) {
        mValue = value;
        mIsSet = true;
    }

    public void clear() {
        mValue = null;
        mIsSet = false;
    }

    public boolean isSet() {
        return mIsSet;
    }
}
