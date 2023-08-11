package com.github.dzhey.flow_frames;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dzhey.flow_frames.traversal.TraversalFrameChanger;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class BasicHistoryFrame implements RestorableHistoryFrame {

    public static HistoryFrameFactory FACTORY = new HistoryFrameFactory() {
        @Override
        public HistoryFrame newHistoryFrame(Collection<Screen> screens) {
            return BasicHistoryFrame.from(screens);
        }
    };

    private static final String KEY_FRAME_NAME = "basic__frame_name";
    private static final String KEY_ENTER_FRAME_CHANGER = "basic__enter_frame_changer";
    private static final String KEY_EXIT_FRAME_CHANGER = "basic__exit_frame_changer";
    private static final String KEY_REPLACE_FRAME_CHANGER = "basic__replace_frame_changer";
    private static final String KEY_FRAME_RESULT = "basic__frame_result";
    private static final String KEY_FRAME_TAGS = "basic__frame_tags";

    private final List<Screen> mScreens;
    private final List<Object> mKeys;
    private final int mBaseHash;
    private Set<String> mTags;
    private String mName;
    private TraversalFrameChanger mEnterFrameChanger;
    private TraversalFrameChanger mExitFrameChanger;
    private TraversalFrameChanger mReplaceFrameChanger;
    private ResultsParcel mResultsParcel;

    public static BasicHistoryFrame from(Screen... screens) {
        return new BasicHistoryFrame(screens);
    }

    public static BasicHistoryFrame from(Collection<Screen> screens) {
        final Screen[] array = new Screen[screens.size()];

        int i = 0;
        for (Screen screen : screens) {
            array[i] = screen;
            i++;
        }

        return from(array);
    }

    protected BasicHistoryFrame(Screen... screens) {
        mScreens = Arrays.asList((Screen[]) screens);
        mKeys = Arrays.asList((Object[]) screens);
        mBaseHash = mKeys.hashCode();
        mResultsParcel = new ResultsParcel();
    }

    @Override
    public List<Screen> getScreens() {
        return mScreens;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean hasTag(String tag) {
        return mTags != null && mTags.contains(tag);
    }

    public void addTag(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("tag == null");
        }

        if (mTags == null) {
            mTags = new HashSet<>();
        }

        mTags.add(tag);
    }

    public Collection<String> getTags() {
        if (mTags == null) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableCollection(mTags);
    }

    public boolean containsScreen(Class<? extends Screen> screenClass) {
        for (Screen myScreen : mScreens) {
            if (screenClass.equals(myScreen.getClass())) {
                return true;
            }
        }

        return false;
    }

    public <T extends Screen> T findScreen(Class<? extends Screen> screenClass) {
        for (Screen myScreen : mScreens) {
            if (screenClass.equals(myScreen.getClass())) {
                //noinspection unchecked
                return (T) myScreen;
            }
        }

        throw new IllegalArgumentException("frame has no requested screen");
    }

    public Screen getTopScreen() {
        return mScreens.isEmpty() ? null : mScreens.get(mScreens.size() - 1);
    }

    public @LayoutRes int findTopLayoutId(@IdRes int containerId) {
        int layoutId = 0;

        for (Screen myScreen : mScreens) {
            for (LayoutSpec.LayoutMapping mapping : myScreen.getLayoutSpec().getLayoutMappings()) {
                if (mapping.containerId() == containerId) {
                    layoutId = mapping.layoutId();
                }
            }
        }

        return layoutId;
    }

    @Override
    public boolean onGoHome() {
        for (Screen screen : mScreens) {
            if (screen instanceof HandlesHome) {
                if (((HandlesHome) screen).onGoHome()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onGoBack() {
        for (int i = mScreens.size() - 1; i >= 0; i--) {
            if (mScreens.get(i) instanceof HandlesBack) {
                if (((HandlesBack) mScreens.get(i)).onGoBack()) {
                    return true;
                }
            }
        }

        return false;
    }

    public TraversalFrameChanger getEnterFrameChanger() {
        return mEnterFrameChanger;
    }

    public void setEnterFrameChanger(TraversalFrameChanger enterFrameChanger) {
        mEnterFrameChanger = enterFrameChanger;
    }

    public TraversalFrameChanger getExitFrameChanger() {
        return mExitFrameChanger;
    }

    public void setExitFrameChanger(TraversalFrameChanger exitFrameChanger) {
        mExitFrameChanger = exitFrameChanger;
    }

    @NonNull
    @Override
    public List<Object> getKeys() {
        return mKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicHistoryFrame that = (BasicHistoryFrame) o;

        return hashCode() == that.hashCode();

    }

    @Override
    public int hashCode() {
        return mBaseHash
                + (mName == null ? 0 : mName.hashCode())
                + (mTags == null ? 0 : mTags.hashCode());
    }

    @Nullable
    @Override
    public Parcelable onSave() {
        final Bundle bundle = new Bundle();

        bundle.putParcelable(KEY_ENTER_FRAME_CHANGER, Parcels.wrap(mEnterFrameChanger));
        bundle.putParcelable(KEY_EXIT_FRAME_CHANGER, Parcels.wrap(mExitFrameChanger));
        bundle.putParcelable(KEY_REPLACE_FRAME_CHANGER, Parcels.wrap(mReplaceFrameChanger));
        bundle.putString(KEY_FRAME_NAME, mName);
        bundle.putParcelable(KEY_FRAME_RESULT, mResultsParcel);

        if (mTags != null) {
            final String[] tags = new String[mTags.size()];
            mTags.toArray(tags);
            bundle.putStringArray(KEY_FRAME_TAGS, tags);
        }

        return bundle;
    }

    @Override
    public void onRestore(@Nullable Parcelable parcelable) {
        if (parcelable == null) {
            return;
        }

        final Bundle bundle = (Bundle) parcelable;
        bundle.setClassLoader(BasicHistoryFrame.class.getClassLoader());

        mName = bundle.getString(KEY_FRAME_NAME);
        mEnterFrameChanger = Parcels.unwrap(bundle.getParcelable(KEY_ENTER_FRAME_CHANGER));
        mExitFrameChanger = Parcels.unwrap(bundle.getParcelable(KEY_EXIT_FRAME_CHANGER));
        mReplaceFrameChanger = Parcels.unwrap(bundle.getParcelable(KEY_REPLACE_FRAME_CHANGER));
        mResultsParcel = bundle.getParcelable(KEY_FRAME_RESULT);

        final String[] tags = bundle.getStringArray(KEY_FRAME_TAGS);
        if (tags != null) {
            mTags = new HashSet<>(tags.length);
            Collections.addAll(mTags, tags);
        }
    }

    public boolean hasScreenResult(String key) {
        return mResultsParcel.mResults.containsKey(key);
    }

    public Parcelable getScreenResult(String key) {
        return mResultsParcel.mResults.get(key);
    }

    public <T extends Parcelable> T getScreenResult(String key, T defaultValue) {
        if (!hasScreenResult(key)) {
            return defaultValue;
        }

        //noinspection unchecked
        return (T) mResultsParcel.mResults.get(key);
    }

    public void putScreenResult(String key, Parcelable result) {
        mResultsParcel.mResults.put(key, result);
    }

    public void setReplaceFrameChanger(TraversalFrameChanger replaceFrameChanger) {
        mReplaceFrameChanger = replaceFrameChanger;
    }

    public TraversalFrameChanger getReplaceFrameChanger() {
        return mReplaceFrameChanger;
    }

    public static class Builder {

        private final List<Screen> mScreens;
        private TraversalFrameChanger mEnterFrameChanger;
        private TraversalFrameChanger mExitFrameChanger;
        private TraversalFrameChanger mReplaceFrameChanger;
        private String mFrameName;
        private Set<String> mTags;

        public Builder() {
            mScreens = new ArrayList<>();
        }

        public Builder(BasicHistoryFrame frame) {
            this();
            withScreens(frame.getScreens());
            withTags(frame.getTags());
            withFrameName(frame.getName());
            withReplaceFrameChanger(frame.getReplaceFrameChanger());
            withEnterFrameChanger(frame.getEnterFrameChanger());
            withExitFrameChanger(frame.getExitFrameChanger());
        }

        public Builder withScreen(Screen screen) {
            mScreens.add(screen);

            return this;
        }

        public Builder withScreens(Screen... screens) {
            Collections.addAll(mScreens, screens);

            return this;
        }

        public Builder withScreens(Collection<Screen> screens) {
            mScreens.addAll(screens);

            return this;
        }

        public Builder withScreensOnly(Collection<Screen> screens) {
            mScreens.clear();
            return withScreens(screens);
        }

        public Builder withEnterFrameChanger(TraversalFrameChanger frameChanger) {
            mEnterFrameChanger = frameChanger;

            return this;
        }

        public Builder withExitFrameChanger(TraversalFrameChanger frameChanger) {
            mExitFrameChanger = frameChanger;

            return this;
        }

        public Builder withReplaceFrameChanger(TraversalFrameChanger frameChanger) {
            mReplaceFrameChanger = frameChanger;

            return this;
        }

        public Builder withFrameName(String frameName) {
            mFrameName = frameName;

            return this;
        }

        public Builder withTag(String tag) {
            if (tag == null) {
                throw new IllegalArgumentException("tag == null");
            }

            if (mTags == null) {
                mTags = new HashSet<>();
            }

            mTags.add(tag);

            return this;
        }

        public Builder withTags(String... tags) {
            for (String tag : tags) {
                withTag(tag);
            }

            return this;
        }

        public Builder withTags(Collection<String> tags) {
            for (String tag : tags) {
                withTag(tag);
            }

            return this;
        }

        public BasicHistoryFrame build() {
            final Screen[] screens = new Screen[mScreens.size()];

            mScreens.toArray(screens);

            final BasicHistoryFrame frame = new BasicHistoryFrame(screens);

            frame.setName(mFrameName);
            frame.setEnterFrameChanger(mEnterFrameChanger);
            frame.setExitFrameChanger(mExitFrameChanger);
            frame.setReplaceFrameChanger(mReplaceFrameChanger);

            if (mTags != null) {
                frame.mTags = new HashSet<>(mTags);
            }

            return frame;
        }
    }

    static class ResultsParcel implements Parcelable {

        private final Map<String, Parcelable> mResults;

        public static final Creator<ResultsParcel> CREATOR = new Creator<ResultsParcel>() {
            @Override
            public ResultsParcel createFromParcel(Parcel parcel) {
                return new ResultsParcel(parcel);
            }

            @Override
            public ResultsParcel[] newArray(int sz) {
                return new ResultsParcel[sz];
            }
        };

        ResultsParcel() {
            mResults = new HashMap<>(1);
        }

        ResultsParcel(Parcel src) {
            this();

            final ClassLoader loader = ResultsParcel.class.getClassLoader();
            final int sz = src.readInt();

            for (int i = 0; i < sz; i++) {
                mResults.put(src.readString(), src.readParcelable(loader));
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            final int sz = mResults.size();

            parcel.writeInt(sz);

            if (sz > 0) {
                for (Map.Entry<String, Parcelable> entry : mResults.entrySet()) {
                    parcel.writeString(entry.getKey());
                    parcel.writeParcelable(entry.getValue(), 0);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "BasicHistoryFrame{" +
                "mScreens=" + mScreens +
                ", mTags=" + mTags +
                ", mName='" + mName + '\'' +
                '}';
    }
}
