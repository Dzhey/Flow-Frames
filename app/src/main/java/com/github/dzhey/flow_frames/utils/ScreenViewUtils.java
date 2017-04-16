package com.github.dzhey.flow_frames.utils;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.github.dzhey.flow_frames.LayoutSpec;
import com.github.dzhey.flow_frames.R;
import com.github.dzhey.flow_frames.Screen;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class ScreenViewUtils {

    public static View findViewByTag(ViewGroup root,
                                     @IdRes int tagId,
                                     Object tagValue) {

        final List<View> results = findViewsByTag(root, tagId, tagValue);

        return results.isEmpty()
                ? null
                : results.get(0);
    }

    public static List<View> findViewsByTag(ViewGroup root,
                                            @IdRes int tagId,
                                            Object tagValue) {

        return findViewsByTagRecursive(root, tagId, tagValue, true);
    }

    public static List<View> findViewsByTagRecursive(ViewGroup root,
                                                     @IdRes int tagId,
                                                     Object tagValue,
                                                     boolean recursive) {

        final ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);

            if (recursive && child instanceof ViewGroup) {
                views.addAll(findViewsByTagRecursive((ViewGroup) child, tagId, tagValue, true));
            }

            final Object tagObj = child.getTag(tagId);
            if (tagObj != null && tagObj.equals(tagValue)) {
                views.add(child);
            }
        }
        return views;
    }


    public static String makeViewLayoutTag(LayoutSpec.LayoutMapping layoutMapping) {
        if (layoutMapping == null) {
            throw new IllegalArgumentException("mapping == null");
        }

        return String.format("layout:%s:%s",
                String.valueOf(layoutMapping.layoutId()),
                layoutMapping.layoutTag() == null ? "*" : layoutMapping.layoutTag());
    }

    public static String makeViewStateKey(View view) {
        final Object tag = view.getTag(R.id.__screens_key_changer_view_layout_id);

        if (tag == null) {
            throw new IllegalArgumentException(String.format(
                    "view %s was not tagged with layout id", view));
        }

        return (String) tag;
    }
}
