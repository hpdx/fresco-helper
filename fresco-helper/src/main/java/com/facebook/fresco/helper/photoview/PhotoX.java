package com.facebook.fresco.helper.photoview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.anbetter.log.MLog;
import com.facebook.fresco.helper.photoview.anim.ViewOptionsCompat;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;
import com.facebook.fresco.helper.utils.PhotoConstant;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Created by android_ls on 16/9/19.
 */
public class PhotoX {

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static Builder with(Context context, Class<?> targetClass) {
        return new Builder(context, targetClass);
    }

    public static class Builder {
        private Intent mIntent;
        private GridLayoutManager mLayoutManager;
        private ArrayList<String> mThumbnailList;
        private View mThumbnailView;
        private String mOriginalUrl;
        private Context mContext;

        private Builder(Context context) {
            mIntent = new Intent(context, PictureBrowseActivity.class);
            mContext = context;
        }

        private Builder(Context context, Class<?> targetClass) {
            mIntent = new Intent(context, targetClass);
            mContext = context;
        }

        public Builder setPhotoList(ArrayList<PhotoInfo> data) {
            int size = data.size();
            mThumbnailList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                PhotoInfo photoInfo = data.get(i);
                if (!TextUtils.isEmpty(photoInfo.thumbnailUrl)) {
                    mThumbnailList.add(photoInfo.thumbnailUrl);
                }
            }
            mIntent.putParcelableArrayListExtra(PhotoConstant.PHOTO_LIST_KEY, data);
            return this;
        }

        public Builder setPhotoStringList(ArrayList<String> data) {
            int size = data.size();
            mThumbnailList = new ArrayList<>();
            ArrayList<PhotoInfo> photos = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String imageUrl = data.get(i);
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.originalUrl = imageUrl;
                photoInfo.thumbnailUrl = imageUrl;
                photos.add(photoInfo);
                mThumbnailList.add(imageUrl);
            }
            mIntent.putParcelableArrayListExtra(PhotoConstant.PHOTO_LIST_KEY, photos);
            return this;
        }

        public Builder setOriginalUrl(String originalUrl) {
            mOriginalUrl = originalUrl;

            ArrayList<PhotoInfo> photos = new ArrayList<>();
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.originalUrl = originalUrl;
            photos.add(photoInfo);

            mIntent.putParcelableArrayListExtra(PhotoConstant.PHOTO_LIST_KEY, photos);
            mIntent.putExtra(PhotoConstant.PHOTO_ONLY_ONE_KEY, true);
            return this;
        }

        public Builder setPhotoInfo(PhotoInfo photoInfo) {
            mOriginalUrl = photoInfo.originalUrl;

            ArrayList<PhotoInfo> photos = new ArrayList<>();
            photos.add(photoInfo);

            mIntent.putParcelableArrayListExtra(PhotoConstant.PHOTO_LIST_KEY, photos);
            mIntent.putExtra(PhotoConstant.PHOTO_ONLY_ONE_KEY, true);
            return this;
        }

        /**
         * 当前被点击的View在照片墙中的索引
         */
        public Builder setCurrentPosition(int position) {
            mIntent.putExtra(PhotoConstant.PHOTO_CURRENT_POSITION_KEY, position);
            return this;
        }

        /**
         * 是否响应长按事件
         *
         * @param onLongClick
         * @return
         */
        public Builder toggleLongClick(boolean onLongClick) {
            mIntent.putExtra(PhotoConstant.PHOTO_LONG_CLICK_KEY, onLongClick);
            return this;
        }

        /**
         * 是否启用向下拖动关闭功能
         *
         * @param isDragClose
         * @return
         */
        public Builder enabledDragClose(boolean isDragClose) {
            mIntent.putExtra(PhotoConstant.PHOTO_DRAG_CLOSE, isDragClose);
            return this;
        }

        /**
         * 在打开/关闭大图浏览界面时，是否启用动画效果
         *
         * @param isAnimation
         * @return
         */
        public Builder enabledAnimation(boolean isAnimation) {
            mIntent.putExtra(PhotoConstant.PHOTO_ANIMATION_KEY, isAnimation);

            if (isAnimation) {
                if (mLayoutManager != null && mThumbnailList != null && mThumbnailList.size() > 0) {
                    Bundle bundle = ViewOptionsCompat.makeScaleUpAnimation(mLayoutManager, mThumbnailList);
                    mIntent.putExtras(bundle);
                } else if (mThumbnailView != null && mOriginalUrl != null) {
                    MLog.i("mOriginalUrl = " + mOriginalUrl);
                    Bundle bundle = ViewOptionsCompat.makeScaleUpAnimation(mThumbnailView, mOriginalUrl);
                    mIntent.putExtras(bundle);
                }
            }
            return this;
        }

        public Builder setLayoutManager(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            return this;
        }

        public Builder setThumbnailView(View thumbnailView) {
            this.mThumbnailView = thumbnailView;
            return this;
        }

        public void start() {
            mContext.startActivity(mIntent);
            ((Activity) mContext).overridePendingTransition(0, 0);
        }
    }

}
