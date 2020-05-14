package com.anbetter.oka.http.dispatcher;

import android.os.Handler;
import android.os.Looper;

/**
 * <p>
 * Created by android_ls on 2020/3/14 3:51 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class Dispatcher {

    private Handler mHandler;
    private static Dispatcher sInstance;

    private Dispatcher() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    private static Dispatcher getInstance() {
        if (sInstance == null) {
            synchronized (Dispatcher.class) {
                if (sInstance == null) {
                    sInstance = new Dispatcher();
                }
            }
        }
        return sInstance;
    }

    public static void runOnUIThread(Runnable runnable) {
        getInstance().mHandler.post(runnable);
    }

}
