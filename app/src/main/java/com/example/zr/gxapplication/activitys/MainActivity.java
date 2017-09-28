package com.example.zr.gxapplication.activitys;

import android.os.Bundle;

import com.example.zr.gxapplication.R;

import cn.droidlover.xdroidmvp.mvp.XActivity;

public class MainActivity extends XActivity {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }
}
