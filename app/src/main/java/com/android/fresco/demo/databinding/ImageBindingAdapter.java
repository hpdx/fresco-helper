package com.android.fresco.demo.databinding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;
import com.facebook.fresco.helper.listener.IResult;
import com.facebook.fresco.helper.utils.DensityUtil;

/**
 * Created by android_ls on 16/11/11.
 */
public class ImageBindingAdapter {

    @BindingAdapter({"url"})
    public static void loadImage(SimpleDraweeView simpleDraweeView, String url) {
        ImageLoader.loadImage(simpleDraweeView, url);
    }

    @BindingAdapter({"url_small"})
    public static void loadImageSmall(SimpleDraweeView simpleDraweeView, String url) {
        ImageLoader.loadImageSmall(simpleDraweeView, url);
    }

    @BindingAdapter({"url", "iconWidth", "iconHeight"})
    public static void loadTextDrawable(final TextView view, String url, final int iconWidth, final int iconHeight) {
        ImageLoader.loadImage(view.getContext(), url, new IResult<Bitmap>() {
            @Override
            public void onResult(Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(view.getContext().getResources(), bitmap);
                final int width = DensityUtil.dipToPixels(view.getContext(), iconWidth);
                final int height = DensityUtil.dipToPixels(view.getContext(), iconHeight);
                drawable.setBounds(0, 0, width, height);
                view.setCompoundDrawables(drawable, null, null, null);
            }
        });
    }

    @BindingAdapter({"url", "direction", "iconWidth", "iconHeight"})
    public static void loadTextDrawable(final TextView view, String url, final int direction, final int iconWidth, final int iconHeight) {
        ImageLoader.loadImage(view.getContext(), url, new IResult<Bitmap>() {
            @Override
            public void onResult(Bitmap bitmap) {
                Drawable drawable = new BitmapDrawable(view.getContext().getResources(), bitmap);
                final int width = DensityUtil.dipToPixels(view.getContext(), iconWidth);
                final int height = DensityUtil.dipToPixels(view.getContext(), iconHeight);
                drawable.setBounds(0, 0, width, height);
                switch (direction) {
                    case 0:
                        view.setCompoundDrawables(drawable, null, null, null);
                        break;
                    case 1:
                        view.setCompoundDrawables(null, drawable, null, null);
                        break;
                    case 2:
                        view.setCompoundDrawables(null, null, drawable, null);
                        break;
                    case 3:
                        view.setCompoundDrawables(null, null, null, drawable);
                        break;
                }
            }
        });
    }

}
