package com.facebook.fresco.helper.photoview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;

public class SceneScaleUpAnimator extends TransitionAnimator {

    private float mStartX;
    private float mStartY;
    private float mHeight;
    private float mWidth;

    public SceneScaleUpAnimator(Activity activity, ViewOptions viewOptions) {
        super(activity);
        mStartX = viewOptions.startX;
        mStartY = viewOptions.startY;
        mWidth = viewOptions.width;
        mHeight = viewOptions.height;
    }

    public void playScreenAnim(final boolean isEnter) {
        View sceneRoot = getSceneRoot();

        float fromAlpha, toAlpha;
        float fromX, toX;
        float fromY, toY;
        float fromScaleX, toScaleX;
        float fromScaleY, toScaleY;

        if (isEnter) {
            fromAlpha = 1f;
            toAlpha = 1f;

            fromX = mStartX;
            toX = 0f;
            fromY = mStartY;
            toY = 0f;

            fromScaleX = (float) mWidth / getSceneRoot().getWidth();
            toScaleX = 1f;
            fromScaleY = (float) mHeight / getSceneRoot().getHeight();
            toScaleY = 1f;
        } else {
            fromAlpha = 1f;
            toAlpha = 1f;

            fromX = 0f;
            toX = mStartX;
            fromY = 0f;
            toY = mStartY;

            fromScaleX = 1f;
            toScaleX = (float) mWidth / getSceneRoot().getWidth();
            fromScaleY = 1f;
            toScaleY = (float) mHeight / getSceneRoot().getHeight();
        }

        // 定义移动的起始位置，如果不定义就是相对于自身中心
        sceneRoot.setPivotX(0f);
        sceneRoot.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(sceneRoot, "alpha", fromAlpha, toAlpha);
        ObjectAnimator xAnim = ObjectAnimator.ofFloat(sceneRoot, "x", fromX, toX);
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(sceneRoot, "y", fromY, toY);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(sceneRoot, "scaleX", fromScaleX, toScaleX);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(sceneRoot, "scaleY", fromScaleY, toScaleY);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isEnter) {
                    enterAnimsEnd();
                } else {
                    exitAnimsEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        set.play(alphaAnim).with(xAnim).with(yAnim).with(scaleXAnim).with(scaleYAnim);
        set.setDuration(getAnimDuration());
        set.setInterpolator(getAnimInterpolator());
        set.start();
    }

}
