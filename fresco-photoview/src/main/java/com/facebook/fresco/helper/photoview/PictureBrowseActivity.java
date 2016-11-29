package com.facebook.fresco.helper.photoview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.anbetter.log.MLog;
import com.facebook.fresco.helper.photoview.anim.TransitionCompat;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;
import com.facebook.fresco.helper.photoview.photodraweeview.OnPhotoTapListener;

import java.util.ArrayList;
import java.util.Locale;

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
    private  boolean mIsAnimation;

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
        mIsAnimation = data.getBooleanExtra(PictureBrowse.PHOTO_IS_ANIMATION_KEY, false);
        MLog.i("isAnimation = " + mIsAnimation);
        mFirstExecAnimation = data.getBooleanExtra(PictureBrowse.PHOTO_ONLY_ONE_ANIMATION_KEY, false);
        MLog.i("firstExecAnimation = " + mFirstExecAnimation);

        setupViews();

        if (mIsAnimation) {
            mTransitionCompat = new TransitionCompat(PictureBrowseActivity.this);
            mTransitionCompat.setCurrentPosition(mPhotoIndex);
            mTransitionCompat.startTransition();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(mFirstExecAnimation) {
            return;
        }

        mPhotoIndex = position;
        tvPhotoIndex.setText(String.format(Locale.getDefault(), "%d/%d", mPhotoIndex + 1, mPhotoCount));

        if (mTransitionCompat != null && mIsAnimation) {
            MLog.i("onPageSelected mPhotoIndex = " + mPhotoIndex);
            mTransitionCompat.setCurrentPosition(mPhotoIndex);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (mTransitionCompat != null && mIsAnimation) {
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
        if(mFirstExecAnimation) {
            findViewById(R.id.rl_photo_bottom).setVisibility(View.GONE);
            tvPhotoIndex.setVisibility(View.GONE);
        } else {
            tvPhotoIndex.setText(String.format(Locale.getDefault(), "%d/%d", mPhotoIndex + 1, mPhotoCount));
        }

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
