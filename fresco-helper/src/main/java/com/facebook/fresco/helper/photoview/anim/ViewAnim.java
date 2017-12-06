package com.facebook.fresco.helper.photoview.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class ViewAnim {

    private long mAnimTime = 300;
    private TimeInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private AnimListener mAnimListener;

    public void startViewSimpleAnim(final View fromView, Rect finalBounds, int
            startOffsetY, int finalOffsetY, float startAlpha, float finalAlpha) {
        int[] position = new int[2];
        fromView.getLocationOnScreen(position);

        Rect startBounds = new Rect();
        startBounds.left = position[0];
        startBounds.top = position[1];
        startBounds.right = startBounds.left + fromView.getWidth();
        startBounds.bottom = startBounds.top + fromView.getHeight();

        // 设置偏移量
        startBounds.offset(0, -startOffsetY);
        finalBounds.offset(0, -finalOffsetY);

        // 设定拉伸或者旋转动画的中心位置，这里是相对于自身左上角
        fromView.setPivotX(0f);
        fromView.setPivotY(0f);

        // 计算拉伸比例
        float scaleX = (float) finalBounds.width() / startBounds.width();
        float scaleY = (float) finalBounds.height() / startBounds.height();

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(fromView, "alpha", startAlpha, finalAlpha);
        ObjectAnimator xAnim = ObjectAnimator.ofFloat(fromView, "x", startBounds.left, finalBounds.left);
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(fromView, "y", startBounds.top, finalBounds.top);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(fromView, View.SCALE_X, 1f, scaleX);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(fromView, View.SCALE_Y, 1f, scaleY);

        set.play(alphaAnim).with(xAnim).with(yAnim).with(scaleXAnim).with(scaleYAnim);

        set.setDuration(mAnimTime);
        set.setInterpolator(mInterpolator);
        set.addListener(mAnimListener);

        set.start();
    }

    /**
     * 设置动画差值器
     *
     * @param interpolator
     */
    public void setTimeInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public long getDuration() {
        return mAnimTime;
    }

    public void setDuration(long time) {
        mAnimTime = time;
    }

    public void addAnimListener(AnimListener animListener) {
        this.mAnimListener = animListener;
    }
}
