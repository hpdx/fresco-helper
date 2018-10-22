package com.android.fresco.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.listener.IResult;

/**
 * 用于演示从网络加载gif图片，返回Bitmap对象
 * gif取的是其第一帧
 *
 * Created by android_ls on 16/11/11.
 */

public class GIFFirstFrameActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_gif_bitmap);

        mImageView = (ImageView) findViewById(R.id.iv_thumbnail);

//        String url = "https://ws1.sinaimg.cn/large/610dc034ly1fjgfyxgwgnj20u00gvgmt.jpg";
//        String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496383829892&di=ac88e62a1424eaddcc03502e99e8168f&imgtype=0&src=http%3A%2F%2Fi3.17173cdn.com%2F2fhnvk%2FYWxqaGBf%2Fcms3%2FVqLgYAbkkbmrjhy.gif";
//        String url = "http://lh-avatar.liehuozhibo.com/20170911/d7682418510c7877fd4dd6afaa1be460.jpg?x-oss-process=image/resize,w_640,h_640/quality,q_90";
        Phoenix.with(GIFFirstFrameActivity.this)
                .setUrl(url)
//                .setWidth(DensityUtil.dipToPixels(GIFFirstFrameActivity.this, 100))
//                .setHeight(DensityUtil.dipToPixels(GIFFirstFrameActivity.this, 100))
                .setResult(new IResult<Bitmap>() {
                    @Override
                    public void onResult(Bitmap result) {
                        mImageView.setImageBitmap(result);
                    }
                }).load();
    }

}
