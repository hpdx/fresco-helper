package com.facebook.fresco.helper.photoview.anim;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by android_ls on 16/9/20.
 */
public class AnimListener implements Animator.AnimatorListener {
    private View mFromView;
    private View mToView;

    public AnimListener(View fromView, View toView) {
        mFromView = fromView;
        mToView = toView;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        mFromView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        mFromView.setVisibility(View.INVISIBLE);
        ((ViewGroup) mFromView.getParent()).removeView(mFromView);

        if (mToView != null) {
            mToView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}