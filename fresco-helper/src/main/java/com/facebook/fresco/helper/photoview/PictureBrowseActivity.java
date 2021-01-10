package com.facebook.fresco.helper.photoview;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.facebook.fresco.helper.R;
import com.facebook.fresco.helper.photoview.anim.TransitionCompat;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;
import com.facebook.fresco.helper.photoview.photodraweeview.OnPhotoTapListener;
import com.facebook.fresco.helper.statusbar.StatusBarCompat;
import com.facebook.fresco.helper.utils.DragCloseHelper;
import com.facebook.fresco.helper.utils.MLog;
import com.facebook.fresco.helper.utils.PhotoConstant;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by android_ls on 16/9/13.
 */
public class PictureBrowseActivity extends FragmentActivity
        implements ViewPager.OnPageChangeListener, OnPhotoTapListener, View.OnLongClickListener {

    protected int mPhotoIndex;
    protected int mPhotoCount;

    protected RelativeLayout rlPhotoContainer;
    protected RelativeLayout rlPhotoBottom;
    protected FrameLayout flContainer;
    protected TextView tvPhotoIndex;
    protected MViewPager mViewPager;

    protected PictureBrowseAdapter mAdapter;
    protected ArrayList<PhotoInfo> mItems;

    protected TransitionCompat mTransitionCompat;
    protected DragCloseHelper mDragCloseHelper;

    protected boolean mPhotoOnlyOne;
    protected boolean isAnimation;
    protected boolean isDragClose;
    protected boolean mLongClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        StatusBarCompat.translucentStatusBar(this);

        Intent data = getIntent();
        mItems = data.getParcelableArrayListExtra(PhotoConstant.PHOTO_LIST_KEY);
        if (mItems == null || mItems.size() == 0) {
            MLog.e("photos data is NULL");
            onBackPressed();
            return;
        }

        mPhotoIndex = data.getIntExtra(PhotoConstant.PHOTO_CURRENT_POSITION_KEY, 0);
        isAnimation = data.getBooleanExtra(PhotoConstant.PHOTO_ANIMATION_KEY, false);
        mPhotoOnlyOne = data.getBooleanExtra(PhotoConstant.PHOTO_ONLY_ONE_KEY, false);
        mLongClick = data.getBooleanExtra(PhotoConstant.PHOTO_LONG_CLICK_KEY, true);
        isDragClose = data.getBooleanExtra(PhotoConstant.PHOTO_DRAG_CLOSE, false);
        MLog.i("mPhotoIndex = " + mPhotoIndex);
        MLog.i("mLongClick = " + mLongClick);
        MLog.i("mPhotoOnlyOne = " + mPhotoOnlyOne);
        MLog.i("isAnimation = " + isAnimation);
        MLog.i("isDragClose = " + isDragClose);

        setupViews();

        if (isAnimation) {
            mTransitionCompat = new TransitionCompat(PictureBrowseActivity.this);
            mTransitionCompat.setCurrentPosition(mPhotoIndex);
            mTransitionCompat.startTransition();
        }

        if (isDragClose) {
            setDragClose();
        }
    }

    protected int getLayoutResId() {
        return R.layout.activity_picture_browse;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPhotoOnlyOne) {
            return;
        }

        mPhotoIndex = position;
        setPhotoIndex();

        if (mTransitionCompat != null && isAnimation) {
            MLog.i("onPageSelected mPhotoIndex = " + mPhotoIndex);
            mTransitionCompat.setCurrentPosition(mPhotoIndex);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (mTransitionCompat != null && isAnimation) {
            mTransitionCompat.finishAfterTransition();
        } else {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        onBackPressed();
    }

    protected void setupViews() {
        rlPhotoContainer = findViewById(R.id.rl_photo_container);
        flContainer = findViewById(R.id.fl_container);
        rlPhotoBottom = findViewById(R.id.rl_photo_bottom);
        tvPhotoIndex = (TextView) findViewById(R.id.tv_photo_count);
        mViewPager = (MViewPager) findViewById(R.id.vp_picture_browse);
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(this);

        mAdapter = new PictureBrowseAdapter(this, mItems, this, mLongClick ? this : null);
        mViewPager.setAdapter(mAdapter);

        mPhotoCount = mItems.size();
        setupBottomViews();
        mViewPager.setCurrentItem(mPhotoIndex);
    }

    protected void setupBottomViews() {
        if (mPhotoOnlyOne) {
            if (rlPhotoBottom != null) {
                rlPhotoBottom.setVisibility(View.GONE);
                tvPhotoIndex.setVisibility(View.GONE);
            }
        } else {
            setPhotoIndex();
        }
    }

    protected void setPhotoIndex() {
        if (tvPhotoIndex != null) {
            tvPhotoIndex.setText(String.format(Locale.getDefault(), "%d/%d", mPhotoIndex + 1, mPhotoCount));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        MLog.i("onLongClick");
        return false;
    }

    public ArrayList<PhotoInfo> getItems() {
        return mItems;
    }

    public PhotoInfo getItem(int position) {
        if (mItems != null && mItems.size() > 0) {
            return mItems.get(position);
        }
        return null;
    }

    /**
     * 获取当前PhotoInfo在集合中Position
     *
     * @return
     */
    public int getCurrentPosition() {
        return mPhotoIndex;
    }

    public PhotoInfo getCurrentPhotoInfo() {
        if (mItems != null && mItems.size() > 0) {
            return mItems.get(mPhotoIndex);
        }
        return null;
    }

    /**
     * 向下拖动关闭
     */
    protected void setDragClose() {
        mDragCloseHelper = new DragCloseHelper(this);
        mDragCloseHelper.setShareElementMode(true);
        mDragCloseHelper.setMinScale(0.2f);
        mDragCloseHelper.setMaxExitY(200);
        mDragCloseHelper.setDragCloseViews(rlPhotoContainer, flContainer);
        mDragCloseHelper.setDragCloseListener(new DragCloseHelper.OnDragCloseListener() {
            @Override
            public boolean intercept() {
                return false;
            }

            @Override
            public void onDragStart() {
                if (rlPhotoBottom != null) {
                    rlPhotoBottom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDragging(float percent) {

            }

            @Override
            public void onDragCancel() {
                if (rlPhotoBottom != null) {
                    rlPhotoBottom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDragClose(boolean isShareElementMode) {
                if (isShareElementMode) {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDragCloseHelper != null && mDragCloseHelper.handleEvent(ev)) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    protected void onDestroy() {
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
