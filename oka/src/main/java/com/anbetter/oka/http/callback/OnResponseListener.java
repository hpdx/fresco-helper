package com.anbetter.oka.http.callback;

/**
 * <p>
 * Created by android_ls on 2020/3/26 10:47 AM.
 *
 * @author android_ls
 * @version 1.0
 */
public interface OnResponseListener<T> extends OnErrorListener {
    void onResponse(T response);
}