package com.android.fresco.demo;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.anbetter.log.MLog;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.fresco.helper.utils.DensityUtil;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * 用于演示加载大图
 *
 * Created by android_ls on 16/11/11.
 */

public class BigImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_big);

        final String url = "https://ws1.sinaimg.cn/large/610dc034ly1fgi3vd6irmj20u011i439.jpg";
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)findViewById(R.id.sdv_1);


//        RoundingParams roundingParams = new RoundingParams();
//        roundingParams.setRoundAsCircle(true);
//        roundingParams.setBorder(Color.RED, 20);
//
//        roundingParams.setBorderColor(ContextCompat.getColor(simpleDraweeView.getContext(),
//                R.color.default_background_color));
////        roundingParams.setBorderColor(Color.parseColor("#0xff0000"));
//        roundingParams.setBorderWidth(23f);
//
//        GenericDraweeHierarchy genericDraweeHierarchy
//                = GenericDraweeHierarchyBuilder.newInstance(simpleDraweeView.getResources())
//                .setRoundingParams(roundingParams)
//                .build();
//        simpleDraweeView.setHierarchy(genericDraweeHierarchy);


        Phoenix.with(simpleDraweeView)
                .setWidth(DensityUtil.getDisplayWidth(this))
                .setHeight(DensityUtil.getDisplayHeight(this))
                .setControllerListener(new ControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                MLog.i("onSubmit id = " + id);

            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                MLog.i("onFinalImageSet id = " + id);

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
        }).load(url);

    }

}
