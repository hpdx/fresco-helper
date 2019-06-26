package com.android.fresco.demo;

import android.os.Bundle;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;
import com.facebook.fresco.helper.utils.DensityUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 用于演示高斯模糊的实现
 *
 * Created by android_ls on 16/11/11.
 */

public class BlurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_blur);

        String url = "https://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
        simpleDraweeView.setAspectRatio(0.7f);
        ViewGroup.LayoutParams lvp = simpleDraweeView.getLayoutParams();
        lvp.width = DensityUtil.getDisplayWidth(this);

        ImageLoader.loadImageBlur(simpleDraweeView, url);

    }

}
