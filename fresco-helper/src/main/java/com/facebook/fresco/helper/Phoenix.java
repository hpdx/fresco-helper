package com.facebook.fresco.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.anbetter.log.MLog;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.config.ImageLoaderConfig;
import com.facebook.fresco.helper.listener.IDownloadResult;
import com.facebook.fresco.helper.listener.IResult;
import com.facebook.fresco.helper.utils.CircleBitmapTransform;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Facebook开源的Android图片加载库Fresco的使用帮助类
 *
 * Created by android_ls on 16/11/13.
 */

public final class Phoenix {

    private Phoenix() {

    }

    public static void init(Context context) {
        MLog.init(true, "MLog");
        Fresco.initialize(context, ImageLoaderConfig.getImagePipelineConfig(context));
    }

    public static Builder with(SimpleDraweeView simpleDraweeView) {
        return new Builder().build(simpleDraweeView);
    }

    public static Builder with(Context context) {
        return new Builder().build(context);
    }

    public static Builder with(String url) {
        return new Builder().build(url);
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private Context mContext;
        private SimpleDraweeView mSimpleDraweeView;
        private String mUrl;
        private int mWidth;
        private int mHeight;
        private float mAspectRatio;
        private boolean mNeedBlur;
        private boolean mSmallDiskCache;
        private boolean mCircleBitmap;
        private BasePostprocessor mPostprocessor;
        private ControllerListener<ImageInfo> mControllerListener;
        private IResult<Bitmap> mResult;
        private IDownloadResult mDownloadResult;

        public Builder build(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder build(Context context) {
            this.mContext = context.getApplicationContext();
            return this;
        }

        public Builder build(SimpleDraweeView simpleDraweeView) {
            this.mSimpleDraweeView = simpleDraweeView;
            return this;
        }

        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder setWidth(int reqWidth) {
            this.mWidth = reqWidth;
            return this;
        }

        public Builder setHeight(int reqHeight) {
            this.mHeight = reqHeight;
            return this;
        }

        public Builder setAspectRatio(float aspectRatio) {
            this.mAspectRatio = aspectRatio;
            return this;
        }

        public Builder setNeedBlur(boolean needBlur) {
            this.mNeedBlur = needBlur;
            return this;
        }

        public Builder setSmallDiskCache(boolean smallDiskCache) {
            this.mSmallDiskCache = smallDiskCache;
            return this;
        }

        public Builder setCircleBitmap(boolean circleBitmap) {
            this.mCircleBitmap = circleBitmap;
            return this;
        }

        public Builder setResult(IResult<Bitmap> result) {
            this.mResult = result;
            return this;
        }

        public Builder setResult(IDownloadResult result) {
            this.mDownloadResult = result;
            return this;
        }

        public Builder setBasePostprocessor(BasePostprocessor postprocessor) {
            this.mPostprocessor = postprocessor;
            return this;
        }

        public Builder setControllerListener(ControllerListener<ImageInfo> controllerListener) {
            this.mControllerListener = controllerListener;
            return this;
        }

        public void download() {
            if(TextUtils.isEmpty(mUrl)
                    || !UriUtil.isNetworkUri(Uri.parse(mUrl))
                    || mDownloadResult == null) {
                return;
            }

            ImageLoader.downloadImage(mContext, mUrl, mDownloadResult);
        }

        public void load() {
            if(TextUtils.isEmpty(mUrl) || !UriUtil.isNetworkUri(Uri.parse(mUrl))) {
                return;
            }

            // 目前只对从网络加载图片，提供支持
            ImageLoader.loadImage(mContext, mUrl,
                    mWidth,
                    mHeight,
                    new IResult<Bitmap>() {

                        @Override
                        public void onResult(Bitmap bitmap) {
                            if(mResult != null) {
                                if(mCircleBitmap) {
                                    mResult.onResult(CircleBitmapTransform.transform(bitmap));
                                } else {
                                    mResult.onResult(bitmap);
                                }
                            }
                        }
                    });
        }

        public void load(String url) {
            if (TextUtils.isEmpty(url) || mSimpleDraweeView == null) {
                return;
            }

            if (!mNeedBlur) {
                loadNormal(url);
            } else {
                loadBlur(url);
            }
        }

        public void load(int resId) {
            if(resId == 0 || mSimpleDraweeView == null) {
                return;
            }

            if(!mNeedBlur) {
                if (mWidth > 0 && mHeight > 0) {
                    ImageLoader.loadDrawable(mSimpleDraweeView, resId, mWidth, mHeight);
                } else if (handlerAspectRatio()) {
                    ImageLoader.loadDrawable(mSimpleDraweeView, resId);
                }
            } else {
                if (mWidth > 0 && mHeight > 0) {
                    ImageLoader.loadDrawableBlur(mSimpleDraweeView, resId, mWidth, mHeight);
                } else if (handlerAspectRatio()) {
                    ImageLoader.loadDrawableBlur(mSimpleDraweeView, resId);
                }
            }
        }

        private void loadNormal(String url) {
            handlerAspectRatio();

            Uri uri = Uri.parse(url);
            if(!UriUtil.isNetworkUri(uri)) {
                uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_FILE_SCHEME)
                        .path(url)
                        .build();
            }

            ImageLoader.loadImage(mSimpleDraweeView, uri, mWidth, mHeight, mPostprocessor,
                    mControllerListener, mSmallDiskCache);
        }

        private void loadBlur(String url) {
            Uri uri = Uri.parse(url);
            if(mWidth > 0 && mHeight > 0) {
                if (UriUtil.isNetworkUri(uri)) {
                    ImageLoader.loadImageBlur(mSimpleDraweeView, url, mWidth, mHeight);
                } else {
                    ImageLoader.loadFileBlur(mSimpleDraweeView, url, mWidth, mHeight);
                }
            } else if(handlerAspectRatio()) {
                if (UriUtil.isNetworkUri(uri)) {
                    ImageLoader.loadImageBlur(mSimpleDraweeView, url);
                } else {
                    ImageLoader.loadFileBlur(mSimpleDraweeView, url);
                }
            }
        }

        private boolean handlerAspectRatio() {
            if(mWidth > 0 && mHeight > 0) {
                ViewGroup.LayoutParams lvp = mSimpleDraweeView.getLayoutParams();
                lvp.width = mWidth;
                lvp.height = mHeight;
            } else if (mAspectRatio > 0 && (mWidth > 0 || mHeight > 0)) {
                ViewGroup.LayoutParams lvp = mSimpleDraweeView.getLayoutParams();
                if(mWidth > 0) {
                    lvp.width = mWidth;
                    lvp.height = (int)(mWidth / mAspectRatio);
                } else {
                    lvp.height = mHeight;
                    lvp.width = (int)(mHeight * mAspectRatio);
                }
                return true;
            }
            return false;
        }

    }

