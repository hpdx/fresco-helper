package com.facebook.fresco.helper.listener;

/**
 * 下载图片的结果监听器
 *
 * Created by android_ls on 16/9/20.
 */
public abstract class DownloadImageResult {

    private String mFilePath;

    public DownloadImageResult(String filePath) {
        this.mFilePath = filePath;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public abstract void onResult(String filePath);

}
