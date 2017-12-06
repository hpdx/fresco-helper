package com.android.fresco.demo.photo;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/11/7.
 */

public interface OnItemClickListener<T> {

    void onItemClick(View view, ArrayList<T> photos, int position);

}
