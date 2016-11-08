package com.facebook.fresco.helper.photo;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.fresco.helper.R;
import com.facebook.fresco.helper.photo.entity.PhotoInfo;
import com.facebook.fresco.helper.photodraweeview.OnPhotoTapListener;
import com.facebook.fresco.helper.photodraweeview.OnViewTapListener;
import com.facebook.fresco.helper.photodraweeview.PhotoDraweeView;
import com.facebook.fresco.helper.utils.MLog;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/9/13.
 */
public class PictureBrowseAdapter extends PagerAdapter {

    private ArrayList<PhotoInfo> mItems;
    private OnPhotoTapListener mOnPhotoTapListener;
    private View.OnLongClickListener mOnLongClickListener;

    public PictureBrowseAdapter(ArrayList<PhotoInfo> items,
                                OnPhotoTapListener photoTapListener, View.OnLongClickListener onLongClickListener) {
        mItems = items;
        mOnPhotoTapListener = photoTapListener;
        mOnLongClickListener = onLongClickListener;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        final PhotoInfo photoInfo = mItems.get(position);
        MLog.i("photoInfo.originalUrl = " + photoInfo.originalUrl);

        View contentView = View.inflate(container.getContext(), R.layout.picture_browse_item, null);
        if (!TextUtils.isEmpty(photoInfo.originalUrl)) {
            final PhotoDraweeView photoDraweeView = (PhotoDraweeView) contentView.findViewById(R.id.photo_drawee_view);
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();

            Uri uri = Uri.parse(photoInfo.originalUrl);
            if (!UriUtil.isNetworkUri(uri)) {
                uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_FILE_SCHEME)
                        .path(photoInfo.originalUrl)
                        .build();
            }

            controller.setUri(uri);
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoDraweeView.setController(controller.build());

            photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (mOnPhotoTapListener != null) {
                        mOnPhotoTapListener.onPhotoTap(view, x, y);
                    }
                }
            });

            photoDraweeView.setOnLongClickListener(mOnLongClickListener);
            photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {

                }
            });
        }

        container.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return contentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mItems != null && mItems.size() > 0 && position < mItems.size()) {
            PhotoInfo photoInfo = mItems.get(position);
            if (!TextUtils.isEmpty(photoInfo.originalUrl)) {
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                Uri uri = Uri.parse(photoInfo.originalUrl);
                if (imagePipeline.isInBitmapMemoryCache(uri)) {
                    imagePipeline.evictFromMemoryCache(uri);
                }
            }
        }

        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void recycler() {
        mOnPhotoTapListener = null;
        mOnLongClickListener = null;
    }

}