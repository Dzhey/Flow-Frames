package com.github.dzhey.flow_frames;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link HistoryFrameId} which identifies {@link HistoryFrame} by it's screen's class names
 *
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 26-Sep-16.
 */
public class ClassNameHistoryFrameId implements HistoryFrameId {

    private final Set<String> mClassNames;

    public static ClassNameHistoryFrameId fromClassName(String className) {
        return new Builder().withScreenClassName(className).build();
    }

    public static ClassNameHistoryFrameId fromClassNames(String... classNames) {
        final Builder builder = new Builder();

        for (String className : classNames) {
            builder.withScreenClassName(className);
        }

        return builder.build();
    }

    protected ClassNameHistoryFrameId(Set<String> classNames) {
        if (classNames == null) {
            throw new IllegalArgumentException("class names must not be null");
        }

        mClassNames = classNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassNameHistoryFrameId)) return false;

        ClassNameHistoryFrameId that = (ClassNameHistoryFrameId) o;

        return mClassNames.equals(that.mClassNames);

    }

    @Override
    public int hashCode() {
        return mClassNames.hashCode();
    }

    public static class Builder {
        private final Set<String> mClassNames;

        public Builder() {
            mClassNames = new HashSet<>();
        }

        public Builder withHistoryFrame(HistoryFrame historyFrame) {
            for (Screen screen : historyFrame.getScreens()) {
                withScreen(screen);
            }

            return this;
        }

        public Builder withScreen(Screen screen) {
            mClassNames.add(screen.getClass().getName());

            return this;
        }

        public Builder withScreenClassName(String className) {
            if (className == null || className.length() == 0) {
                throw new IllegalArgumentException("class name may not be empty");
            }

            mClassNames.add(className);

            return this;
        }

        public ClassNameHistoryFrameId build() {
            return new ClassNameHistoryFrameId(new HashSet<>(mClassNames));
        }
    }
}
