package com.facebook.fresco.helper.photoview.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

/**
 * Created by android_ls on 2017/5/16.
 */

public class ProgressBarDrawable extends Drawable {

    // init Max value is 10000
    protected long mMaxValue = 10000;
    // The Progress Value
    protected long mProgress;
    // The size of text
    private int mTextSize;
    // The color of text
    private int mTextColor;
    // the X offset of text
    private int mTextXOffset;
    private int mTextYOffset;
    //The text typeface
    private Typeface mTypeface;
    // THe visiable of text
    private boolean mTextShow;
    // The paint of text
    private Paint mTextPaint;
    // target ImageVIew
    //Custom String
    private String mCustomStr;

    // The Paint of the Ring
    private Paint mCirclePaint;
    // The Size of the Ring
    private float mCircleWidth;
    // The progress Color for the Ring
    private int mCircleProgressColor;
    // The bottom color for the Ring
    private int mCircleBottomColor;
    //THe bottom circle width
    private int mCircleBottomWidth;
    // the radius of the Ring
    private int mCircleRadius;
    // Padding of line padding int the fan style
    private int mFanPadding;
    // The Style of the Circle
    private CircleStyle mCircleStyle = CircleStyle.FAN;
    // The linear color
    private int[] mGradientColor;
    //The Gradient Type
    private GradientType mGradientType = GradientType.LINEAR;

    /**
     * Creates a new layer drawable with the list of specified layers.
     */
    public ProgressBarDrawable() {
        initProperty();
    }

    /**
     * Creates a new fade drawable. The first layer is displayed with full
     * opacity whereas all other layers are invisible.
     */


    @Override
    public void draw(@NonNull Canvas canvas) {
        // if progress = 0 ,not show
        if (mProgress == 0 || (int) ((double) ((double) mProgress / (double) mMaxValue) * 100) == 100) {
            return;
        }
        DrawOther(canvas);
        if (mTextShow) {
            DrawText(canvas);
        }
    }

    /**
     * draw text on the center of the canvas
     *
     * @param canvas 画布
     */
    public void DrawText(Canvas canvas) {
        Rect size = getBounds();
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = size.top + (size.bottom - size.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mCustomStr == null ? (int) ((double) ((double) mProgress / (double) mMaxValue) * 100) + "%" : mCustomStr, size.centerX() + mTextXOffset, baseline + mTextYOffset, mTextPaint);
    }

    /**
     * The method will add this progress to the imageview And it will in the top
     * of the imageview
     */
//    public void AddToImageView(boolean haveDrawable) {
//        List<Drawable> layer_arrays = new ArrayList<>();
//        if (haveDrawable) {
//            Drawable drawable = mTarget.getDrawable();
//            layer_arrays.add(drawable);
//        }
//        layer_arrays.add(this);
//        LayerDrawable levelDrawable = new LayerDrawable(layer_arrays.toArray(new Drawable[layer_arrays.size()]));
//        mTarget.setImageDrawable(levelDrawable);
//    }

