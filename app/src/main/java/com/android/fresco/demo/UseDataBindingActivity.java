package com.android.fresco.demo;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.android.fresco.demo.databinding.ActivityFrescoUseDatabindingBinding;

/**
 * Created by android_ls on 16/11/11.
 */

public class UseDataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewDataBinding databindingBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.activity_fresco_use_databinding, null, false);
        setContentView(databindingBinding.getRoot());

        String url = "http://g.hiphotos.baidu.com/imgad/pic/item/c75c10385343fbf22c362d2fb77eca8065388fa0.jpg";
        ((ActivityFrescoUseDatabindingBinding)databindingBinding).setUrl(url);

    }

}
