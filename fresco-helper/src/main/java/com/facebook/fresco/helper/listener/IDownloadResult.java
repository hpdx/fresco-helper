package com.facebook.fresco.helper.listener;

import android.content.Context;

import com.facebook.fresco.helper.utils.ImageFileUtils;

/**
 * 下载图片的结果监听器
 * <p>
 * Created by android_ls on 16/9/20.
 */
public abstract class IDownloadResult implements IResult<String> {

    private String mFilePath;

    public IDownloadResult(String filePath) {
        this.mFilePath = filePath;
    }

    public IDownloadResult(Context context) {
        this.mFilePath = ImageFileUtils.getImageDownloadPath(context);
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void onProgress(int progress) {

    }

    @Override
    public abstract void onResult(String filePath);

}
