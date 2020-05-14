package com.anbetter.oka.http.callback;

/**
 * <p>
 * Created by android_ls on 2020/3/16 8:24 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public interface OnProgressRespResult<T> extends OnResponseListener<T> {

    void onProgress(int progress, long current, long total);

}
