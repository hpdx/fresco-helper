package com.android.fresco.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.fresco.helper.ImageLoader;

/**
 * 用于演示对任意View的背景，进行高斯模糊效果的实现
 *
 * Created by android_ls on 16/11/11.
 */

public class Blur2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_blur2);

        String url = "http://a.hiphotos.baidu.com/image/pic/item/55e736d12f2eb938d3de795ad0628535e4dd6fe2.jpg";
        View view = findViewById(R.id.ll_bg);
        ImageLoader.loadImageBlur(view, url);

    }

}
