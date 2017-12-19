package com.facebook.fresco.helper.photoview.anim;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class ViewOptionsCompat {

    public static final String KEY_VIEW_OPTION_LIST = "view_option_list";

    public static Bundle makeScaleUpAnimation(GridLayoutManager layoutManager, ArrayList<String> thumbnailList) {
        SparseArray<ViewOptions> sparseArray = new SparseArray<>();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        for (int i = 0; i < thumbnailList.size(); i++) {
            if (i >= firstVisibleItemPosition && i <= lastVisibleItemPosition) {
                View childView = layoutManager.findViewByPosition(i);
                if (childView != null) {
                    sparseArray.put(i, createViewOptions(childView, thumbnailList.get(i)));
                }
            }
        }

        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(KEY_VIEW_OPTION_LIST, sparseArray);
        return bundle;
    }

    public static Bundle makeScaleUpAnimation(View thumbnailView, String thumbnail) {
        SparseArray<ViewOptions> sparseArray = new SparseArray<>();
        sparseArray.put(0, createViewOptions(thumbnailView, thumbnail));

        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(KEY_VIEW_OPTION_LIST, sparseArray);
        return bundle;
    }

    private static ViewOptions createViewOptions(View thumbnailView, String thumbnail) {
        ViewOptions viewOptions = new ViewOptions();
        viewOptions.thumbnail = thumbnail;

        // 判断当前activity是否是全屏模式
        viewOptions.isFullScreen = isFullScreen((Activity) thumbnailView.getContext());
        // 判断当前是否是竖屏
        viewOptions.isVerticalScreen = isVerticalScreen((Activity) thumbnailView.getContext());
        // 判断view是否在屏幕上，如果在就执行动画，否则不执行动画
        viewOptions.isInTheScreen = isInScreen(thumbnailView);

        int[] location = new int[2];
        // ps = position，目的得到当前view相对于屏幕的坐标
        thumbnailView.getLocationOnScreen(location);
        // 设置起始坐标和起始宽高
        viewOptions.startX = location[0];
        viewOptions.startY = location[1];

        viewOptions.width = thumbnailView.getMeasuredWidth();
        viewOptions.height = thumbnailView.getMeasuredHeight();
        return viewOptions;
    }

    /**
     * view当前是否显示在屏幕上，包括被遮挡，显示不全的状态
     *
     * @return
     */
    public static boolean isInScreen(View view) {
        Rect bounds = new Rect();
        // 只要有一部分显示在屏幕内，就是true，不考虑遮挡情况
        boolean isInScreen = view.getGlobalVisibleRect(bounds);
        if (isInScreen) {
            if (bounds.width() < view.getWidth() * 0.3f || bounds.height() < view.getHeight() * 0.3f) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * 判断当前屏幕是否是横屏
     *
     * @param activity
     * @return
     */
    public static boolean isVerticalScreen(Activity activity) {
        int flag = activity.getResources().getConfiguration().orientation;
        return !(flag == 0);
    }

    /**
     * @param activity
     * @return 判断当前手机是否是全屏
     */
    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        } else {
            return false;
        }
    }

}
