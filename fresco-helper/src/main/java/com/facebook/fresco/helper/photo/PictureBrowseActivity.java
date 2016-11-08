package com.facebook.fresco.helper.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.facebook.fresco.helper.R;
import com.facebook.fresco.helper.photo.anim.TransitionCompat;
import com.facebook.fresco.helper.photo.entity.PhotoInfo;
import com.facebook.fresco.helper.photo.view.MViewPager;
import com.facebook.fresco.helper.photodraweeview.OnPhotoTapListener;
import com.facebook.fresco.helper.utils.MLog;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/9/13.
 */
public class PictureBrowseActivity extends FragmentActivity
        implements ViewPager.OnPageChangeListener, OnPhotoTapListener, View.OnLongClickListener {

    private int mPhotoIndex;
    private int mPhotoCount;
    private PhotoInfo mLookPhoto;

    private TextView tvPhotoIndex;
    private MViewPager mViewPager;

    private PictureBrowseAdapter mAdapter;
    private ArrayList<PhotoInfo> mItems;

    private TransitionCompat mTransitionCompat;
    private boolean mFirstExecAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_browse);

        Intent data = getIntent();
        mItems = data.getParcelableArrayListExtra(PictureBrowse.PHOTO_LIST_KEY);
        if (mItems == null || mItems.size() == 0) {
            MLog.i("mItems is NULL");
            onBackPressed();
            return;
        }

        mPhotoIndex = data.getIntExtra(PictureBrowse.PHOTO_CURRENT_POSITION_KEY, 0);
        MLog.i("mPhotoIndex = " + mPhotoIndex);
        setupViews();

        boolean isAnimation = data.getBooleanExtra(PictureBrowse.PHOTO_IS_ANIMATION_KEY, false);
        MLog.i("isAnimation = " + isAnimation);
        if (isAnimation) {
            mTransitionCompat = new TransitionCompat(PictureBrowseActivity.this);
            mFirstExecAnimation = data.getBooleanExtra(PictureBrowse.PHOTO_ONLY_ONE_ANIMATION_KEY, false);
            MLog.i("firstExecAnimation = " + mFirstExecAnimation);
            mTransitionCompat.setCurrentPosition(mFirstExecAnimation ? 0 : mPhotoIndex);
            mTransitionCompat.startTransition();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPhotoIndex = position;
        tvPhotoIndex.setText(String.format("%d/%d", mPhotoIndex + 1, mPhotoCount));
        if (mTransitionCompat != null && !mFirstExecAnimation) {
            MLog.i("onPageSelected mPhotoIndex = " + mPhotoIndex);
            mTransitionCompat.setCurrentPosition(mPhotoIndex);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (mTransitionCompat != null) {
            mTransitionCompat.finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        onBackPressed();
    }

    private void setupViews() {
        tvPhotoIndex = (TextView) findViewById(R.id.tv_photo_count);
        mViewPager = (MViewPager) findViewById(R.id.vp_picture_browse);
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(this);
        mAdapter = new PictureBrowseAdapter(mItems, this, this);
        mViewPager.setAdapter(mAdapter);

        mPhotoCount = mItems.size();
        tvPhotoIndex.setText(String.format("%d/%d", mPhotoIndex + 1, mPhotoCount));
        mViewPager.setCurrentItem(mPhotoIndex);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mLookPhoto != null) {
            mLookPhoto = null;
        }

        if (mItems != null) {
            mItems = null;
        }

        if (mAdapter != null) {
            mAdapter.recycler();
            mAdapter = null;
        }

        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
            mViewPager.setAdapter(null);
            mViewPager = null;
        }

        super.onDestroy();
    }

}
