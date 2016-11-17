package com.android.fresco.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.fresco.helper.photo.PictureBrowse;
import com.facebook.fresco.helper.photo.entity.PhotoInfo;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/11/2.
 */

public class PhotoWallActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PhotoWallAdapter mPhotoWallAdapter;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);

        String[] images = {
                "http://g.hiphotos.baidu.com/imgad/pic/item/c75c10385343fbf22c362d2fb77eca8065388fa0.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f9nuk0nvrdj20u011haci.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f9mp3xhjdhj20u00u0ta9.jpg",
                "http://ww2.sinaimg.cn/large/610dc034gw1f9lmfwy2nij20u00u076w.jpg",
                "http://ww2.sinaimg.cn/large/610dc034gw1f9kjnm8uo1j20u00u0q5q.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f9j7nvnwjdj20u00k0jsl.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f9frojtu31j20u00u0go9.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f9em0sj3yvj20u00w4acj.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f9dh2ohx2vj20u011hn0r.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f9cayjaa96j20u011hqbs.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f9b46kpoeoj20ku0kuwhc.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f978bh1cerj20u00u0767.jpg",
                "http://ww4.sinaimg.cn/large/610dc034gw1f96kp6faayj20u00jywg9.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f95hzq3p4rj20u011htbm.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f9469eoojtj20u011hdjy.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f91ypzqaivj20u00k0jui.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f8zlenaornj20u011idhv.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f8xz7ip2u5j20u011h78h.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f8xff48zauj20u00x5jws.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f8w2tr9bgzj20ku0mjdi8.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f8uxlbptw7j20ku0q1did.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f8rgvvm5htj20u00u0q8s.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f8qd9a4fx7j20u011hq78.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f8kmud15q1j20u011hdjg.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f8bc5c5n4nj20u00irgn8.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f8a5uj64mpj20u00u0tca.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f88ylsqjvqj20u011hn5i.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f87z2n2taej20u011h11h.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f867mvc6qjj20u00u0wh7.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f837uocox8j20f00mggoo.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f820oxtdzzj20hs0hsdhl.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f80uxtwgxrj20u011hdhq.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f7z9uxopq0j20u011hju5.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f7y296c5taj20u00u0tay.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7wws4xk5nj20u011hwhb.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7sszr81ewj20u011hgog.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f7rmrmrscrj20u011hgp1.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7qgschtz8j20hs0hsac7.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f7mixvc7emj20ku0dv74p.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7lughzrjmj20u00k9jti.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7kpy9czh0j20u00vn0us.jpg",
                "http://ww2.sinaimg.cn/large/610dc034jw1f7jkj4hl41j20u00mhq68.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7hu7d460oj20u00u075u.jpg",
                "http://ww1.sinaimg.cn/large/610dc034jw1f7ef7i5m1zj20u011hdjm.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f7d97id9mbj20u00u0q4g.jpg",
                "http://ww4.sinaimg.cn/large/610dc034jw1f7cmpd95iaj20u011hjtt.jpg",
                "http://ww2.sinaimg.cn/large/610dc034gw1f7bm1unn17j20u00u00wm.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f8mssipb9sj20u011hgqk.jpg"
        };

        ArrayList<PhotoInfo> data = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.originalUrl = images[i];
            photoInfo.thumbnailUrl = images[i];
            data.add(photoInfo);
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_photo_wall);
        mLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoWallAdapter = new PhotoWallAdapter(data, new OnItemClickListener<PhotoInfo>() {

            @Override
            public void onItemClick(ArrayList<PhotoInfo> photos, int position) {
//                MLog.i("position = " + position);
//                MLog.i("photos.get(position).thumbnailUrl = " + photos.get(position).thumbnailUrl);

                PictureBrowse.newBuilder(PhotoWallActivity.this)
                        .setLayoutManager(mLayoutManager)
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
