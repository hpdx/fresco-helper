package com.anbetter.album.engine;

import android.content.Context;

import com.anbetter.album.callback.CompressCallback;
import com.anbetter.album.models.album.entity.PhotoInfo;

import java.util.ArrayList;

/**
 * 图片压缩方式
 * Created by joker on 2019/8/1.
 */
public interface CompressEngine {
    /**
     * 压缩处理
     *
     * @param photoInfos 选择的图片
     */
    void compress(Context context, ArrayList<PhotoInfo> photoInfos, CompressCallback callback);
}
