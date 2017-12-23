package com.game.helper.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.views.widget.StateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;


public class GameDetailCommunityFragment extends XBaseFragment {
    private static final String TAG = GameDetailCommunityFragment.class.getSimpleName();

    @BindView(R.id.common_recycler_view_layout)
    XRecyclerContentLayout xRecyclerContentLayout;
    private StateView errorView;

    public static GameDetailCommunityFragment newInstance() {
        GameDetailCommunityFragment fragment = new GameDetailCommunityFragment();
        return fragment;
    }

    private void setData() {

    }


    private void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        xRecyclerContentLayout.getRecyclerView().setHasFixedSize(true);
        xRecyclerContentLayout.getRecyclerView().setLayoutManager(manager);
        xRecyclerContentLayout.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        xRecyclerContentLayout.getRecyclerView().setAdapter(new CommunityAdapter());
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH, 1);
                xRecyclerContentLayout.showContent();
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.LOADMORE, page);
                xRecyclerContentLayout.getRecyclerView().setPage(1,1);
                xRecyclerContentLayout.showContent();
            }
        });

        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(xRecyclerContentLayout.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }

        xRecyclerContentLayout.errorView(errorView);
        xRecyclerContentLayout.loadingView(View.inflate(getContext(), R.layout.view_loading, null));

        xRecyclerContentLayout.showLoading();
        errorView.setLoadDataType(StateView.REFRESH, 1);

        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Log.d(TAG,"----------------==================3");
        initList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_game_detail_recycler_layout;
    }

    @Override
    public Object newP() {
        return null;
    }


    public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityHolder> {


        @Override
        public CommunityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.game_communication_item_layout, null);
            CommunityHolder viewHolder = new CommunityHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CommunityHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class CommunityHolder extends RecyclerView.ViewHolder {

            public CommunityHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(errorView != null){
            xRecyclerContentLayout.removeView(errorView);
        }
    }


}
