package com.android.fresco.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.utils.DensityUtil;

/**
 * 用于演示高斯模糊的实现
 *
 * Created by android_ls on 16/11/11.
 */

public class WebpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_meizi);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
//        ViewGroup.LayoutParams lvp = simpleDraweeView.getLayoutParams();
//        lvp.width = DensityUtil.getDisplayWidth(this);
//        simpleDraweeView.setAspectRatio(0.6f); // 设置宽高比
//
//        ImageLoader.loadDrawable(simpleDraweeView, R.drawable.meizi_webp,
//                DensityUtil.getDisplayWidth(this), DensityUtil.getDisplayHeight(this));

        Phoenix.with(simpleDraweeView)
//                .setWidth(DensityUtil.getDisplayWidth(this))
                .setHeight(DensityUtil.getDisplayWidth(this))
                .setAspectRatio(0.64f)
                .load("http://ww4.sinaimg.cn/large/610dc034jw1fb8iv9u08ij20u00u0tc7.jpg");

    }

}
