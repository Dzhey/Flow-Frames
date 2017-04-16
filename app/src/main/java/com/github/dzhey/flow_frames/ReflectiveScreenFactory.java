package com.github.dzhey.flow_frames;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
@Parcel
public class ReflectiveScreenFactory implements ScreenFactory {

    @ParcelProperty("screenClassName")
    String mScreenClassName;

    @ParcelConstructor
    public ReflectiveScreenFactory(@ParcelProperty("screenClassName") String screenClassName) {
        mScreenClassName = screenClassName;
    }

    @Override
    public Screen createScreen() {
        try {
            final Class<?> clazz = Class.forName(mScreenClassName);
            final Constructor<?> constructor = clazz.getDeclaredConstructor();

            constructor.setAccessible(true);

            return (Screen) constructor.newInstance();

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format(
                    "screen %s should have default public constructor "
                            + "in order to be created from reflective screen factory",
                    mScreenClassName), e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
