package com.anbetter.oka.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created by android_ls on 2017/8/28.
 */

public class RetryInterceptor implements Interceptor {

    private static final String TAG = "RetryInterceptor";

    private int maxRetryCount; // 最大重试次数

    public RetryInterceptor(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int retryCount = 0;
        while (!response.isSuccessful() && retryCount < maxRetryCount) {
            retryCount++;
            response.close();
            response = chain.proceed(request);
        }
        return response;
    }

}
