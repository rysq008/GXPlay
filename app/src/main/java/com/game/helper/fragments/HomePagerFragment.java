package com.game.helper.fragments;

import android.content.Intent;

import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.activitys.HotRecommandGameListActivity;
import com.game.helper.adapters.HomeItemAdapter;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.HomeBasePagerFragment;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.utils.SharedPreUtil;

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
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, info == null ? "" : info.icon));
        }
    }

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
                            DetailFragmentsActivity.launch(context, null, GameDetailFragment.newInstance());
                            break;
                        case RxConstant.HomeModeType.Hot_Model_Type:
                            startActivity(new Intent(getActivity(), HotRecommandGameListActivity.class));
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