    /**
     * 从内存缓存中移除指定图片的缓存
     *
     * @param uri
     */
    public static void evictFromMemoryCache(final Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline.isInBitmapMemoryCache(uri)) {
            imagePipeline.evictFromMemoryCache(uri);
        }
    }

    /**
     * 从磁盘缓存中移除指定图片的缓存
     *
     * @param uri
     */
    public static void evictFromDiskCache(final Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline.isInDiskCacheSync(uri)) {
            imagePipeline.evictFromDiskCache(uri);
        }
    }

    /**
     * 移除指定图片的所有缓存（包括内存+磁盘）
     *
     * @param uri
     */
    public static void evictFromCache(final Uri uri) {
        evictFromMemoryCache(uri);
        evictFromDiskCache(uri);
    }

    /**
     * 清空所有内存缓存
     */
    public static void clearMemoryCaches() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清空所有磁盘缓存，若你配置有两个磁盘缓存，则两个都会清除
     */
    public static void clearDiskCaches() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /**
     * 清除所有缓存（包括内存+磁盘）
     */
    public static void clearCaches() {
        clearMemoryCaches();
        clearDiskCaches();
    }

    /**
     * 查找一张图片在已解码的缓存中是否存在
     *
     * @param uri
     * @return
     */
    public static boolean isInBitmapMemoryCache(final Uri uri) {
        return Fresco.getImagePipeline().isInBitmapMemoryCache(uri);
    }

    /**
     * 查找一张图片在磁盘缓存中是否存在，若配有两个磁盘缓存，则只要其中一个存在，就会返回true
     *
     * @param uri
     * @return
     */
    public static boolean isInDiskCacheSync(final Uri uri) {
        return isInDiskCacheSync(uri, ImageRequest.CacheChoice.SMALL) ||
                isInDiskCacheSync(uri, ImageRequest.CacheChoice.DEFAULT);
    }

    /**
     * 查找一张图片在磁盘缓存中是否存在，可以指定是哪个磁盘缓存
     *
     * @param uri
     * @param cacheChoice
     * @return
     */
    public static boolean isInDiskCacheSync(final Uri uri, final ImageRequest.CacheChoice cacheChoice) {
        return Fresco.getImagePipeline().isInDiskCacheSync(uri, cacheChoice);
    }

    /**
     * 需要暂停网络请求时调用
     */
    public static void pause() {
        Fresco.getImagePipeline().pause();
    }

    /**
     * 需要恢复网络请求时调用
     */
    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

    /**
     * 当前网络请求是否处于暂停状态
     *
     * @return
     */
    public static boolean isPaused() {
        return Fresco.getImagePipeline().isPaused();
    }

    /**
     * 预加载到内存缓存并解码
     *
     * @param url
     */
    public static void prefetchToBitmapCache(String url) {
        if(TextUtils.isEmpty(url)) {
            return;
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, null);
    }

    /**
     * 预加载到磁盘缓存（未解码）
     *
     * @param url
     */
    public static void prefetchToDiskCache(String url) {
        if(TextUtils.isEmpty(url)) {
            return;
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        Fresco.getImagePipeline().prefetchToDiskCache(imageRequest, null);
    }

}
