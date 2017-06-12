package com.android.fresco.demo;

import android.app.Application;

import com.android.fresco.demo.config.PhoenixConfig;
import com.facebook.fresco.helper.Phoenix;

/**
 * Created by android_ls on 2017/6/12.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // init Phoenix
        Phoenix.init(this, PhoenixConfig.get(this).getImagePipelineConfig());
    }

}
