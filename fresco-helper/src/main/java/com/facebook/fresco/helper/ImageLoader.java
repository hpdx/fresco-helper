package com.facebook.fresco.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.blur.BitmapBlurHelper;
import com.facebook.fresco.helper.listener.IDownloadResult;
import com.facebook.fresco.helper.listener.IResult;
import com.facebook.fresco.helper.utils.StreamTool;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 提供基于Fresco的图片加载工具类
 * <p>
 * 在程序入口处添加下面代码，建议在Application的onCreate()方法内添加
 * Fresco.initialize(this, ImageLoaderConfig.getImagePipelineConfig(this));
 * <p>
 * Created by android_ls on 16/9/8.
 */
public class ImageLoader {

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, final int reqWidth, final int reqHeight) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url,
                                 final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView, String url, ControllerListener<ImageInfo> controllerListener) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, null, controllerListener, false);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, null, null, true);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url, final int reqWidth, final int reqHeight) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, true);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, true);
    }

    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url,
                                      final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (TextUtils.isEmpty(url) || simpleDraweeView == null) {
            return;
        }

        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, true);
    }

    public static void loadFile(final SimpleDraweeView simpleDraweeView, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadFile(final SimpleDraweeView simpleDraweeView, String filePath, final int reqWidth, final int reqHeight) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();

        BaseControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }

                ViewGroup.LayoutParams vp = simpleDraweeView.getLayoutParams();
                vp.width = reqWidth;
                vp.height = reqHeight;
                simpleDraweeView.requestLayout();
            }
        };
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, controllerListener, false);
    }

    public static void loadFile(SimpleDraweeView simpleDraweeView, String filePath, BasePostprocessor processor) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadFile(SimpleDraweeView simpleDraweeView, String filePath,
                                final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();

        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();

        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId, final int reqWidth, final int reqHeight) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId, BasePostprocessor processor) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadDrawable(SimpleDraweeView simpleDraweeView, int resId,
                                    final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (resId == 0 || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, null, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename, final int reqWidth, final int reqHeight) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, null, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename, BasePostprocessor processor) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, 0, 0, processor, null, false);
    }

    public static void loadAssetDrawable(SimpleDraweeView simpleDraweeView, String filename,
                                         final int reqWidth, final int reqHeight, BasePostprocessor processor) {
        if (filename == null || simpleDraweeView == null) {
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(filename)
                .build();
        loadImage(simpleDraweeView, uri, reqWidth, reqHeight, processor, null, false);
    }

    /**
     * 从网络加载图片，并对图片进行高斯模糊处理
     *
     * @param view View
     * @param url  URL
     */
    public static void loadImageBlur(final View view, String url) {
        loadImage(view.getContext(), url, new IResult<Bitmap>() {

            @Override
            public void onResult(Bitmap source) {
                Bitmap blurBitmap = BitmapBlurHelper.blur(view.getContext(), source);
                view.setBackground(new BitmapDrawable(view.getContext().getResources(), blurBitmap));
            }
        });
    }

    public static void loadImageBlur(final View view, String url, final int reqWidth, final int reqHeight) {
        loadImage(view.getContext(), url, reqWidth, reqHeight, new IResult<Bitmap>() {

            @Override
            public void onResult(Bitmap source) {
                Bitmap blurBitmap = BitmapBlurHelper.blur(view.getContext(), source);
                view.setBackground(new BitmapDrawable(view.getContext().getResources(), blurBitmap));
            }
        });
    }

    public static void loadImageBlur(final SimpleDraweeView draweeView, String url) {
        loadImage(draweeView, url, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                BitmapBlurHelper.blur(bitmap, 35);
            }
        });
    }

    public static void loadImageBlur(final SimpleDraweeView draweeView, String url, final int reqWidth, final int reqHeight) {
        loadImage(draweeView, url, reqWidth, reqHeight, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                BitmapBlurHelper.blur(bitmap, 35);
            }
        });
    }

    public static void loadFileBlur(final SimpleDraweeView draweeView, String filePath) {
        loadFile(draweeView, filePath, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                BitmapBlurHelper.blur(bitmap, 35);
            }
        });
    }

    public static void loadFileBlur(final SimpleDraweeView draweeView, String filePath, final int reqWidth, final int reqHeight) {
        loadFile(draweeView, filePath, reqWidth, reqHeight, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                BitmapBlurHelper.blur(bitmap, 35);
            }
        });
    }

    public static void loadDrawableBlur(SimpleDraweeView simpleDraweeView, int resId, final int reqWidth, final int reqHeight) {
        loadDrawable(simpleDraweeView, resId, reqWidth, reqHeight, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                BitmapBlurHelper.blur(bitmap, 35);
            }
        });
    }

    public static void loadDrawableBlur(SimpleDraweeView simpleDraweeView, int resId) {
        loadDrawable(simpleDraweeView, resId, new BasePostprocessor() {
            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                BitmapBlurHelper.blur(bitmap, 35);
            }
        });
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView,
                                 Uri uri,
                                 final int reqWidth,
                                 final int reqHeight,
                                 BasePostprocessor postprocessor,
                                 ControllerListener<ImageInfo> controllerListener,
                                 boolean isSmall) {

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        imageRequestBuilder.setRotationOptions(RotationOptions.autoRotate());

        // 不支持图片渐进式加载，理由：https://github.com/facebook/fresco/issues/1204
        imageRequestBuilder.setProgressiveRenderingEnabled(false);

        if (isSmall) {
            imageRequestBuilder.setCacheChoice(ImageRequest.CacheChoice.SMALL);
        }

        if (reqWidth > 0 && reqHeight > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(reqWidth, reqHeight));
        }

        if (UriUtil.isLocalFileUri(uri)) {
            imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
        }

        if (postprocessor != null) {
            imageRequestBuilder.setPostprocessor(postprocessor);
        }

        ImageRequest imageRequest = imageRequestBuilder.build();

        PipelineDraweeControllerBuilder draweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        draweeControllerBuilder.setOldController(simpleDraweeView.getController());
        draweeControllerBuilder.setImageRequest(imageRequest);

        if (controllerListener != null) {
            draweeControllerBuilder.setControllerListener(controllerListener);
        }

        draweeControllerBuilder.setTapToRetryEnabled(true); // 开启重试功能
        draweeControllerBuilder.setAutoPlayAnimations(true); // 自动播放gif动画
        DraweeController draweeController = draweeControllerBuilder.build();
        simpleDraweeView.setController(draweeController);
    }

    /**
     * 根据提供的图片URL加载原始图（该方法仅针对大小在100k以内的图片，若不确定图片大小，
     * 请使用下面的downloadImage(String url, final DownloadImageResult loadFileResult) ）
     *
     * @param url             图片URL
     * @param loadImageResult LoadImageResult
     */
    public static void loadImage(Context context, String url, final IResult<Bitmap> loadImageResult) {
        loadOriginalImage(context, url, loadImageResult, UiThreadImmediateExecutorService.getInstance());
    }

    /**
     * 根据提供的图片URL加载原始图（该方法仅针对大小在100k以内的图片，若不确定图片大小，
     * 请使用下面的downloadImage(String url, final DownloadImageResult loadFileResult) ）
     *
     * @param url             图片URL
     * @param loadImageResult LoadImageResult
     * @param executor        的取值有以下三个：
     *                        UiThreadImmediateExecutorService.getInstance() 在回调中进行任何UI操作
     *                        CallerThreadExecutor.getInstance() 在回调里面做的事情比较少，并且不涉及UI
     *                        Executors.newSingleThreadExecutor() 你需要做一些比较复杂、耗时的操作，并且不涉及UI（如数据库读写、文件IO），你就不能用上面两个Executor。
     *                        你需要开启一个后台Executor，可以参考DefaultExecutorSupplier.forBackgroundTasks。
     */
    public static void loadOriginalImage(Context context, String url, final IResult<Bitmap> loadImageResult, Executor executor) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = builder.build();
        // 获取已解码的图片，返回的是Bitmap
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableImage> closeableReference = imageReference.clone();
                    try {
                        CloseableImage closeableImage = closeableReference.get();
                        if (closeableImage instanceof CloseableAnimatedImage) {
                            AnimatedImageResult animatedImageResult = ((CloseableAnimatedImage) closeableImage).getImageResult();
                            if (animatedImageResult != null && animatedImageResult.getImage() != null) {
                                int imageWidth = animatedImageResult.getImage().getWidth();
                                int imageHeight = animatedImageResult.getImage().getHeight();

                                Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, bitmapConfig);
                                animatedImageResult.getImage().getFrame(0).renderFrame(imageWidth, imageHeight, bitmap);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(bitmap);
                                }
                            }
                        } else if (closeableImage instanceof CloseableBitmap) {
                            CloseableBitmap closeableBitmap = (CloseableBitmap) closeableImage;
                            Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                            if (bitmap != null && !bitmap.isRecycled()) {
                                // https://github.com/facebook/fresco/issues/648
                                final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(tempBitmap);
                                }
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadImageResult != null) {
                    loadImageResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, executor);
    }

    /**
     * 从网络下载图片
     * 1、根据提供的图片URL，获取图片数据流
     * 2、将得到的数据流写入指定路径的本地文件
     *
     * @param url            URL
     * @param loadFileResult LoadFileResult
     */
    public static void downloadImage(Context context, String url, final IDownloadResult loadFileResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = builder.build();

        // 获取未解码的图片数据
        DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished() || loadFileResult == null) {
                    return;
                }

                CloseableReference<PooledByteBuffer> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<PooledByteBuffer> closeableReference = imageReference.clone();
                    try {
                        PooledByteBuffer pooledByteBuffer = closeableReference.get();
                        InputStream inputStream = new PooledByteBufferInputStream(pooledByteBuffer);
                        String photoPath = loadFileResult.getFilePath();
                        byte[] data = StreamTool.read(inputStream);
                        StreamTool.write(photoPath, data);
                        loadFileResult.onResult(photoPath);
                    } catch (IOException e) {
                        loadFileResult.onResult(null);
                        e.printStackTrace();
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                int progress = (int) (dataSource.getProgress() * 100);
                if (loadFileResult != null) {
                    loadFileResult.onProgress(progress);
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadFileResult != null) {
                    loadFileResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, Executors.newSingleThreadExecutor());
    }

    /**
     * 从本地文件或网络获取Bitmap
     *
     * @param context
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @param loadImageResult
     */
    public static void loadImage(final Context context,
                                 String url,
                                 final int reqWidth,
                                 final int reqHeight,
                                 final IResult<Bitmap> loadImageResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        if (!UriUtil.isNetworkUri(uri)) {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(url)
                    .build();
        }

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (reqWidth > 0 && reqHeight > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(reqWidth, reqHeight));
        }
        ImageRequest imageRequest = imageRequestBuilder.build();

        // 获取已解码的图片，返回的是Bitmap
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableImage> closeableReference = imageReference.clone();
                    try {
                        CloseableImage closeableImage = closeableReference.get();
                        if (closeableImage instanceof CloseableAnimatedImage) {
                            AnimatedImageResult animatedImageResult = ((CloseableAnimatedImage) closeableImage).getImageResult();
                            if (animatedImageResult != null && animatedImageResult.getImage() != null) {
                                int imageWidth = animatedImageResult.getImage().getWidth();
                                int imageHeight = animatedImageResult.getImage().getHeight();

                                Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, bitmapConfig);
                                animatedImageResult.getImage().getFrame(0).renderFrame(imageWidth, imageHeight, bitmap);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(bitmap);
                                }
                            }
                        } else if (closeableImage instanceof CloseableBitmap) {
                            CloseableBitmap closeableBitmap = (CloseableBitmap) closeableImage;
                            Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                            if (bitmap != null && !bitmap.isRecycled()) {
                                // https://github.com/facebook/fresco/issues/648
                                final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(tempBitmap);
                                }
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadImageResult != null) {
                    loadImageResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

    /**
     * 从本地缓存文件中获取Bitmap
     *
     * @param url
     * @param loadImageResult
     */
    public static void loadLocalDiskCache(String url, final IResult<Bitmap> loadImageResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        // 获取已解码的图片，返回的是Bitmap
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest,
                ImageRequest.RequestLevel.DISK_CACHE);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableImage> closeableReference = imageReference.clone();
                    try {
                        CloseableImage closeableImage = closeableReference.get();
                        if (closeableImage instanceof CloseableAnimatedImage) {
                            AnimatedImageResult animatedImageResult = ((CloseableAnimatedImage) closeableImage).getImageResult();
                            if (animatedImageResult != null && animatedImageResult.getImage() != null) {
                                int imageWidth = animatedImageResult.getImage().getWidth();
                                int imageHeight = animatedImageResult.getImage().getHeight();

                                Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, bitmapConfig);
                                animatedImageResult.getImage().getFrame(0).renderFrame(imageWidth, imageHeight, bitmap);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(bitmap);
                                }
                            }
                        } else if (closeableImage instanceof CloseableBitmap) {
                            CloseableBitmap closeableBitmap = (CloseableBitmap) closeableImage;
                            Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                            if (bitmap != null && !bitmap.isRecycled()) {
                                // https://github.com/facebook/fresco/issues/648
                                final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                                if (loadImageResult != null) {
                                    loadImageResult.onResult(tempBitmap);
                                }
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadImageResult != null) {
                    loadImageResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

}
