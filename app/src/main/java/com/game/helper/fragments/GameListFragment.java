package com.game.helper.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.game.helper.R;
import com.game.helper.adapters.GameItemAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.RecommendResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by zr on 2017-10-13.
 */

public class GameListFragment extends XBaseFragment {

    @BindView(R.id.game_list_layout)
    XRecyclerContentLayout xRecyclerContentLayout;

    GameItemAdapter mAdapter;
    private StateView errorView;
    private View loadingView;
    int classical_type = 0, common_type = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classical_type = getArguments().getInt("classical", 0);
        common_type = getArguments().getInt("common", 0);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH, 1);
        loadGmaeAdapterData(1, classical_type, common_type);
    }

    private void initAdapter() {
        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == mAdapter) {
            mAdapter = new GameItemAdapter(context);
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH, 1);
                loadGmaeAdapterData(1, classical_type, common_type);
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.LOADMORE, page);
                loadGmaeAdapterData(page, classical_type, common_type);
            }
        });

        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(xRecyclerContentLayout.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }
        if (null != errorView.getParent()) {
            ((ViewGroup) errorView.getParent()).removeView(errorView);
        }
        if (loadingView == null) {
            loadingView = View.inflate(context, R.layout.view_loading, null);
        }
        if (null != loadingView.getParent()) {
            ((ViewGroup) loadingView.getParent()).removeView(loadingView);
        }
        xRecyclerContentLayout.errorView(errorView);
        xRecyclerContentLayout.getRecyclerView().useDefLoadMoreView();

        xRecyclerContentLayout.loadingView(loadingView);
        xRecyclerContentLayout.showLoading();

    }

    public void showError(NetError error) {
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        xRecyclerContentLayout.refreshState(false);
        xRecyclerContentLayout.showError();
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            mAdapter.addData(model);
        } else {
            mAdapter.setData(model);
        }
        xRecyclerContentLayout.getRecyclerView().setPage(cur_page, total_page);
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        if (mAdapter.getItemCount() < 1) {
            xRecyclerContentLayout.showEmpty();
            return;
        } else {
            xRecyclerContentLayout.showContent();
            xRecyclerContentLayout.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
            return;
        }
    }

    public void loadGmaeAdapterData(int page, int class_type_id, int type_id) {
        Flowable<HttpResultModel<RecommendResults>> fr = DataService.getHomeRecommend(new RecommendRequestBody(page, class_type_id, type_id));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<RecommendResults>>() {
            @Override
            public void accept(HttpResultModel<RecommendResults> recommendResultsHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(recommendResultsHttpResultModel.data.list);
                showData(recommendResultsHttpResultModel.current_page, recommendResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }

    public static GameListFragment newInstance(int classical_type, int common_type) {
        GameListFragment fragment = new GameListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("classical", classical_type);
        bundle.putInt("common", common_type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_list_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

}
