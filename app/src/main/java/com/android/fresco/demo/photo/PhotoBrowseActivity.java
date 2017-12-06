package com.android.fresco.demo.photo;

import android.view.View;

import com.anbetter.log.MLog;
import com.android.fresco.demo.R;
import com.facebook.fresco.helper.photoview.PictureBrowseActivity;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;

/**
 * 查看大图
 * Created by android_ls on 2017/1/20.
 */

public class PhotoBrowseActivity extends PictureBrowseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_browse;
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        findViewById(R.id.rl_top_deleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.i("用户点击了删除按钮");
                MLog.i("mPhotoIndex = " + mPhotoIndex);

                PhotoInfo photoInfo = mItems.get(mPhotoIndex);
                MLog.i("originalUrl = " + photoInfo.originalUrl);

            }
        });
    }

    @Override
    public boolean onLongClick(View view) {
        MLog.i("currentPosition = " + getCurrentPosition());

        PhotoInfo photoInfo = getCurrentPhotoInfo();
        MLog.i("current originalUrl = " + photoInfo.originalUrl);

        return super.onLongClick(view);
    }

}
