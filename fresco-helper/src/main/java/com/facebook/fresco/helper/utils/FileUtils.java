package com.facebook.fresco.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.UUID;

public class FileUtils {

    public static final String TAG = "FileUtils";

    /**
     * 应用程序相关文件存放的根目录
     */
    private static final String ROOT_DIR_NAME = "CMoney";

    /**
     * 拍照或者从相册选取，照片在本地的存储目录
     */
    public static final String IMAGE_CAMERA_UPLOAD_PATH = "/Photo/Upload";

    /**
     * LOG文件存放的目录
     */
    public static final String LOG_PATH = "/Log";

    /**
     * 语音文件存放的目录
     */
    public static final String VOICE_PATH = "/Voice";

    /**
     * 下载图片文件存放的目录
     */
    public static final String IMAGE_DOWNLOAD_IMAGES_PATH = "/Download/Images/";

    /**
     * 下载文件存放的目录
     */
    public static final String IMAGE_DOWNLOAD_PATH = "/Download/";

    /**
     * 下载分享图片文件存放的目录
     */
    public static final String IMAGE_SHARE_IMAGES_PATH = "/Download/ShareImages/";

    /**
     * tmp临时存放文件夹
     */
    public static final String TMP_PATH = "/Tmp";

    /**
     * 拍摄视频存放路径
     */
    private static final String VIDEO_DIR_PATH = "/Video/";

    /**
     * 视频文件缓存目录
     */
    private static final String VIDEO_CACHE_DIR = "/Video/cache";

    /**
     * 压缩视频存放路径
     */
    private static final String COMPRESS_VIDEO_DIR_PATH = "/CompressVideo/";

    /**
     * 压缩图片存在的目录
     */
    private static final String COMPRESS_DIR_PATH = "/Compress/";

    /**
     * 磁盘缓存目录
     */
    private static final String DISK_CACHE_DIR = "/DiskCache";

    /**
     * 礼物缓存目录
     */
    private static final String GIFT_CACHE_DIR = "/gift";

    /**
     * App web 缓存目录
     */
    private static final String WEB_CACHE_DIR = "/WebCache";

    /**
     * OCR（身份证、行驶证、银行卡、人脸识别得到的照片）临时文件存储目录
     */
    private static final String OCR_DIR = "/ORC/";

    /**
     * 获取OCR（身份证、行驶证、银行卡、人脸识别得到的照片）的临时存储文件
     *
     * @param context
     * @return
     */
    public static String getOcrFile(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.OCR_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "ocr_pic.jpg";
        return dir + File.separator + fileName;
    }

    /**
     * 获取App web 缓存目录
     *
     * @param context
     * @return
     */
    public static String getWebCacheDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        if (!TextUtils.isEmpty(rootDir)) {
            File dir = new File(rootDir + File.separator + FileUtils.WEB_CACHE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取礼物缓存目录
     *
     * @param context
     * @return
     */
    public static String getGiftDirectory(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + GIFT_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + "/";
    }

    /**
     * 获取磁盘缓存路径
     *
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + DISK_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取视频文件所存放的目录
     *
     * @param context
     * @return
     */
    public static String getVideoDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.VIDEO_DIR_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + "/";
    }

    /**
     * 视频文件缓存目录
     *
     * @param context
     * @return
     */
    public static File getVideoCacheDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.VIDEO_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取压缩后的图片文件所存放的目录
     *
     * @param context
     * @return
     */
    public static String getCompressImageDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.COMPRESS_DIR_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + "/";
    }

