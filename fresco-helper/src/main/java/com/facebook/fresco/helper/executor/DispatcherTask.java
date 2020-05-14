package com.facebook.fresco.helper.executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Executor;

/**
 * <p>
 * Created by android_ls on 2020/3/20 3:56 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class DispatcherTask extends TaskExecutor {

    private static volatile DispatcherTask sInstance;

    @NonNull
    private TaskExecutor mDelegate;

    @NonNull
    private TaskExecutor mDefaultTaskExecutor;

    @NonNull
    private static final Executor sMainThreadExecutor = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            getInstance().postToMainThread(command);
        }
    };

    @NonNull
    private static final Executor sIOThreadExecutor = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            getInstance().executeOnDiskIO(command);
        }
    };

    private DispatcherTask() {
        mDefaultTaskExecutor = new SmartTaskExecutor();
        mDelegate = mDefaultTaskExecutor;
    }

    @NonNull
    public static DispatcherTask getInstance() {
        if (sInstance == null) {
            synchronized (DispatcherTask.class) {
                if (sInstance == null) {
                    sInstance = new DispatcherTask();
                }
            }
        }
        return sInstance;
    }

    public void setDelegate(@Nullable TaskExecutor taskExecutor) {
        mDelegate = taskExecutor == null ? mDefaultTaskExecutor : taskExecutor;
    }

    @Override
    public void executeOnDiskIO(@NonNull Runnable runnable) {
        mDelegate.executeOnDiskIO(runnable);
    }

    @Override
    public void postToMainThread(@NonNull Runnable runnable) {
        mDelegate.postToMainThread(runnable);
    }

    @NonNull
    public static Executor getMainThreadExecutor() {
        return sMainThreadExecutor;
    }

    @NonNull
    public static Executor getIOThreadExecutor() {
        return sIOThreadExecutor;
    }

    @Override
    public boolean isMainThread() {
        return mDelegate.isMainThread();
    }

}
