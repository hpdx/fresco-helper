package com.anbetter.album.callback;

import com.anbetter.album.models.album.entity.PhotoInfo;

import java.util.ArrayList;

public interface CompressCallback {
    /**
     * 压缩开始
     */
    void onStart();

    /**
     * 压缩成功
     *
     * @param photoInfos 压缩结果
     */
    void onSuccess(ArrayList<PhotoInfo> photoInfos);

    /**
     * 压缩失败
     *
     * @param photoInfos  压缩结果
     * @param message 压缩失败原因
     */
    void onFailed(ArrayList<PhotoInfo> photoInfos, String message);
}
