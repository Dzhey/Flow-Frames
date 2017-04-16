package com.github.dzhey.flow_frames.traversal.pathnodes;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public abstract class BasePathNode implements PathNode {

    private final String mName;

    public BasePathNode(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name should not be empty");
        }

        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return "PathNode{" +
                "name='" + mName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasePathNode that = (BasePathNode) o;

        return mName.equals(that.mName);

    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }
}
