package com.zhny.zhny_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.zhny.zhny_app.fragments.BaseFragment.XHomeManagesBaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.base.XFragmentAdapter;

public class HomeFragment extends XHomeManagesBaseFragment {

    private List<Fragment> mFragment = new ArrayList<>();
    private XFragmentAdapter mAdapter;


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
