package com.game.helper.fragments;

import android.os.Bundle;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

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
