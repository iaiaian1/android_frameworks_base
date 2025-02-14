/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.biometrics.sensors;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.Context;
import android.hardware.biometrics.BiometricsProtoEnums;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;

import com.android.internal.annotations.VisibleForTesting;

import java.util.NoSuchElementException;

/**
 * Abstract base class for keeping track and dispatching events from the biometric's HAL to the
 * the current client.  Subclasses are responsible for coordinating the interaction with
 * the biometric's HAL for the specific action (e.g. authenticate, enroll, enumerate, etc.).
 */
public abstract class BaseClientMonitor extends LoggableMonitor
        implements IBinder.DeathRecipient {

    private static final String TAG = "Biometrics/ClientMonitor";
    protected static final boolean DEBUG = true;

    // Counter used to distinguish between ClientMonitor instances to help debugging.
    private static int sCount = 0;

    /**
     * Interface that ClientMonitor holders should use to receive callbacks.
     */
    public interface Callback {

        /**
         * Invoked when the ClientMonitor operation has been started (e.g. reached the head of
         * the queue and becomes the current operation).
         *
         * @param clientMonitor Reference of the ClientMonitor that is starting.
         */
        default void onClientStarted(@NonNull BaseClientMonitor clientMonitor) {
        }

        /**
         * Invoked when the ClientMonitor operation is complete. This abstracts away asynchronous
         * (i.e. Authenticate, Enroll, Enumerate, Remove) and synchronous (i.e. generateChallenge,
         * revokeChallenge) so that a scheduler can process ClientMonitors regardless of their
         * implementation.
         *
         * @param clientMonitor Reference of the ClientMonitor that finished.
         * @param success True if the operation completed successfully.
         */
        default void onClientFinished(@NonNull BaseClientMonitor clientMonitor, boolean success) {
        }
    }

    /** Holder for wrapping multiple handlers into a single Callback. */
    protected static class CompositeCallback implements Callback {
        @NonNull
        private final Callback[] mCallbacks;

        public CompositeCallback(@NonNull Callback... callbacks) {
            mCallbacks = callbacks;
        }

        @Override
        public final void onClientStarted(@NonNull BaseClientMonitor clientMonitor) {
            for (int i = 0; i < mCallbacks.length; i++) {
                mCallbacks[i].onClientStarted(clientMonitor);
            }
        }

        @Override
        public final void onClientFinished(@NonNull BaseClientMonitor clientMonitor,
                boolean success) {
            for (int i = mCallbacks.length - 1; i >= 0; i--) {
                mCallbacks[i].onClientFinished(clientMonitor, success);
            }
        }
    }

    private final int mSequentialId;
    @NonNull private final Context mContext;
    private final int mTargetUserId;
    @NonNull private final String mOwner;
    private final int mSensorId; // sensorId as configured by the framework

    @Nullable private IBinder mToken;
    private long mRequestId;
    @Nullable private ClientMonitorCallbackConverter mListener;
    // Currently only used for authentication client. The cookie generated by BiometricService
    // is never 0.
    private final int mCookie;
    boolean mAlreadyDone;

    // Use an empty callback by default since delayed operations can receive events
    // before they are started and cause NPE in subclasses that access this field directly.
    @NonNull protected Callback mCallback = new Callback() {
        @Override
        public void onClientStarted(@NonNull BaseClientMonitor clientMonitor) {
            Slog.e(TAG, "mCallback onClientStarted: called before set (should not happen)");
        }

        @Override
        public void onClientFinished(@NonNull BaseClientMonitor clientMonitor,
                boolean success) {
            Slog.e(TAG, "mCallback onClientFinished: called before set (should not happen)");
        }
    };

    /**
     * @return A ClientMonitorEnum constant defined in biometrics.proto
     */
    public abstract int getProtoEnum();

    /**
     * @return True if the ClientMonitor should cancel any current and pending interruptable clients
     */
    public boolean interruptsPrecedingClients() {
        return false;
    }

    /**
     * @param context    system_server context
     * @param token      a unique token for the client
     * @param listener   recipient of related events (e.g. authentication)
     * @param userId     target user id for operation
     * @param owner      name of the client that owns this
     * @param cookie     BiometricPrompt authentication cookie (to be moved into a subclass soon)
     * @param sensorId   ID of the sensor that the operation should be requested of
     * @param statsModality One of {@link BiometricsProtoEnums} MODALITY_* constants
     * @param statsAction   One of {@link BiometricsProtoEnums} ACTION_* constants
     * @param statsClient   One of {@link BiometricsProtoEnums} CLIENT_* constants
     */
    public BaseClientMonitor(@NonNull Context context,
            @Nullable IBinder token, @Nullable ClientMonitorCallbackConverter listener, int userId,
            @NonNull String owner, int cookie, int sensorId, int statsModality, int statsAction,
            int statsClient) {
        super(context, statsModality, statsAction, statsClient);
        mSequentialId = sCount++;
        mContext = context;
        mToken = token;
        mRequestId = -1;
        mListener = listener;
        mTargetUserId = userId;
        mOwner = owner;
        mCookie = cookie;
        mSensorId = sensorId;

        try {
            if (token != null) {
                token.linkToDeath(this, 0);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "caught remote exception in linkToDeath: ", e);
        }
    }

    public int getCookie() {
        return mCookie;
    }

    /**
     * Starts the ClientMonitor's lifecycle.
     * @param callback invoked when the operation is complete (succeeds, fails, etc)
     */
    public void start(@NonNull Callback callback) {
        mCallback = wrapCallbackForStart(callback);
        mCallback.onClientStarted(this);
    }

    /**
     * Called during start to provide subclasses a hook for decorating the callback.
     *
     * Returns the original callback unless overridden.
     */
    @NonNull
    protected Callback wrapCallbackForStart(@NonNull Callback callback) {
        return callback;
    }

    public boolean isAlreadyDone() {
        return mAlreadyDone;
    }

    public void destroy() {
        if (mToken != null) {
            try {
                mToken.unlinkToDeath(this, 0);
            } catch (NoSuchElementException e) {
                // TODO: remove when duplicate call bug is found
                Slog.e(TAG, "destroy(): " + this + ":", new Exception("here"));
            }
            mToken = null;
        }
        mListener = null;
    }

    @Override
    public void binderDied() {
        binderDiedInternal(true /* clearListener */);
    }

    // TODO(b/157790417): Move this to the scheduler
    void binderDiedInternal(boolean clearListener) {
        Slog.e(TAG, "Binder died, owner: " + getOwnerString()
                + ", operation: " + this.getClass().getName());

        if (isAlreadyDone()) {
            Slog.w(TAG, "Binder died but client is finished, ignoring");
            return;
        }

        // If the current client dies we should cancel the current operation.
        if (this instanceof Interruptable) {
            Slog.e(TAG, "Binder died, cancelling client");
            ((Interruptable) this).cancel();
        }
        mToken = null;
        if (clearListener) {
            mListener = null;
        }
    }

    public final Context getContext() {
        return mContext;
    }

    public final String getOwnerString() {
        return mOwner;
    }

    public final ClientMonitorCallbackConverter getListener() {
        return mListener;
    }

    public int getTargetUserId() {
        return mTargetUserId;
    }

    public final IBinder getToken() {
        return mToken;
    }

    public final int getSensorId() {
        return mSensorId;
    }

    /** Unique request id. */
    public final long getRequestId() {
        return mRequestId;
    }

    /** If a unique id has been set via {@link #setRequestId(long)} */
    public final boolean hasRequestId() {
        return mRequestId > 0;
    }

    /**
     * A unique identifier used to tie this operation to a request (i.e an API invocation).
     *
     * Subclasses should not call this method if this operation does not have a direct
     * correspondence to a request and {@link #hasRequestId()} will return false.
     */
    protected final void setRequestId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("request id must be positive");
        }
        mRequestId = id;
    }

    @VisibleForTesting
    public Callback getCallback() {
        return mCallback;
    }

    @Override
    public String toString() {
        return "{[" + mSequentialId + "] "
                + this.getClass().getSimpleName()
                + ", proto=" + getProtoEnum()
                + ", owner=" + getOwnerString()
                + ", cookie=" + getCookie()
                + ", requestId=" + getRequestId()
                + ", userId=" + getTargetUserId() + "}";
    }
}
