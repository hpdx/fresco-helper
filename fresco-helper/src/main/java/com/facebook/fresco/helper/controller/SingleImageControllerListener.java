package com.facebook.fresco.helper.controller;

import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.utils.DensityUtil;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * 单张图片显示控制器
 * <p>
 * Created by android_ls on 16-9-22.
 */
public class SingleImageControllerListener extends BaseControllerListener<ImageInfo> {

    private final SimpleDraweeView draweeView;
    private final int mMaxWidth;
    private final int mMaxHeight;

    public SingleImageControllerListener(SimpleDraweeView draweeView, int maxWidth, int maxHeight) {
        this.draweeView = draweeView;
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
    }

    public SingleImageControllerListener(SimpleDraweeView draweeView) {
        this(draweeView, 0, 0);
    }

    @Override
    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
        if (imageInfo == null || draweeView == null) {
            return;
        }

        ViewGroup.LayoutParams vp = draweeView.getLayoutParams();
        int width = imageInfo.getWidth();
        int height = imageInfo.getHeight();

        int maxWidthSize;
        int maxHeightSize;
        if (mMaxWidth > 0 && mMaxHeight > 0) {
            maxWidthSize = mMaxWidth;
            maxHeightSize = mMaxHeight;
        } else {
            maxWidthSize = DensityUtil.getDisplayWidth(draweeView.getContext());
            maxHeightSize = DensityUtil.getDisplayHeight(draweeView.getContext());
        }

        if (maxWidthSize > 1080) {
            maxWidthSize = 1080;
        }

        if (maxHeightSize > 1920) {
            maxHeightSize = 1920;
        }

        if (width > height) {
            if (width > maxWidthSize) {
                width = maxWidthSize;
            }
            vp.width = width;
            vp.height = (int) (imageInfo.getHeight() / (float) imageInfo.getWidth() * vp.width);
        } else {
            // width <= height
            if (height > maxHeightSize) {
                height = maxHeightSize;
            }

            vp.height = height;
            vp.width = (int) ((float) imageInfo.getWidth() / imageInfo.getHeight() * vp.height);
        }

        draweeView.requestLayout();
    }

}

