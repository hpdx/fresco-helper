package com.android.fresco.demo;

import android.app.Application;

import com.android.fresco.demo.okhttp3.OkHttpNetworkFetcher;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.config.PhoenixConfig;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by android_ls on 2017/6/12.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Phoenix.init(this);

        // LOG过滤标签： RequestLoggingListener
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        ImagePipelineConfig imagePipelineConfig = new PhoenixConfig.Builder(this)
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient))
                .setRequestListeners(requestListeners)
                .build();

        Phoenix.init(this, imagePipelineConfig); // this-->Context
    }

}
