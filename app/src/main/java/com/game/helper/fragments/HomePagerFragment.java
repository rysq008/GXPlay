package com.game.helper.fragments;

import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.adapters.HomeItemAdapter;
import com.game.helper.data.RxConstant;
import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by zr on 2017-10-13.
 */

public class HomePagerFragment extends HomeBasePagerFragment {

    HomeItemAdapter mAdapter;

    @Override
    public SimpleRecAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new HomeItemAdapter(context);
            mAdapter.setRecItemClick(new RecyclerItemCallback<ItemType, HomeItemAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, ItemType model, int tag, HomeItemAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    switch (model.itemType()) {
                        case RxConstant.HomeModeType.Recommend_Model_Type:
                            DetailFragmentsActivity.launch(context,null, GameDetailFragment.newInstance());
                            break;
                    }
                }
            });
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
