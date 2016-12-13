package com.facebook.fresco.helper.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by android_ls on 16/6/12.
 */

public class CircleBitmapTransform {

    public static Bitmap transform(Bitmap toTransform) {
        if (toTransform == null) {
            return null;
        }

        int bitmapWidth = toTransform.getWidth();
        int bitmapHeight = toTransform.getHeight();
        Bitmap output = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        RectF outerRect = new RectF(0, 0, bitmapWidth, bitmapHeight);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // paint.setColor()的参数，除不能为Color.TRANSPARENT外，可以任意写
        paint.setColor(Color.WHITE);
        int cornerRadius = Math.min((bitmapWidth / 2), (bitmapHeight / 2));
        canvas.drawRoundRect(outerRect, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Drawable drawable = new BitmapDrawable(toTransform);
        drawable.setBounds(0, 0, bitmapWidth, bitmapHeight);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        drawable.draw(canvas);
        canvas.restore();

        return output;
    }

}
