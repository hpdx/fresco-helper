package com.facebook.fresco.helper.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import androidx.annotation.FloatRange;

public class DragCloseHelper {

    /**
     * 动画执行时间
     */
    private final static long DURATION = 100;

    /**
     * 滑动边界距离
     */
    private final static int MAX_EXIT_Y = 500;

    /**
     * 最小的缩放尺寸
     */
    private static final float MIN_SCALE = 0.4F;

    private ViewConfiguration viewConfiguration;

    private int maxExitY = MAX_EXIT_Y;
    private float minScale = MIN_SCALE;

    /**
     * 是否在滑动关闭中，手指还在触摸中
     */
    private boolean isSwipingToClose;

    /**
     * 上次触摸坐标
     */
    private float mLastY, mLastRawY, mLastX, mLastRawX;

    /**
     * 上次触摸手指id
     */
    private int lastPointerId;

    /**
     * 当前位移距离
     */
    private float mCurrentTranslationY, mCurrentTranslationX;

    /**
     * 上次位移距离
     */
    private float mLastTranslationY, mLastTranslationX;

    /**
     * 正在恢复原位中
     */
    private boolean isRestAnimate = false;

    /**
     * 共享元素模式
     */
    private boolean isShareElementMode = false;

    private View parentV, childV;

    private OnDragCloseListener dragCloseListener;
    private Context mContext;

    public DragCloseHelper(Context mContext) {
        this.mContext = mContext;
        viewConfiguration = ViewConfiguration.get(mContext);
    }

    public void setDragCloseListener(OnDragCloseListener dragCloseListener) {
        this.dragCloseListener = dragCloseListener;
    }

    /**
     * 设置共享元素模式
     *
     * @param shareElementMode
     */
    public void setShareElementMode(boolean shareElementMode) {
        isShareElementMode = shareElementMode;
    }

    /**
     * 设置拖拽关闭的view
     *
     * @param parentV
     * @param childV
     */
    public void setDragCloseViews(View parentV, View childV) {
        this.parentV = parentV;
        this.childV = childV;
    }

    /**
     * 设置最大退出距离
     *
     * @param maxExitY
     */
    public void setMaxExitY(int maxExitY) {
        this.maxExitY = maxExitY;
    }

    /**
     * 设置最小缩放尺寸
     *
     * @param minScale
     */
    public void setMinScale(@FloatRange(from = 0.1f, to = 1.0f) float minScale) {
        this.minScale = minScale;
    }

    /**
     * 处理touch事件
     *
     * @param event
     * @return
     */
    public boolean handleEvent(MotionEvent event) {
        if (dragCloseListener != null && dragCloseListener.intercept()) {
            isSwipingToClose = false;
            return false;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                lastPointerId = event.getPointerId(0);
                reset(event);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (event.getPointerCount() > 1) {
                    if (isSwipingToClose) {
                        isSwipingToClose = false;
                        resetCallBackAnimation();
                        return true;
                    }
                    reset(event);
                    return false;
                }

                if (lastPointerId != event.getPointerId(0)) {
                    if (isSwipingToClose) {
                        resetCallBackAnimation();
                    }
                    reset(event);
                    return true;
                }

                float currentY = event.getY();
                float currentX = event.getX();
                if (isSwipingToClose || (Math.abs(currentY - mLastY) > 2 * viewConfiguration.getScaledTouchSlop()
                        && Math.abs(currentY - mLastY) > Math.abs(currentX - mLastX) * 1.5)) {
                    mLastY = currentY;
                    mLastX = currentX;
                    float currentRawY = event.getRawY();
                    float currentRawX = event.getRawX();
                    if (!isSwipingToClose) {
                        isSwipingToClose = true;
                        if (dragCloseListener != null) {
                            dragCloseListener.onDragStart();
                        }
                    }

                    mCurrentTranslationY = currentRawY - mLastRawY + mLastTranslationY;
                    mCurrentTranslationX = currentRawX - mLastRawX + mLastTranslationX;
                    float percent = 1 - Math.abs(mCurrentTranslationY / (maxExitY + childV.getHeight()));
                    if (percent > 1) {
                        percent = 1;
                    } else if (percent < 0) {
                        percent = 0;
                    }
                    parentV.getBackground().mutate().setAlpha((int) (percent * 255));
                    if (dragCloseListener != null) {
                        dragCloseListener.onDragging(percent);
                    }
                    childV.setTranslationY(mCurrentTranslationY);
                    childV.setTranslationX(mCurrentTranslationX);
                    if (percent < minScale) {
                        percent = minScale;
                    }
                    childV.setScaleX(percent);
                    childV.setScaleY(percent);
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isSwipingToClose) {
                    if (mCurrentTranslationY > maxExitY) {
                        if (isShareElementMode) {
                            if (dragCloseListener != null) {
                                dragCloseListener.onDragClose(true);
                            }
                        } else {
                            exitWithTranslation(mCurrentTranslationY);
                        }
                    } else {
                        resetCallBackAnimation();
                    }
                    isSwipingToClose = false;
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (isSwipingToClose) {
                    resetCallBackAnimation();
                    isSwipingToClose = false;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出动画
     *
     * @param currentY
     */
    public void exitWithTranslation(float currentY) {
        int targetValue = currentY > 0 ? childV.getHeight() : -childV.getHeight();
        ValueAnimator anim = ValueAnimator.ofFloat(mCurrentTranslationY, targetValue);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateChildView(mCurrentTranslationX, (float) animation.getAnimatedValue());
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (dragCloseListener != null) {
                    dragCloseListener.onDragClose(false);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(DURATION);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    /**
     * 重置数据
     *
     * @param event
     */
    private void reset(MotionEvent event) {
        isSwipingToClose = false;
        mLastY = event.getY();
        mLastX = event.getX();
        mLastRawY = event.getRawY();
        mLastRawX = event.getRawX();
        mLastTranslationY = 0;
        mLastTranslationX = 0;
    }

    /**
     * 更新缩放的view
     */
    private void updateChildView(float transX, float transY) {
        childV.setTranslationY(transY);
        childV.setTranslationX(transX);
        float percent = Math.abs(transY / (maxExitY + childV.getHeight()));
        float scale = 1 - percent;
        if (scale < minScale) {
            scale = minScale;
        }
        childV.setScaleX(scale);
        childV.setScaleY(scale);
    }

    /**
     * 恢复到原位动画
     */
    private void resetCallBackAnimation() {
        if (isRestAnimate || mCurrentTranslationY == 0) {
            return;
        }

        final float ratio = mCurrentTranslationX / mCurrentTranslationY;
        ValueAnimator animatorY = ValueAnimator.ofFloat(mCurrentTranslationY, 0);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isRestAnimate) {
                    mCurrentTranslationY = (float) valueAnimator.getAnimatedValue();
                    mCurrentTranslationX = ratio * mCurrentTranslationY;
                    mLastTranslationY = mCurrentTranslationY;
                    mLastTranslationX = mCurrentTranslationX;
                    updateChildView(mLastTranslationX, mCurrentTranslationY);
                }
            }
        });
        animatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRestAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isRestAnimate) {
                    parentV.getBackground().mutate().setAlpha(255);
                    mCurrentTranslationY = 0;
                    mCurrentTranslationX = 0;
                    isRestAnimate = false;
                    if (dragCloseListener != null) {
                        dragCloseListener.onDragCancel();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorY.setDuration(DURATION).start();
    }

    public interface OnDragCloseListener {
        boolean intercept();
        void onDragStart();
        void onDragging(float percent);
        void onDragCancel();
        void onDragClose(boolean isShareElementMode);
    }

}

