package com.game.helper.fragments.BaseFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.game.helper.R;
import com.game.helper.present.HomeFragmentPresent;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.IModel;
import zlc.season.practicalrecyclerview.AbstractAdapter;
import zlc.season.practicalrecyclerview.PracticalRecyclerView;

/**
 * Created by wanglei on 2016/12/31.
 */

public abstract class HomeBasePagerFragment extends XBaseFragment<HomeFragmentPresent> {

    @BindView(R.id.contentLayout)
    PracticalRecyclerView contentLayout;//content

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getP().loadData();
    }

    private void initAdapter() {
        contentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        contentLayout.setAdapterWithLoading(getAdapter());
        contentLayout.setRefreshListener(new PracticalRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentLayout.setLoadMoreViewEnabled(false);
                contentLayout.setNoMoreViewEnabled(false);
                getP().loadData();
            }
        });
        contentLayout.setLoadMoreListener(new PracticalRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                contentLayout.setLoadMoreViewEnabled(true);
                contentLayout.setNoMoreViewEnabled(true);
                getP().loadMoreData();
            }
        });
    }

    public abstract AbstractAdapter getAdapter();

    public abstract <T extends IModel> AbstractAdapter getAdapter(Class<T> t);

    public abstract <T extends IModel> Void setLayoutManager(Class<T> t, RecyclerView recyclerView);

    public abstract <T extends IModel> String getType(Class<T> t);

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public HomeFragmentPresent newP() {
        return new HomeFragmentPresent();
    }

    public PracticalRecyclerView getContentLayout() {
        return contentLayout;
    }
}
