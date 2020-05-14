package com.anbetter.album.engine;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.anbetter.album.callback.CompressCallback;
import com.anbetter.album.models.album.entity.PhotoInfo;
import com.anbetter.album.utils.system.SystemUtils;
import com.facebook.fresco.helper.executor.DispatcherTask;
import com.facebook.fresco.helper.utils.FileUtils;
import com.facebook.fresco.helper.utils.MLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;

public class LubanCompressEngine implements CompressEngine {

    private static LubanCompressEngine sInstance = null;

    private LubanCompressEngine() {
    }

    public static LubanCompressEngine getInstance() {
        if (null == sInstance) {
            synchronized (LubanCompressEngine.class) {
                if (null == sInstance) {
                    sInstance = new LubanCompressEngine();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void compress(final Context context, final ArrayList<PhotoInfo> photos, final CompressCallback callback) {
        callback.onStart();
        DispatcherTask.getInstance().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> paths = new ArrayList<>();
                    for (PhotoInfo photo : photos) {
                        if (photo.selectedOriginal) continue;
                        if (!TextUtils.isEmpty(photo.cropPath)) {
                            paths.add(photo.cropPath);
                        } else {
                            paths.add(photo.path);
                        }
                    }
                    if (paths.isEmpty()) {
                        callback.onSuccess(photos);
                        return;
                    }

                    String compressImageDir = FileUtils.getCompressImageDir(context);
                    MLog.i("compressImageDir = " + compressImageDir);

                    if (!SystemUtils.beforeAndroidTen()) {
                        ArrayList<String> compressPaths = new ArrayList<>();
                        for (int i = 0; i < paths.size(); i++) {
                            String newPath = copyPathToAndroidQ(context, paths.get(i));
                            compressPaths.add(newPath);
                        }

                        paths = compressPaths;
                    }
                    MLog.i("paths = " + paths.toString());

                    List<File> files = Luban.with(context)
                            .load(paths)
                            .ignoreBy(400)
                            .setTargetDir(compressImageDir)
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path)
                                            || path.toLowerCase().endsWith(".gif")
                                            || path.toLowerCase().endsWith(".mp4"));
                                }
                            }).get();
                    for (int i = 0, j = photos.size(); i < j; i++) {
                        PhotoInfo photo = photos.get(i);
                        photo.compressPath = files.get(i).getPath();
                    }
                    callback.onSuccess(photos);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onFailed(photos, e.getMessage());
                }
            }
        });
    }

    /**
     * 解析Android Q版本下图片
     * #耗时操作需要放在子线程中操作
     *
     * @param ctx
     * @return
     */
    public static String copyPathToAndroidQ(Context ctx, String path) {
        // 走普通的文件复制流程，拷贝至应用沙盒内来
        BufferedSource inBuffer = null;
        try {
            Uri uri = Uri.parse(path);
            final File outFile = FileUtils.getPhotoPath(ctx);
            inBuffer = Okio.buffer(Okio.source(Objects.requireNonNull(ctx.getContentResolver().openInputStream(uri))));
            boolean copyFileSuccess = bufferCopy(inBuffer, outFile);
            if (copyFileSuccess) {
                return outFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inBuffer != null && inBuffer.isOpen()) {
                    inBuffer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 拷贝文件
     *
     * @param outFile
     * @return
     */
    public static boolean bufferCopy(BufferedSource inBuffer, File outFile) {
        BufferedSink outBuffer = null;
        try {
            outBuffer = Okio.buffer(Okio.sink(outFile));
            outBuffer.writeAll(inBuffer);
            outBuffer.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inBuffer != null) {
                    inBuffer.close();
                }
                if (outBuffer != null) {
                    outBuffer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
