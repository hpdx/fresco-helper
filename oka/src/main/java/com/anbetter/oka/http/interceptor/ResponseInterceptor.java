package com.anbetter.oka.http.interceptor;

import com.anbetter.oka.http.callback.OnProgressRespResult;
import com.anbetter.oka.http.filebody.FileResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created by android_ls on 2017/9/15.
 */

public class ResponseInterceptor implements Interceptor {

    private OnProgressRespResult mOnProgressRespResult;

    public ResponseInterceptor(OnProgressRespResult listener) {
        this.mOnProgressRespResult = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.isSuccessful() && mOnProgressRespResult != null) {
            return response
                    .newBuilder()
                    .body(new FileResponseBody(response, mOnProgressRespResult))
                    .build();
        }
        return response;
    }

}
