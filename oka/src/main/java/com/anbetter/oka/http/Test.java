package com.anbetter.oka.http;

import com.anbetter.oka.http.callback.OnProgressRespResult;
import com.anbetter.oka.http.callback.OnResponseListener;
import com.anbetter.oka.http.interceptor.RetryInterceptor;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <p>
 * Created by android_ls on 2020/3/14 5:29 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class Test {

    public void test() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // builder.cookieJar(CookieManager.getInstance().getCookieJar());
        builder.retryOnConnectionFailure(true);
        builder.followRedirects(true); // 请求支持重定向
        builder.addInterceptor(new RetryInterceptor(2)); // 弱网环境，网络请求失败了，重试2次

        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();

        OkHttp.addHeader("ua", "");
        OkHttp.addHeader("token", "");
        OkHttp.setHttpClient(okHttpClient);

        String url = "/login";
        OkHttp.get(url)
                .asString()
                .result(new OnResponseListener<String>() {
                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });

    }

    public void test1() {
        String url = "";
        Map<String, String> params = null;
        OkHttp.post()
                .url(url)
                .params(params)
                .asString()
                .result(new OnResponseListener<String>() {

                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
    }

    public void test2() {
        String url = "";
        OkHttp.post()
                .url(url)
                .param("name", "莫轻舞")
                .param("age", 18)
                .asString()
                .result(new OnResponseListener<String>() {

                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
    }

    public void test3() {
        String url = "";
        OkHttp.get(url)
                .asObject(UserInfo.class)
                .result(new OnResponseListener<UserInfo>() {

                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(UserInfo response) {

                    }
                });
    }

    public void test4() {
        String url = "";
        Type listType = new TypeToken<List<UserInfo>>() {
        }.getType();

        OkHttp.get()
                .url(url)
                .asObject(listType)
                .result(new OnResponseListener<List<UserInfo>>() {

                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(List<UserInfo> response) {

                    }
                });
    }


    public void test5() {
        String url = "";
        String filePath = "";
        OkHttp.post(url)
                .param("name", "出菲林")
                .file("file", filePath)
                .asObject(String.class)
                .result(new OnProgressRespResult<String>() {
                    @Override
                    public void onProgress(int progress, long current, long total) {

                    }

                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
    }

    public void test6() {
        String url = "";
        String filePath = "";
        OkHttp.get(url)
                .path(filePath)
                .asString()
                .result(new OnProgressRespResult<String>() {
                    @Override
                    public void onProgress(int progress, long current, long total) {

                    }

                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
    }

    static class UserInfo {
        public String name;
        public int age;
    }


}
