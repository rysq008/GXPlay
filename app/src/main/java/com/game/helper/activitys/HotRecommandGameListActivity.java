package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.HotRecommandAdapter;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.HotResults;
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
import io.reactivex.functions.Consumer;
import zlc.season.practicalrecyclerview.ItemType;

public class HotRecommandGameListActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "HotRecommandGameListActivity";

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.hot_game_layout)
    XRecyclerContentLayout xRecyclerContentLayout;

    HotRecommandAdapter mAdapter;
    private StateView errorView;
    private View loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH, 1);
        getHomeHot(1);
    }

    public void getHomeHot(int page) {
        Flowable<HttpResultModel<HotResults>> fr = DataService.getHomeHot(new RecommendRequestBody(page, 0, 0));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<com.game.helper.model.HotResults>>() {
            @Override
            public void accept(HttpResultModel<HotResults> hotResultsHttpResultModel) throws Exception {
                List<ItemType> list = new ArrayList<>();
                list.addAll(hotResultsHttpResultModel.data.list);
                showData(hotResultsHttpResultModel.current_page, hotResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
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

    private void initAdapter() {
        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);

        if (null == mAdapter) {
            mAdapter = new HotRecommandAdapter(context);
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH, 1);
                getHomeHot(1);
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.LOADMORE, page);
                getHomeHot(page);
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

    private void initView() {
        mHeadTittle.setText("热门推荐");
        mHeadBack.setOnClickListener(this);
    }

    private void initIntentData(Intent intent) {
//        option_game_id = intent.getIntExtra(OPTION_GAME_ID, 0);
//        option_channel_id = intent.getIntExtra(OPTION_CHANNEL_ID, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_recommand_game_list;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_bar_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
