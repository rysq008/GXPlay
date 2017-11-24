package com.game.helper.fragments.BaseFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.game.helper.R;
import com.game.helper.multipleitem.MultiItemAdapter;
import com.game.helper.multipleitem.MultiItemPresenter;
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
    RecyclerView recyclerView_banner;//banner
    RecyclerView recyclerView_zt;//专题
    RecyclerView recyclerView_rm;//热门
    RecyclerView recyclerView_tj;//推荐

    private MultiItemAdapter mAdapter;
    private MultiItemPresenter mPresenter;


    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getP().loadData();
//        mPresenter.loadData(true);
    }

    private void initAdapter() {
        contentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        contentLayout.setAdapterWithLoading(getAdapter());
        contentLayout.setRefreshListener(new PracticalRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getP().loadData();
            }
        });
        contentLayout.setLoadMoreListener(new PracticalRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getP().loadData();
            }
        });

        contentLayout.setAutoLoadEnable(false);


//        contentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
//        contentLayout.setAdapterWithLoading(getAdapter(BannerResults.class));
//
//        contentLayout.setRefreshListener(new PracticalRecyclerView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.loadData(true);
//            }
//        });
//
//        contentLayout.setLoadMoreListener(new PracticalRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                mPresenter.loadData(false);
//            }
//        });
//
//        mPresenter = new MultiItemPresenter(getContext());
//        mPresenter.setDataLoadCallBack(new MultiItemView() {
//            @Override
//            public void onDataLoadSuccess(final List<ItemType> list, boolean isRefresh) {
//                if (isRefresh) {
//                    contentLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getAdapter(BannerResults.class).clear();
////                            getAdapter(BannerResults.class).addHeader(new Header());
//                            list.clear();
//                            getAdapter(BannerResults.class).addAll(list);
//                        }
//                    }, 1000);
//
//                } else {
//                    getAdapter(BannerResults.class).addAll(list);
//                }
//            }
//
//            @Override
//            public void onDataLoadFailed(boolean isRefresh) {
//                if (isRefresh) {
//                    getAdapter(BannerResults.class).showError();
//                } else {
//                    getAdapter(BannerResults.class).loadMoreFailed();
//                }
//            }
//        });
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
}
