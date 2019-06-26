package com.android.fresco.demo;

import android.os.Bundle;
import android.view.View;

import com.facebook.fresco.helper.ImageLoader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        String url = "https://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
        View view = findViewById(R.id.ll_bg);
        ImageLoader.loadImageBlur(view, url);

    }

}
