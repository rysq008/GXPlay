package com.game.helper.fragments;

import android.os.Bundle;

import com.game.helper.R;
import com.game.helper.adapters.HomeItemAdapter;
import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by zr on 2017-10-13.
 */

public class MinePagerFragment extends XBaseFragment {


    public static MinePagerFragment newInstance() {
        return new MinePagerFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    public Object newP() {
        return null;
    }
}