    /**
     * 获取视频文件所存放的目录
     *
     * @param context
     * @return
     */
    public static String getCompressVideoDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.COMPRESS_VIDEO_DIR_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + "/";
    }

    /**
     * 获取语音文件所存放的目录
     *
     * @param context
     * @return
     */
    public static String getVoiceDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        if (!TextUtils.isEmpty(rootDir)) {
            File dir = new File(rootDir + File.separator + FileUtils.VOICE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取语音文件名
     *
     * @param uid String
     * @return
     */
    public static String getVoiceFileName(String uid) {
        return uid + System.currentTimeMillis() + ".mp3";
    }

    /**
     * 检测要下载的语音文件是否已存在
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean existsVoiceFile(Context context, String fileName) {
        String filePath = getVoiceDir(context) + File.separator + fileName;
        MLog.i("filePath = " + filePath);
        return exists(filePath);
    }

    /**
     * 检查本地是否存在某个文件
     *
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        File file = new File(filePath);
        MLog.i(TAG, "检查本地是否存在 file = " + file.getAbsolutePath());
        return file.exists() && file.isFile();
    }

    /**
     * 获取要上传的图片的路径
     *
     * @param context
     * @return Upload Photo Path
     */
    public static String getUploadPhotoPath(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.IMAGE_CAMERA_UPLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        return dir + File.separator + fileName;
    }

    /**
     * 获取要上传的图片的路径
     *
     * @param context
     * @return Upload Photo Path
     */
    public static File getPhotoPath(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.IMAGE_CAMERA_UPLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            return File.createTempFile("IMG_", ".jpg", dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        String path = dir + File.separator + fileName;
        return new File(path);
    }

    /**
     * 获取要上传的图片所在目录
     *
     * @param context
     * @return Upload Photo Path
     */
    public static String getUploadPhotoDir(Context context) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + FileUtils.IMAGE_CAMERA_UPLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取下载图片文件存放的目录
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getImageDownloadDir(Context context) {
        String imageRootDir = getRootDir(context) + IMAGE_DOWNLOAD_IMAGES_PATH;
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static String getShareImageDownloadDir(Context context) {
        String imageRootDir = getRootDir(context) + IMAGE_SHARE_IMAGES_PATH;
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 保存二维码图片
     *
     * @return
     */
    public static String getImageQRCodePath() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir + File.separator + "qrCodeImage.jpg";
    }

    /**
     * 获取临时文件存放的目录
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getTmpDir(Context context, String fileName) {
        String imageRootDir = getRootDir(context) + TMP_PATH;
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + File.separator + fileName;
    }

    /**
     * 获取临时文件存放的目录
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getTempDir(Context context) {
        String imageRootDir = getRootDir(context) + TMP_PATH;
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + File.separator;
    }

    /**
     * 获取Log文件存放的目录
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径 /storage/emulated/0/Android/data/com.trident.hungerdating/Log/agorasdk.log
     */
    public static String getLogPathDir(Context context, String fileName) {
        String rootDir = FileUtils.getRootDir(context);
        File dir = new File(rootDir + File.separator + LOG_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir + File.separator + fileName;
    }

    /**
     * 获取外部存储路径
     * <p>
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getRootDir(Context context) {
        // 内部存储
        // /data/data/包名/files
        // context.getFilesDir().getPath()

        // /data/data/包名/cache
        // context.getCacheDir().getPath()

        // 外部存储
        // /sdcard/Android/data/包名/cache/dir
        // context.getExternalFilesDir("dir").getPath()

        // /sdcard/Android/data/包名/cache
        // context.getExternalCacheDir().getPath()

        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // 外部存储可用
            File cacheFile = context.getExternalFilesDir(ROOT_DIR_NAME);
            if (cacheFile != null && cacheFile.exists()) {
                cachePath = cacheFile.getPath();
            }
            MLog.i(TAG, "getExternalFilesDir: " + cachePath);
            // context.getCacheDir();
        }

        if (TextUtils.isEmpty(cachePath)) {
            // 外部存储不可用
            cachePath = context.getFilesDir().getPath();
            MLog.i(TAG, "getFilesDir cachePath: " + cachePath);
        }
        return cachePath;
    }

    /**
     * 随机生成一个文件，用于存放下载的图片
     *
     * @param context Context
     * @return
     */
    public static String getImageDownloadPath(Context context) {
        String imageRootDir = FileUtils.getImageDownloadDir(context);
        String fileName = UUID.randomUUID().toString() + ".jpg";
        return imageRootDir + File.separator + fileName;
    }

    /**
     * 随机生成一个文件，用于存放分享的图片
     *
     * @param context Context
     * @return
     */
    public static String getShareImageDownloadPath(Context context) {
        String imageRootDir = FileUtils.getShareImageDownloadDir(context);
        String fileName = UUID.randomUUID().toString() + ".jpg";
        return imageRootDir + File.separator + fileName;
    }

    /**
     * 根据url获取文件名
     */
    public static String getFileName() {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        return fileName;
    }

    /**
     * 获取下载文件在本地存放的路径
     *
     * @param context
     * @param url
     * @return
     */
    public static String getDownloadPath(Context context, String url) {
        if (url.startsWith("/")) {
            return url;
        }

        String fileName = getFileName(url);
        String imageRootDir = getRootDir(context) + IMAGE_DOWNLOAD_IMAGES_PATH;
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir + File.separator + fileName;
    }


    /**
     * 获取下载文件在本地存放的路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFilePath(Context context, String fileName) {
        String imageRootDir = getRootDir(context) + IMAGE_DOWNLOAD_PATH;
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir + File.separator + fileName;
    }

    /**
     * 将Bitmap对象写入本地的File，返回File Path
     * 注：调用该方法是耗时操作，请在子线程中调用
     *
     * @param context Context
     * @param bitmap  Bitmap
     * @return File Path
     * @throws IOException
     */
    public static String writeFile(Context context, Bitmap bitmap) throws IOException {
        String photoPath = getImageDownloadPath(context);
        byte[] data = IOUtils.read(bitmap);
        IOUtils.write(photoPath, data);
        return photoPath;
    }

    /**
     * 清除指定目录的缓存文件
     *
     * @param context
     */
    public static void clear(Context context, String dirPath) {
        try {
            String rootDir = FileUtils.getRootDir(context);
            File dir = new File(rootDir + File.separator + dirPath);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (File f : files) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有缓存文件
     *
     * @param context
     */
    public static void clearAll(final Context context) {
        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    String rootDir = FileUtils.getRootDir(context);
                    File f = new File(rootDir);
                    if (f.exists()) {
                        delFilesInOneDirectory(f);
                    }
                }
            });
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除文件夹中所有非目录的文件.
     *
     * @param file 要删除的根目录
     */
    public static void delFilesInOneDirectory(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File f : childFiles) {
                if (f.isDirectory()) {
                    delFilesInOneDirectory(f);
                } else if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 获取文件大小
     * eg:1.5M 精确到小数点后一位.
     *
     * @param fileS
     * @return
     */
    public static String getFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String fileSizeString;
        /*if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else*/
        if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取音频文件的本地路径
     *
     * @param context
     * @param url     音频文件的url
     * @return
     */
    public static String getVoicePath(Context context, String url) {
        if (url.startsWith("/")) {
            return url;
        }

        String fileName = getFileName(url);
        String filePath = getVoiceDir(context) + File.separator + fileName;

        MLog.i(TAG, "getVoicePath fileName = " + fileName);
        MLog.i(TAG, "getVoicePath filePath = " + filePath);
        return filePath;
    }

    /**
     * 获取视频文件的本地路径
     *
     * @param context
     * @param url     文件的url
     * @return
     */
    public static String getVideoPath(Context context, String url) {
        if (url.startsWith("/")) {
            return url;
        }

        String fileName = getFileName(url);
        String filePath = getVideoDir(context) + fileName;

        return filePath;
    }

    /**
     * 获取视频压缩文件的路径
     */
    public static String getCompressVideoPath(Context context, String sourcePath) {
        String compressFileName = getFileName(sourcePath);
        String compressPath = getCompressVideoDir(context) + compressFileName;
        return compressPath;
    }

    /**
     * 根据url获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        String fileName = url.substring(url.lastIndexOf(File.separator) + 1);
        MLog.i(TAG, "fileName = " + fileName);
        return fileName;
    }

    /**
     * 获取tmp文件名
     *
     * @param fileName
     * @return
     */
    public static String getTmpFileName(String fileName) {
        String tmpName = fileName.substring(0, fileName.lastIndexOf(".")) + ".tmp";
        MLog.i(TAG, "tmpName = " + tmpName);
        return tmpName;
    }

    /**
     * 获取临时文件的路径
     *
     * @param context
     * @param url
     * @return
     */
    public static String getTmpFilePath(Context context, String url) {
        String fileName = getFileName(url);
        MLog.i("fileName = " + fileName);

        String tmpName = getTmpFileName(fileName);
        MLog.i("tmpName = " + tmpName);

        String tmpFilePath = FileUtils.getTmpDir(context, tmpName);
        MLog.i(TAG, "tmpFilePath = " + tmpFilePath);
        return tmpFilePath;
    }

    /**
     * 删除指定的文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        try {
            // MLog.i(TAG, "deleteFile path = " + path);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
//                boolean delStatus = deleteFileSafely(file);
//                MLog.i(TAG, "delStatus: " + delStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFileSafely(File file) {
        MLog.i(TAG, "deleteFileSafely. ");

        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            MLog.i(TAG, "tmpPath: " + tmpPath);
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @return
     */
    public static void deleteFolderFile(String filePath) {
//        if (!TextUtils.isEmpty(filePath)) {
//            new FolderDelTask().execute(filePath);
//        }
    }

    /**
     * 拼接路径
     * concatPath("/mnt/sdcard", "/DCIM/Camera")  	=>		/mnt/sdcard/DCIM/Camera
     * concatPath("/mnt/sdcard", "DCIM/Camera")  	=>		/mnt/sdcard/DCIM/Camera
     * concatPath("/mnt/sdcard/", "/DCIM/Camera")  =>		/mnt/sdcard/DCIM/Camera
     */
    public static String concatPath(String... paths) {
        StringBuilder result = new StringBuilder();
        if (paths != null) {
            for (String path : paths) {
                if (path != null && path.length() > 0) {
                    int len = result.length();
                    boolean suffixSeparator = len > 0 && result.charAt(len - 1) == File.separatorChar;//后缀是否是'/'
                    boolean prefixSeparator = path.charAt(0) == File.separatorChar;//前缀是否是'/'
                    if (suffixSeparator && prefixSeparator) {
                        result.append(path.substring(1));
                    } else if (!suffixSeparator && !prefixSeparator) {//补前缀
                        result.append(File.separatorChar);
                        result.append(path);
                    } else {
                        result.append(path);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 获取raw视频文件拷贝到sd卡后的路径   用于启动页播放本视频
     *
     * @param context        上下文对象
     * @param rawResId       R.raw.yourVideo
     * @param fileSuffix     例如  .mp4
     * @param fileCopyToPath 自己要放置的文件路径   Video/
     * @return 返回filePath
     */
    public static String getCopyRawResToSdcardPath(Context context, int rawResId, String fileSuffix, String fileCopyToPath) {
        String filePath = null;
        String fileDirPath = null;
        if (TextUtils.isEmpty(fileCopyToPath)) {
            filePath = getRootDir(context) + "/Video/video" + rawResId + fileSuffix;
        } else {
            filePath = getRootDir(context) + "/" + fileCopyToPath + "/video" + rawResId + fileSuffix;
            fileDirPath = getRootDir(context) + "/" + fileCopyToPath;
        }

        File file = new File(filePath);
        MLog.i("file = " + file.getAbsolutePath());
        if (file.exists() && file.isFile()) {
            MLog.i("fileExists");
            return filePath;
        }

        if (!TextUtils.isEmpty(fileDirPath)) {
            File fileDir = new File(fileDirPath);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
        }

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            int len;
            byte[] buffer = new byte[1024];
            inputStream = context.getResources().openRawResource(rawResId);
            outputStream = new FileOutputStream(file);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            return filePath;
        } catch (IOException e) {
            MLog.e("getCopyRawResToSdcardPath: " + e.toString());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                MLog.e("getCopyRawResToSdcardPath: " + e.getMessage());
            }
        }
        return filePath;
    }

    /**
     * 保存数据到文件
     *
     * @param inStream
     * @param filePath
     */
    private void saveToFile(InputStream inStream, String filePath) {
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(inStream);
            outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            int len = 0;
            while ((len = inputStream.read()) != -1) {
                outputStream.write(len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
