package com.android.fresco.demo;

import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 用于演示高斯模糊的实现
 *
 * Created by android_ls on 16/11/11.
 */

public class GifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_gif);

        String url = "https://n.sinaimg.cn/tech/transform/34/w512h322/20190626/ff7c-hyvnhqr0149633.gif";

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
        ImageLoader.loadImage(simpleDraweeView, url);

    }

}
