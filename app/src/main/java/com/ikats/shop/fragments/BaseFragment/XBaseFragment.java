package com.ikats.shop.fragments.BaseFragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ikats.shop.R;
import com.jaeger.library.StatusBarUtil;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XFragment;

public abstract class XBaseFragment<P extends IPresent> extends XFragment<P> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public P newP() {
        return null;
    }

    public boolean onBackPress(Activity activity) {
        return false;
    }
}
