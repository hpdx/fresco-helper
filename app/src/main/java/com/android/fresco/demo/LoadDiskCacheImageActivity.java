package com.android.fresco.demo;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anbetter.log.MLog;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.listener.IResult;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * 用于演示从本地磁盘缓存获取Bitmap
 * <p>
 * Created by android_ls on 17/12/7.
 */

public class LoadDiskCacheImageActivity extends AppCompatActivity {

    private ImageView mImageView;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_local_disk_cache);

        mUrl = "https://ws1.sinaimg.cn/large/610dc034ly1fgi3vd6irmj20u011i439.jpg";
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv_1);
        mImageView = (ImageView) findViewById(R.id.local_image);

        MLog.i("isInDiskCacheSync = " + Phoenix.isInDiskCacheSync(Uri.parse(mUrl)));

        Phoenix.with(simpleDraweeView)
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                        MLog.i("onSubmit id = " + id);

                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        MLog.i("onFinalImageSet id = " + id);

                        if (Phoenix.isInDiskCacheSync(Uri.parse(mUrl))) {
                            MLog.i("isInDiskCacheSync=true");
                            Phoenix.with(mUrl)
                                    .setResult(new IResult<Bitmap>() {

                                        @Override
                                        public void onResult(Bitmap result) {
                                            MLog.i("onResult bitmap");
                                            mImageView.setImageBitmap(result);
                                        }
                                    }).loadLocalDiskCache();
                        }

                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                        MLog.i("onIntermediateImageSet id = " + id);

                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        MLog.i("onIntermediateImageFailed id = " + id);

                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        MLog.i("onFailure id = " + id);

                    }

                    @Override
                    public void onRelease(String id) {
                        MLog.i("onRelease id = " + id);

                    }
                }).load(mUrl);

    }

}
