package com.facebook.fresco.helper.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

/**
 *  Created by android_ls on 16/9/10.
 */
public class FileUtils {

    /**
     * 本地与我们应用程序相关文件存放的根目录
     */
    private static final String ROOT_DIR_PATH = "/fresco-helper";

    /**
     * 下载图片文件存放的目录
     */
    private static final String IMAGE_DOWNLOAD_IMAGES_PATH = "/Download/Images/";

    /**
     * 获取下载图片文件存放的目录
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getImageDownloadDir(Context context) {
        return getRootDir(context) + IMAGE_DOWNLOAD_IMAGES_PATH;
    }

    /**
     * 获取SDCard卡或者手机内存的根路径（优先获取SDCard卡的根路径）
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getRootDir(Context context) {
        String rootDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + ROOT_DIR_PATH;
        } else {
            rootDir = context.getApplicationContext().getCacheDir().getAbsolutePath() + ROOT_DIR_PATH;
        }
        return rootDir;
    }

    /**
     * 随机生成一个文件，用于存放下载的图片
     * @param context Context
     * @return
     */
    public static String getImageDownloadPath(Context context) {
        String imageRootDir = getImageDownloadDir(context);
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        return dir + File.separator + fileName;
    }

}
