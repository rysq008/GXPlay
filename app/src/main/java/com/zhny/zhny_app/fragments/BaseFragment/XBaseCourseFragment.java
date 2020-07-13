package com.zhny.zhny_app.fragments.BaseFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XFragment;

public abstract class XBaseCourseFragment<P extends IPresent> extends XFragment<P> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setColor(getActivity(), Color.parseColor("#059DB3"));
    }
}
