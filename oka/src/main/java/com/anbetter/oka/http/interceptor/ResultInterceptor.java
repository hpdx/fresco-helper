package com.anbetter.oka.http.interceptor;

import androidx.annotation.NonNull;

import okhttp3.ResponseBody;

/**
 * <p>
 * Created by android_ls on 2020/3/16 12:10 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public interface ResultInterceptor {

    boolean intercept(@NonNull ResponseBody responseBody);

}
