package com.facebook.fresco.helper.photoview.anim;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.facebook.fresco.helper.R;

public abstract class TransitionAnimator {

    protected Activity mActivity;
    protected View mSceneRoot;
    protected Drawable mBackground;

    private long mDuration = 300;
    private TimeInterpolator mTimeInterpolator = new AccelerateDecelerateInterpolator();

    public TransitionAnimator(Activity activity) {
        mActivity = activity;
        mBackground = ContextCompat.getDrawable(activity, R.drawable.transparent);
        mSceneRoot = ((ViewGroup) getActivity().getWindow().getDecorView()).getChildAt(0);
        mActivity.getWindow().setBackgroundDrawable(mBackground);
    }

    /**
     * 进入动画结束时应该有的操作
     */
    protected void enterAnimsEnd() {
        mSceneRoot.setAlpha(1.0f);
        mBackground.setAlpha(255);
    }

    /**
     * 退出动画结束时应该有的操作
     */
    protected void exitAnimsEnd() {
        mSceneRoot.setAlpha(0);
        mBackground.setAlpha(0);
        mActivity.finish();
        mActivity.overridePendingTransition(0, 0);
    }

    /**
     * @return 当前的activity
     */
    public Activity getActivity() {
        return mActivity;
    }

    /**
     * @return 要进行动画的view
     */
    public View getSceneRoot() {
        return mSceneRoot;
    }

    /**
     * @return 当前activity的背景图
     */
    public Drawable getBackground() {
        return mBackground;
    }

    public TimeInterpolator getAnimInterpolator() {
        return mTimeInterpolator;
    }

    public void setAnimInterpolator(TimeInterpolator interpolator) {
        mTimeInterpolator = interpolator;
    }

    public long getAnimDuration() {
        return mDuration;
    }

    public void setAnimDuration(long duration) {
        mDuration = duration;
    }

}
