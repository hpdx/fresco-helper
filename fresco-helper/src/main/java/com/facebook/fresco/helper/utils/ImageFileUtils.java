package com.facebook.fresco.helper.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by android_ls on 16/9/10.
 */
public class ImageFileUtils {

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
     * 获取外部存储路径
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getRootDir(Context context) {
        return context.getApplicationContext().getFilesDir().getPath() + ROOT_DIR_PATH;
    }

    /**
     * 随机生成一个文件，用于存放下载的图片
     *
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

    /**
     * 获取下载文件在本地存放的路径
     *
     * @param context
     * @param url
     * @return
     */
    public static String getImageDownloadPath(Context context, String url) {
        if (url.startsWith("/")) {
            return url;
        }

        String fileName = getFileName(url);
        String imageRootDir = getImageDownloadDir(context);
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir + File.separator + fileName;
    }

    /**
     * 根据url获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        String fileName = url.substring(url.lastIndexOf(File.separator) + 1);
//        MLog.i("fileName = " + fileName);
        return fileName;
    }

    /**
     * 检查本地是否存在某个文件
     *
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static String getFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String fileSizeString;
        if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
