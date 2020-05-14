package com.facebook.fresco.helper.executor;

import androidx.annotation.NonNull;

/**
 * <p>
 * Created by android_ls on 2020/3/20 3:52 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public abstract class TaskExecutor {

    public abstract void executeOnDiskIO(@NonNull Runnable runnable);

    public abstract void postToMainThread(@NonNull Runnable runnable);

    public abstract boolean isMainThread();

    public void executeOnMainThread(@NonNull Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            postToMainThread(runnable);
        }
    }

}
