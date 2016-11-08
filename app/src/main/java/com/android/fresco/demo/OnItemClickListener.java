package com.android.fresco.demo;

import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/11/7.
 */

public interface OnItemClickListener<T> {

    void onItemClick(ViewGroup parent, ArrayList<T> photos, int position);

}
