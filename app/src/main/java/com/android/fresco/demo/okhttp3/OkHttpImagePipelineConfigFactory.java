package com.android.fresco.demo.okhttp3;

import android.content.Context;

import com.facebook.imagepipeline.core.ImagePipelineConfig;
import okhttp3.OkHttpClient;

/**
 * Created by android_ls on 2017/5/23.
 */

public class OkHttpImagePipelineConfigFactory {

    public static ImagePipelineConfig.Builder newBuilder(Context context, OkHttpClient okHttpClient) {
        return ImagePipelineConfig.newBuilder(context)
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient));
    }

}
