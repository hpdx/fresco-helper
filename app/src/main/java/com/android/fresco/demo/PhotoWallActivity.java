package com.android.fresco.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.facebook.fresco.helper.photo.PictureBrowse;
import com.facebook.fresco.helper.photo.entity.PhotoInfo;
import com.facebook.fresco.helper.utils.MLog;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/11/2.
 */

public class PhotoWallActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PhotoWallAdapter mPhotoWallAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);

        String[] images = {"http://g.hiphotos.baidu.com/imgad/pic/item/c75c10385343fbf22c362d2fb77eca8065388fa0.jpg"
        };

        ArrayList<PhotoInfo> data = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.originalUrl = images[i];
            photoInfo.thumbnailUrl = images[i];
            data.add(photoInfo);
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_photo_wall);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mPhotoWallAdapter = new PhotoWallAdapter(data, new OnItemClickListener<PhotoInfo>() {

            @Override
            public void onItemClick(ViewGroup parent, ArrayList<PhotoInfo> photos, int position) {
                MLog.i("position = " + position);
//                MLog.i("photos.get(position).thumbnailUrl = " + photos.get(position).thumbnailUrl);

                PictureBrowse.newBuilder(PhotoWallActivity.this)
                        .setParentView(parent)
                        .setCurrentPosition(position)
                        .setPhotoList(photos)
                        .enabledAnimation(true)
                        .build()
                        .start();
            }
        });
        mRecyclerView.setAdapter(mPhotoWallAdapter);

    }

}
