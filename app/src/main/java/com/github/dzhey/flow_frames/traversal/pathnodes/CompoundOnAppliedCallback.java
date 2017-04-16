package com.github.dzhey.flow_frames.traversal.pathnodes;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 24-Sep-16.
 */

public class CompoundOnAppliedCallback implements PathNode.OnAppliedCallback {

    private final int mPendingCallsCount;
    private final PathNode.OnAppliedCallback mOnAppliedCallback;
    private int mCallsCount;
    private boolean mIsFinished;

    public CompoundOnAppliedCallback(int pendingCallsCount,
                                     PathNode.OnAppliedCallback onAppliedCallback) {

        mPendingCallsCount = pendingCallsCount;
        mOnAppliedCallback = onAppliedCallback;
    }

    @Override
    public void onApplied() {
        if (mIsFinished) {
            return;
        }

        mCallsCount++;

        if (mPendingCallsCount == mCallsCount) {
            mIsFinished = true;
            mOnAppliedCallback.onApplied();
        }
    }
}
