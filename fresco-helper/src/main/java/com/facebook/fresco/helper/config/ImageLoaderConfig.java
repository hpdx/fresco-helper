package com.facebook.fresco.helper.config;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.fresco.helper.utils.MLog;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 * Created by android_ls on 16/9/8.
 */
public class ImageLoaderConfig {

    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

    private static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

    private static final int MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE = 5 * ByteConstants.MB;

    private static ImagePipelineConfig sImagePipelineConfig;

    /**
     * Creates config using android http stack as network backend.
     */
    public static ImagePipelineConfig getImagePipelineConfig(final Context context) {
        if (sImagePipelineConfig == null) {
            /**
             * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
             * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
             * 2、一些内存清理软件可以扫描出来，进行内存的清理
             */
            File fileCacheDir = context.getApplicationContext().getCacheDir();

            DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setBaseDirectoryPath(fileCacheDir)
                    .build();

            DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(fileCacheDir)
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE)
                    .build();

            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
            Set<RequestListener> requestListeners = new HashSet<>();
            requestListeners.add(new RequestLoggingListener());

            // 当内存紧张时采取的措施
            MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
            memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
                @Override
                public void trim(MemoryTrimType trimType) {
                    final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                    MLog.i("Fresco onCreate suggestedTrimRatio : %d", suggestedTrimRatio);

                    if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                            ) {
                        // 清除内存缓存
                        Fresco.getImagePipeline().clearMemoryCaches();
                    }
                }
            });

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            sImagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient)
//            sImagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                    .setBitmapsConfig(Bitmap.Config.RGB_565)
                    .setDownsampleEnabled(true)
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setRequestListeners(requestListeners)
                    .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                    .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier(
                            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
                    .setMainDiskCacheConfig(mainDiskCacheConfig)
                    .setSmallImageDiskCacheConfig(smallDiskCacheConfig)
                    .build();
        }
        return sImagePipelineConfig;
    }

}
