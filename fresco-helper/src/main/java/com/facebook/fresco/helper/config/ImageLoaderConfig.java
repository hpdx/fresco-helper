package com.facebook.fresco.helper.config;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.anbetter.log.MLog;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 *
 * Created by android_ls on 16/9/8.
 */
public class ImageLoaderConfig {

    protected static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

    protected static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

    protected static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

    protected static final int MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE = 5 * ByteConstants.MB;

    protected Context mContext;
    protected ImagePipelineConfig mImagePipelineConfig;

    private static ImageLoaderConfig sImageLoaderConfig;

    private ImageLoaderConfig(Context context){
        mContext = context.getApplicationContext();
        toggleLog();
    }

    public static ImageLoaderConfig getInstance(Context context) {
        if(sImageLoaderConfig == null) {
            synchronized (ImageLoaderConfig.class) {
                if(sImageLoaderConfig == null) {
                    sImageLoaderConfig = new ImageLoaderConfig(context);
                }
            }
        }
        return sImageLoaderConfig;
    }

    /**
     * Creates config using android http stack as network backend.
     */
    public ImagePipelineConfig getImagePipelineConfig() {
        if (mImagePipelineConfig == null) {
            mImagePipelineConfig = createConfigBuilder()
                    .setBitmapsConfig(Bitmap.Config.RGB_565) // 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
                    .setDownsampleEnabled(true) // 在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使用
                    // 设置Jpeg格式的图片支持渐进式显示
//                    .setProgressiveJpegConfig(new ProgressiveJpegConfig() {
//                        @Override
//                        public int getNextScanNumberToDecode(int scanNumber) {
//                            return scanNumber + 2;
//                        }
//
//                        public QualityInfo getQualityInfo(int scanNumber) {
//                            boolean isGoodEnough = (scanNumber >= 5);
//                            return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
//                        }
//                    })
                    .setRequestListeners(getRequestListeners())
                    .setMemoryTrimmableRegistry(getMemoryTrimmableRegistry()) // 报内存警告时的监听
                    // 设置内存配置
                    .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier(
                            (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)))
                    .setMainDiskCacheConfig(getMainDiskCacheConfig()) // 设置主磁盘配置
                    .setSmallImageDiskCacheConfig(getSmallDiskCacheConfig()) // 设置小图的磁盘配置
                    .build();
        }
        return mImagePipelineConfig;
    }

    /**
     * Create ImagePipelineConfig Builder（默认配置为OKHttp3）
     * @return
     */
    protected ImagePipelineConfig.Builder createConfigBuilder() {
//        return ImagePipelineConfig.newBuilder(mContext);
        return OkHttpImagePipelineConfigFactory.newBuilder(mContext, getOkHttpClient());
    }

    /**
     * 当内存紧张时采取的措施
     * @return
     */
    protected MemoryTrimmableRegistry getMemoryTrimmableRegistry() {
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                MLog.i("Fresco onCreate suggestedTrimRatio = " + suggestedTrimRatio);

                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    // 清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });
        return memoryTrimmableRegistry;
    }

    /**
     * LOG开关
     */
    protected void toggleLog() {
//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    }

    /**
     * 设置网络请求监听
     * @return
     */
    protected Set<RequestListener> getRequestListeners() {
        Set<RequestListener> requestListeners = new HashSet<>();
//        requestListeners.add(new RequestLoggingListener());
        return requestListeners;
    }

    /**
     * 获取主磁盘配置
     * @return
     */
    protected DiskCacheConfig getMainDiskCacheConfig() {
        /**
         * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
         * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
         * 2、一些内存清理软件可以扫描出来，进行内存的清理
         */
        File fileCacheDir = mContext.getCacheDir();
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                fileCacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Fresco");
//            }

        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(mContext)
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                .setBaseDirectoryPath(fileCacheDir)
                .build();
        return mainDiskCacheConfig;
    }

    /**
     * 获取小图的磁盘配置（辅助）
     * @return
     */
    protected DiskCacheConfig getSmallDiskCacheConfig() {
        /**
         * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
         * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
         * 2、一些内存清理软件可以扫描出来，进行内存的清理
         */
        File fileCacheDir = mContext.getCacheDir();
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                fileCacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Fresco");
//            }

        DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(mContext)
                .setBaseDirectoryPath(fileCacheDir)
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE)
                .build();
        return smallDiskCacheConfig;
    }

    /**
     * 获取OkHttpClient，子类覆盖该方法，可以任意定制
     * @return
     */
    protected OkHttpClient getOkHttpClient() {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .addInterceptor(loggingInterceptor)
//                    .retryOnConnectionFailure(false)
                .build();
        return okHttpClient;
    }

}
