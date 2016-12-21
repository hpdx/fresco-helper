package com.android.fresco.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anbetter.log.MLog;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.listener.IResult;

/**
 * 用于演示从网络加载Bitmap，然后显示
 *
 * Created by android_ls on 16/11/11.
 */

public class BitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_bitmap);

        final ImageView imageView = (ImageView) findViewById(R.id.iv_thumbnail);
        String url = "http://ww1.sinaimg.cn/large/610dc034jw1fahy9m7xw0j20u00u042l.jpg";
        Phoenix.with(this)
                .setUrl(url)
                .setWidth(180)
                .setHeight(180)
                .setCircleBitmap(true) // 圆形的
                .setResult(new IResult<Bitmap>() {
                    @Override
                    public void onResult(Bitmap bitmap) {
                        MLog.i("Thread.currentThread().getId() = " + Thread.currentThread().getId());

                        imageView.setImageBitmap(bitmap);
                    }
                })
                .load();

    }

}
