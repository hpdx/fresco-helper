package com.facebook.fresco.helper;

import android.content.Context;
import android.util.AttributeSet;

import com.anbetter.log.MLog;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.fresco.helper.listener.OnProgressListener;

/**
 * Created by android_ls on 2017/5/17.
 */

public class LargePhotoView extends SubsamplingScaleImageView {

    private OnProgressListener mOnProgressListener;
    public LargePhotoView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public LargePhotoView(Context context) {
        super(context);
    }

    public void setOnProgressListener(OnProgressListener listener) {
        this.mOnProgressListener = listener;
    }

    /**
     * 加载进度
     * @param progress 0~100
     */
    public void onProgress(int progress) {
        MLog.i("progress = " + progress);
        if(mOnProgressListener != null) {
            mOnProgressListener.onProgress(progress);
        }
    }

}
