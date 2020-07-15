package com.zhny.zhny_app.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaeger.library.StatusBarUtil;
import com.zhny.zhny_app.R;
import com.zhny.zhny_app.fragments.BaseFragment.XBaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.base.XFragmentAdapter;

public class HomeFragment extends XBaseFragment {

    private List<Fragment> mFragment = new ArrayList<>();
    private XFragmentAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatusBarUtil.setTranslucent(context,0);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


}
