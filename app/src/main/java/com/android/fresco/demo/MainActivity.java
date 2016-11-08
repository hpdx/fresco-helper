package com.android.fresco.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.fresco.helper.config.ImageLoaderConfig;
import com.facebook.fresco.helper.utils.MLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MLog.init(true, "MLog");
        Fresco.initialize(this, ImageLoaderConfig.getImagePipelineConfig(this));

        findViewById(R.id.btn_open_photo_wall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoWallActivity.class));
            }
        });



//        ArrayList<PhotoInfo> data = new ArrayList<>();
//        PictureBrowse.newBuilder(this)
//                .setPhotoList(data)
//                .setThumbnailParentView(null)
//                .setCurrentPosition(0)
//                .enabledAnimation(true)
//                .build()
//                .start();
//
//        PictureBrowse.newBuilder(this)
//                .setPhotoList(data)
//                .setThumbnailView(null)
//                .setThumbnail(null)
//                .setCurrentPosition(0)
//                .enabledAnimation(true)
//                .build()
//                .start();
//
//        ArrayList<String> urls = new ArrayList<>();
//        PictureBrowse.newBuilder(this)
//                .setPhotoStringList(urls)
//                .setThumbnailView(null)
//                .setThumbnail(null)
//                .setCurrentPosition(0)
//                .enabledAnimation(true)
//                .build()
//                .start();

    }
}
