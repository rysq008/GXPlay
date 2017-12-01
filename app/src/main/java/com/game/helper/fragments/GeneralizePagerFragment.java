package com.game.helper.fragments;

import android.os.Bundle;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;

/**
 * Created by zr on 2017-10-13.
 */

public class GeneralizePagerFragment extends XBaseFragment {


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_generalize_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static GeneralizePagerFragment newInstance() {
        return new GeneralizePagerFragment();
    }
}
