package com.anbetter.album.utils.bitmap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.anbetter.album.EasyPhotos;
import com.anbetter.album.utils.system.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * bitmap工具类
 * Created by huan on 2017/9/4.
 */

public class BitmapUtils {
    /**
     * 回收bitmap
     *
     * @param bitmap 回收的bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    public static void recycle(Bitmap... bitmaps) {
        for (Bitmap b : bitmaps) {
            recycle(b);
        }
    }

    public static void recycle(List<Bitmap> bitmaps) {
        for (Bitmap b : bitmaps) {
            recycle(b);
        }
    }

    /**
     * 给图片添加水印，水印会根据图片宽高自动缩放处理
     *
     * @param watermark              水印
     * @param image                  添加水印的图片
     * @param offsetX                添加水印的X轴偏移量
     * @param offsetY                添加水印的Y轴偏移量
     * @param srcWaterMarkImageWidth 水印对应的原图片宽度,即ui制作水印时候参考的图片画布宽度,应该是已知的图片最大宽度
     * @param addInLeft              true 在左下角添加水印，false 在右下角添加水印
     */
    public static void addWatermark(Bitmap watermark, Bitmap image, int srcWaterMarkImageWidth, int offsetX, int offsetY, boolean addInLeft) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (0 == imageWidth || 0 == imageHeight) {
            throw new RuntimeException("AlbumBuilder: 加水印的原图宽或高不能为0！");
        }
        int watermarkWidth = watermark.getWidth();
        int watermarkHeight = watermark.getHeight();
        float scale = imageWidth / (float) srcWaterMarkImageWidth;
        if (scale > 1) scale = 1;
        else if (scale < 0.4) scale = 0.4f;
        int scaleWatermarkWidth = (int) (watermarkWidth * scale);
        int scaleWatermarkHeight = (int) (watermarkHeight * scale);
        Bitmap scaleWatermark = Bitmap.createScaledBitmap(watermark, scaleWatermarkWidth, scaleWatermarkHeight, true);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (addInLeft) {
            canvas.drawBitmap(scaleWatermark, offsetX, imageHeight - scaleWatermarkHeight - offsetY, paint);
        } else {
            canvas.drawBitmap(scaleWatermark, imageWidth - offsetX - scaleWatermarkWidth, imageHeight - scaleWatermarkHeight - offsetY, paint);
        }
        recycle(scaleWatermark);
    }

    /**
     * 给图片添加带文字和图片的水印，水印会根据图片宽高自动缩放处理
     *
     * @param watermark              水印图片
     * @param image                  要加水印的图片
     * @param srcWaterMarkImageWidth 水印对应的原图片宽度,即ui制作水印时候参考的图片画布宽度,应该是已知的图片最大宽度
     * @param text                   要添加的文字
     * @param offsetX                添加水印的X轴偏移量
     * @param offsetY                添加水印的Y轴偏移量
     * @param addInLeft              true 在左下角添加水印，false 在右下角添加水印
     */
    public static void addWatermarkWithText(@NonNull Bitmap watermark, Bitmap image, int srcWaterMarkImageWidth, @NonNull String text, int offsetX, int offsetY, boolean addInLeft) {
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        if (0 == imageWidth || 0 == imageHeight) {
            throw new RuntimeException("AlbumBuilder: 加水印的原图宽或高不能为0！");
        }
        float watermarkWidth = watermark.getWidth();
        float watermarkHeight = watermark.getHeight();
        float scale = imageWidth / (float) srcWaterMarkImageWidth;
        if (scale > 1) scale = 1;
        else if (scale < 0.4) scale = 0.4f;
        float scaleWatermarkWidth = watermarkWidth * scale;
        float scaleWatermarkHeight = watermarkHeight * scale;
        Bitmap scaleWatermark = Bitmap.createScaledBitmap(watermark, (int) scaleWatermarkWidth, (int) scaleWatermarkHeight, true);
        Canvas canvas = new Canvas(image);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        float textsize = (float) (scaleWatermark.getHeight() * 2) / (float) 3;
        textPaint.setTextSize(textsize);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, canvas.getWidth() / 3, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int textWidth = staticLayout.getWidth();
        int textHeight = staticLayout.getHeight();
        canvas.save();
        if (addInLeft) {
            canvas.translate(scaleWatermarkWidth + offsetX + scaleWatermarkWidth / 6, imageHeight - textHeight - offsetY - scaleWatermarkHeight / 6);
        } else {
            canvas.translate(imageWidth - offsetX - textWidth, imageHeight - textHeight - offsetY - scaleWatermarkHeight / 6);
        }
        staticLayout.draw(canvas);
        canvas.restore();

        Paint sacleWatermarkPaint = new Paint();
        sacleWatermarkPaint.setAntiAlias(true);
        if (addInLeft) {
            canvas.drawBitmap(scaleWatermark, offsetX, imageHeight - textHeight - offsetY - scaleWatermarkHeight / 6, sacleWatermarkPaint);
        } else {
            canvas.drawBitmap(scaleWatermark, imageWidth - textWidth - offsetX - scaleWatermarkWidth - scaleWatermarkWidth / 6, imageHeight - textHeight - offsetY - scaleWatermarkHeight / 6, sacleWatermarkPaint);
        }
        recycle(scaleWatermark);
    }


    /**
     * 保存Bitmap到DCIM文件夹
     *
     * @param act      上下文
     * @param bitmap   bitmap
     * @param callBack 保存图片后的回调，回调已经处于UI线程
     */
    public static void saveBitmapToDir(final Activity act, final Bitmap bitmap, final SaveBitmapCallBack callBack) {

        try {
            PackageManager packageManager = act.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(act.getPackageName(), 0);
            final String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (SystemUtils.beforeAndroidTen()) {
                        saveBitmapBeforeAndroidQ(act, applicationName, bitmap, callBack);
                    } else {
                        saveBitmapAndroidQ(act, applicationName, bitmap, callBack);
                    }
                }
            }).start();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveBitmapBeforeAndroidQ(Activity act, String dir, Bitmap b, final SaveBitmapCallBack callBack) {
        final String dirPath;
        if (SystemUtils.beforeAndroidTen()) {
            dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + dir;
        } else {
            dirPath = act.getExternalFilesDir(Environment.DIRECTORY_DCIM) + File.separator + dir;
        }
        File dirF = new File(dirPath);

        if (!dirF.exists() || !dirF.isDirectory()) {
            if (!dirF.mkdirs()) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onCreateDirFailed();
                    }
                });
                return;
            }
        }

        try {
            final File writeFile = File.createTempFile("IMG_", ".png", dirF);

            FileOutputStream fos;
            fos = new FileOutputStream(writeFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            EasyPhotos.notifyMedia(act, writeFile);
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(writeFile.getAbsolutePath());
                }
            });

        } catch (final IOException e) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailed(e);
                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void saveBitmapAndroidQ(Activity act, String dir, Bitmap b, final SaveBitmapCallBack callBack) {
        long dataTake = System.currentTimeMillis();
        String jpegName = "IMG_" + dataTake + ".jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, jpegName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/" + dir);

        Uri external;
        ContentResolver resolver = act.getContentResolver();
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            external = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        final Uri insertUri = resolver.insert(external, values);
        if (insertUri == null) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onCreateDirFailed();
                }
            });
            return;
        }
        OutputStream os;
        try {
            os = resolver.openOutputStream(insertUri);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            if (os != null) {
                os.flush();
                os.close();
            }
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(insertUri.toString());
                }
            });
        } catch (final IOException e) {
            e.printStackTrace();
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailed(e);
                }
            });
        }
    }


    /**
     * 把View画成Bitmap
     *
     * @param view 要处理的View
     * @return Bitmap
     */
    public static Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

}
