package com.game.helper.fragments;

import com.game.helper.adapters.HomeItemAdapter;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.functions.Consumer;

/**
 * Created by zr on 2017-10-13.
 */

public class HomePagerFragment extends HomeBasePagerFragment {

    HomeItemAdapter mAdapter;

    @Override
    public SimpleRecAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new HomeItemAdapter(context);
        }

        return mAdapter;
    }

    @Override
    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);
    }

    @Override
    public String getType() {
        return null;
    }


    public static HomePagerFragment newInstance() {
        return new HomePagerFragment();
    }

}
