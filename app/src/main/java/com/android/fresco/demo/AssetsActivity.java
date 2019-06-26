package com.android.fresco.demo;

import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 从Assets中加载显示图片
 *
 * Created by android_ls on 17/05/15.
 */

public class AssetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_assets);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
        Phoenix.with(simpleDraweeView)
                .setAssets(true)
                .load("qingchun.jpg");

    }

}
