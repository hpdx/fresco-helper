package com.android.fresco.demo.config;

import android.content.Context;

import com.android.fresco.demo.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.common.logging.FLog;
import com.facebook.fresco.helper.config.ImageLoaderConfig;
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

public class PhoenixConfig extends ImageLoaderConfig {

    private static PhoenixConfig sImageLoaderConfig;

    protected PhoenixConfig(Context context) {
        super(context);
    }

    public static PhoenixConfig get(Context context) {
        if (sImageLoaderConfig == null) {
            synchronized (PhoenixConfig.class) {
                if (sImageLoaderConfig == null) {
                    sImageLoaderConfig = new PhoenixConfig(context);
                }
            }
        }
        return sImageLoaderConfig;
    }

    /**
     * LOG开关
     */
    @Override
    protected void toggleLog() {
        // 你可以通过下面这条shell命令来查看Fresco日志：
        // adb logcat -v threadtime | grep -iE 'LoggingListener|AbstractDraweeController|BufferedDiskCache'
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    }

    /**
     * 设置网络请求监听
     *
     * @return
     */
    @Override
    protected Set<RequestListener> getRequestListeners() {
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener()); //LOG过滤标签： RequestLoggingListener
        return requestListeners;
    }

    /**
     * 使用OKHttp3替换原有的网络请求
     *
     * @return
     */
    @Override
    protected ImagePipelineConfig.Builder createConfigBuilder() {
        // LOG过滤标签：Ok
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(false)
                .build();

        return OkHttpImagePipelineConfigFactory.newBuilder(mContext, okHttpClient);
    }

}
