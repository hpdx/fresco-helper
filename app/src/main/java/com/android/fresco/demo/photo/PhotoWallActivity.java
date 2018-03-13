package com.android.fresco.demo.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.fresco.demo.R;
import com.facebook.fresco.helper.photoview.PhotoX;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;

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
                "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                "http://ww1.sinaimg.cn/large/610dc034ly1fjaxhky81vj20u00u0ta1.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fivohbbwlqj20u011idmx.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fj78mpyvubj20u011idjg.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fj3w0emfcbj20u011iabm.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fiz4ar9pq8j20u010xtbk.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fis7dvesn6j20u00u0jt4.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fir1jbpod5j20ip0newh3.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fik2q1k3noj20u00u07wh.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fiednrydq8j20u011itfz.jpg",
                "http://ww1.sinaimg.cn/large/610dc034ly1ffmwnrkv1hj20ku0q1wfu.jpg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-12-18380140_455327614813449_854681840315793408_n.jpg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-11-18380166_305443499890139_8426655762360565760_n.jpg",
                "http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg",
                "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-25-13651793_897557617014845_571817176_n.jpg",
                "https://a-ssl.duitang.com/uploads/item/201701/19/20170119220617_WwTh8.jpeg",
                "https://b-ssl.duitang.com/uploads/item/201705/14/20170514095630_L5aEZ.jpeg",
                "https://b-ssl.duitang.com/uploads/item/201505/23/20150523091525_n2xkL.jpeg",
                "https://a-ssl.duitang.com/uploads/item/201503/28/20150328132442_nRNSQ.jpeg",
                "http://img.hb.aicdn.com/d6fea1e327ba8ed64fc905895b16ab410f7efbb411eab6-jbGElU_fw658",
                "https://b-ssl.duitang.com/uploads/item/201608/28/20160828190814_fucBX.jpeg",
                "https://b-ssl.duitang.com/uploads/item/201610/08/20161008040554_rAwev.jpeg",
                "https://b-ssl.duitang.com/uploads/item/201701/02/20170102174129_vJ2Ns.thumb.700_0.jpeg",
                "https://b-ssl.duitang.com/uploads/item/201504/04/20150404H0402_d8S3E.thumb.700_0.jpeg",
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
                "https://f10.baidu.com/it/u=1733621832,446747761&fm=76",
                "http://ww3.sinaimg.cn/large/610dc034jw1f8mssipb9sj20u011hgqk.jpg"
        };

        ArrayList<PhotoInfo> data = new ArrayList<>();
        for (String image : images) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.originalUrl = image;
            photoInfo.thumbnailUrl = image;
            data.add(photoInfo);
        }

        PhotoInfo photoInfo = new PhotoInfo();
        String url = "http://tupian.enterdesk.com/2016/dqd/03/1401/11.jpg";
        photoInfo.originalUrl = url;
        photoInfo.thumbnailUrl = url;
        photoInfo.width = 1416;
        photoInfo.height = 885;
        data.add(photoInfo);

        photoInfo = new PhotoInfo();
        url = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490349606605&di=5510cf624bce949b977ba005f1dbaf84&imgtype=0&src=http%3A%2F%2Fattach.bbs.letv.com%2Fforum%2F201607%2F02%2F195153fz3mxtd50d0qjtz9.jpg";
        photoInfo.originalUrl = url;
        photoInfo.thumbnailUrl = url;
        photoInfo.width = 3840;
        photoInfo.height = 2160;
        data.add(photoInfo);

        photoInfo = new PhotoInfo();
        url = "http://img2.3lian.com/2014/f6/192/d/112.jpg";
        photoInfo.originalUrl = url;
        photoInfo.thumbnailUrl = url;
        photoInfo.width = 1573;
        photoInfo.height = 885;
        data.add(photoInfo);

        photoInfo = new PhotoInfo();
        url = "http://img5q.duitang.com/uploads/item/201402/24/20140224212510_eQRG5.thumb.700_0.jpeg";
        photoInfo.originalUrl = url;
        photoInfo.thumbnailUrl = url;
        photoInfo.width = 700;
        photoInfo.height = 7633;
        data.add(photoInfo);

        photoInfo = new PhotoInfo();
        url = "http://i3.sinaimg.cn/cj/roll/20081102/38082dbe379d1c04d0e7f0dc28134657.jpg";
        photoInfo.originalUrl = url;
        photoInfo.thumbnailUrl = url;
        photoInfo.width = 776;
        photoInfo.height = 1240;
        data.add(photoInfo);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_photo_wall);
        mLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoWallAdapter = new PhotoWallAdapter(data, new OnItemClickListener<PhotoInfo>() {

            @Override
            public void onItemClick(View view, ArrayList<PhotoInfo> photos, int position) {
//                MLog.i("position = " + position);
//                MLog.i("photos.get(position).thumbnailUrl = " + photos.get(position).thumbnailUrl);

                PhotoX.with(PhotoWallActivity.this, PhotoBrowseActivity.class)
                        .setLayoutManager(mLayoutManager)
                        .setPhotoList(photos)
                        .setCurrentPosition(position)
                        .enabledAnimation(true)
                        .toggleLongClick(false)
                        .start();

//                PictureBrowse.with(PhotoWallActivity.this)
//                        .setPhotoList(photos)
//                        .setCurrentPosition(position)
//                        .start();

//                String originalUrl = photos.get(position).originalUrl;
//                PictureBrowse.with(PhotoWallActivity.this)
//                        .setThumbnailView(view)
//                        .setOriginalUrl(originalUrl)
//                        .enabledAnimation(true)
//                        .start();

//                PictureBrowse.with(PhotoWallActivity.this)
//                        .setThumbnailView(view)
//                        .setPhotoInfo(photos.get(position))
//                        .enabledAnimation(true)
//                        .start();

//                String originalUrl = photos.get(position).originalUrl;
//                PictureBrowse.with(PhotoWallActivity.this)
//                        .setOriginalUrl(originalUrl)
//                        .start();

            }
        });
        mRecyclerView.setAdapter(mPhotoWallAdapter);

    }

}
