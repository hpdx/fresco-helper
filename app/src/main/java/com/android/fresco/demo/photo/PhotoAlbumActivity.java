package com.android.fresco.demo.photo;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.android.fresco.demo.R;
import com.facebook.fresco.helper.photoview.PhotoX;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * 显示加载本地相册
 *
 * Created by android_ls on 16/11/11.
 */
public class PhotoAlbumActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MIME_TYPE};

    private ArrayList<PhotoInfo> mImageList = new ArrayList<>();
    private PhotoWallAdapter mPhotoWallAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_photo_wall);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_photo_wall);
        mLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPhotoWallAdapter = new PhotoWallAdapter(mImageList, new OnItemClickListener<PhotoInfo>() {

            @Override
            public void onItemClick(View view, ArrayList<PhotoInfo> photos, int position) {
//                MLog.i("position = " + position);
//                MLog.i("photos.get(position).thumbnailUrl = " + photos.get(position).thumbnailUrl);

                if(photos.size() > 100) {
                    PhotoX.with(PhotoAlbumActivity.this)
                            .setPhotoInfo(photos.get(position))
                            .setCurrentPosition(position)
                            .start();
                } else {
                    PhotoX.with(PhotoAlbumActivity.this)
                            .setPhotoList(photos)
                            .setCurrentPosition(position)
                            .start();
                }
            }
        });
        mRecyclerView.setAdapter(mPhotoWallAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null) {
            return;
        }

        try {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(IMAGE_PROJECTION[0]));
                if (!TextUtils.isEmpty(path)) {
                    File mFile = new File(path);
                    if (mFile.exists() && mFile.isFile()) {
                        String name = cursor.getString(cursor.getColumnIndex(IMAGE_PROJECTION[1]));
                        if (TextUtils.isEmpty(name)) {
                            continue;
                        }

                        PhotoInfo image = new PhotoInfo();
                        image.thumbnailUrl = path;
                        image.originalUrl = path;
                        mImageList.add(image);
                    }
                }
            }
        } finally {
            cursor.close();
        }

        mPhotoWallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
