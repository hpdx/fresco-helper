package com.android.fresco.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anbetter.log.MLog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;
import com.facebook.fresco.helper.Phoenix;

/**
 * Created by android_ls on 16/11/10.
 */

public class FrescoBaseUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_base_use);

        String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";

        // 从网络加载一张图片
        Phoenix.with((SimpleDraweeView)findViewById(R.id.sdv_1)).load(url);
//        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_1), url);

        // 网络加载一张图片，并以圆形显示
        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_2), url);

        // 网络加载一张图片，并以圆形加边框显示
        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_3), url);

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_4), url);

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_5), url);

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_6), url);

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_7), "");

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_8), url);

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_9), url);
        findViewById(R.id.sdv_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.i("按下效果");

            }
        });

        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_10), url);

        String url2 = "http://www.baidu.com";
        ImageLoader.loadImage((SimpleDraweeView)findViewById(R.id.sdv_11), url2);

    }

}
