package com.github.dzhey.flow_frames.traversal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dzhey.flow_frames.HasViewGroup;
import com.github.dzhey.flow_frames.Logger;
import com.github.dzhey.flow_frames.RetainedViewPool;
import com.github.dzhey.flow_frames.ScreenScoper;
import com.github.dzhey.flow_frames.utils.ValueHolder;

import java.util.Map;

import flow.Direction;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

/**
 * @author Eugene Byzov <gdzhey@gmail.com>
 *         Created on 05-Sep-16.
 */
public class ContextKeyChanger implements KeyChanger {

    private static final String CONTEXT_ACQUIRE_TAG = "ContextKeyChanger";

    private final HasViewGroup mHasViewGroup;
    private final Delegate mDelegateChanger;
    private RetainedViewPool mRetainedViewPool;

    public ContextKeyChanger(HasViewGroup viewGroupProvider, Delegate delegateChanger) {
        mHasViewGroup = viewGroupProvider;
        mDelegateChanger = delegateChanger;
    }

    @Override
    public void changeKey(@Nullable State outgoingState,
                          @NonNull State incomingState,
                          @NonNull Direction direction,
                          @NonNull Map<Object, Context> incomingContexts,
                          @NonNull final TraversalCallback callback) {

        final ValueHolder<TraversalContext> mContextHolder = ValueHolder.of();
        final TraversalCallback proxyCallback = () -> {
            if (mContextHolder.getValue().isDestroyed()) {
                throw new IllegalStateException(
                        "Unable to perform traversal completion callback; "
                                + "Traversal context is already destroyed. "
                                + "Ensure you call onTraversalCompleted() only once");
            }

            callback.onTraversalCompleted();
            Logger.debug(ContextKeyChanger.this,
                    "destroy traversal context on traversal completion");
            mContextHolder.getValue().release(CONTEXT_ACQUIRE_TAG);
        };

        final BasicTraversalContext context = new BasicTraversalContext(mHasViewGroup.getView(),
                outgoingState,
                incomingState,
                direction,
                incomingContexts,
                proxyCallback,
                new ScreenScoper());
        context.setRetainedViewPool(mRetainedViewPool);

        mContextHolder.setValue(context);
        context.acquire(CONTEXT_ACQUIRE_TAG);

        mDelegateChanger.changeKey(context);
    }

    public RetainedViewPool getRetainedViewPool() {
        return mRetainedViewPool;
    }

    public void setRetainedViewPool(RetainedViewPool retainedViewPool) {
        mRetainedViewPool = retainedViewPool;
    }

    public interface Delegate {
        void changeKey(TraversalContext context);
    }
}