    @Override
    protected boolean onLevelChange(int level) {
//        if (mTarget != null) {
//            if (mTarget.getDrawable() == null) {
//                AddToImageView(false);
//            } else if (mProgress < mMaxValue) {
//                if (!mTarget.getDrawable().getClass().equals(LayerDrawable.class))
//                    AddToImageView(true);
//            }
//        }
        long origin = mProgress;
        mProgress = level;
        if (mProgress != 0 && origin != mProgress) {
            invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

//    /*
//     * Clear progress
//     * bacause listview cache
//    */
//    private void ClearProgress() {
//        if (mTarget == null || mTarget.getDrawable() == null || !mTarget.getDrawable().getClass().equals(LayerDrawable.class))
//            return;
//        LayerDrawable levelDrawable = (LayerDrawable) mTarget.getDrawable();
//        if (levelDrawable.getNumberOfLayers() == 2) {
//            if (levelDrawable.getDrawable(1).getClass().isAssignableFrom(ProgressBarDrawable.class) && !levelDrawable.getDrawable(1).equals(this)) {
//                //let the progress not show
//                ((ProgressBarDrawable) levelDrawable.getDrawable(1)).setLevel(0);
//                // let the origin progress's target imageview point to null
//                ((ProgressBarDrawable) levelDrawable.getDrawable(1)).inject(null);
//                //reinject new target
//                mTarget.setImageDrawable(levelDrawable.getDrawable(0));
//            }
//        }
//    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        mTextPaint.setTextSize(mTextSize);
    }

    public void setTextColor(int mTextColor) {
        mTextPaint.setColor(mTextColor);
        this.mTextColor = mTextColor;
    }

    public void setTextColorRes(@ColorRes int ColorRes, Context context) {
        mTextPaint.setColor(context.getResources().getColor(ColorRes));
    }

    public void setTextShow(boolean mTextShow) {
        this.mTextShow = mTextShow;
    }

    public void setTypeface(Typeface mTypeface) {
        this.mTypeface = mTypeface;
        mTextPaint.setTypeface(mTypeface);
    }

    public void setCustomText(String text) {
        mCustomStr = text;
    }

    public void setTextXOffset(int offset) {
        mTextXOffset = offset;
    }

    public void setTextYOffset(int offset) {
        mTextYOffset = offset;
    }

    public void setMaxValue(long value) {
        this.mMaxValue = value;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public Paint getmTextPaint() {
        return mTextPaint;
    }

    public void initProperty() {
        mTextPaint = new Paint();
        mTextSize = 20;
        mTextColor = Color.WHITE;
        mTextShow = true;
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mCircleProgressColor = 0xaa59c8cc;
        mCircleBottomColor = 0xdddddddd;
        mCircleWidth = 8;
        mCircleRadius = 100;
        mFanPadding = 5;
        mCircleBottomWidth = 2;
    }

    public void DrawOther(Canvas canvas) {
        drawCircle(canvas);
        drawArc(canvas);
    }

    /*
     * draw the circle background
     */
    private void drawCircle(Canvas canvas) {

        Rect bounds = getBounds();
        int xpos = bounds.left + bounds.width() / 2;
        int ypos = bounds.bottom - bounds.height() / 2;

        mCirclePaint.setColor(mCircleBottomColor);
        mCirclePaint.setStrokeWidth(mCircleStyle == CircleStyle.RING ? mCircleWidth : mCircleBottomWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setShader(null);

        canvas.drawCircle(xpos, ypos, mCircleStyle == CircleStyle.RING ? mCircleRadius : mCircleRadius + mFanPadding,
                mCirclePaint);

    }

    /*
     * draw the arc for the progress
     */
    private void drawArc(Canvas canvas) {
        mCirclePaint.setStyle(mCircleStyle == CircleStyle.RING ? Paint.Style.STROKE : Paint.Style.FILL);
        Rect bounds = getBounds();
        int xpos = bounds.left + bounds.width() / 2;
        int ypos = bounds.bottom - bounds.height() / 2;
        RectF rectF = new RectF(xpos - mCircleRadius, ypos - mCircleRadius, xpos + mCircleRadius, ypos + mCircleRadius);
        float degree = (float) mProgress / (float) mMaxValue * 360;

        mCirclePaint.setStrokeWidth(mCircleWidth);
        if (mGradientColor != null) {
            if (mGradientType == GradientType.LINEAR)
                mCirclePaint.setShader(new LinearGradient(bounds.centerX(), bounds.centerY() - mCircleRadius,
                        bounds.centerX(), bounds.centerY() + mCircleRadius, mGradientColor, null, Shader.TileMode.MIRROR));
            else {
                mCirclePaint.setShader(new SweepGradient(bounds.centerX(), bounds.centerY(), mGradientColor, null));
            }

            mCirclePaint.setColor(0xffffffff);
        } else {
            mCirclePaint.setColor(mCircleProgressColor);
        }

        canvas.drawArc(rectF, 270, degree, mCircleStyle == CircleStyle.FAN, mCirclePaint);
    }

    public enum CircleStyle {
        RING, FAN
    }

    public enum GradientType {
        LINEAR, SWEEP;
    }

}
