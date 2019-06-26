package com.android.fresco.demo;

import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.controller.SingleImageControllerListener;
import com.facebook.fresco.helper.utils.DensityUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 用于演示加载大图
 *
 * Created by android_ls on 16/11/11.
 */

public class SingleImageControllerDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_scale);

//        String url = "https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg";
        String url = "https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg";
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);
        Phoenix.with(simpleDraweeView)
                .setControllerListener(new SingleImageControllerListener(simpleDraweeView,
                        DensityUtil.dipToPixels(this, 200), DensityUtil.dipToPixels(this, 200)))
                .load(url);

        //        Phoenix.with(simpleDraweeView)
//                .setControllerListener(new SingleImageControllerListener(simpleDraweeView))
//                .load(url);

    }

}
