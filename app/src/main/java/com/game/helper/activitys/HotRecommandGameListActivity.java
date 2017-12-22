package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.MyAccountAdapter;
import com.game.helper.views.widget.StateView;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;

public class HotRecommandGameListActivity extends XBaseActivity implements View.OnClickListener {

    public static final String TAG = "HotRecommandGameListActivity";

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.hot_game_layout)
    XRecyclerContentLayout xRecyclerContentLayout;

    MyAccountAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntentData(getIntent());
        initView();
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH, 1);
        getGameAccountInfo(1);
    }

    private void getGameAccountInfo(int page) {
//        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page, 1, option_game_id, option_channel_id));
//        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
//            @Override
//            public void accept(HttpResultModel<GameAccountResultModel> recommendResultsHttpResultModel) throws Exception {
//                List<GameAccountResultModel.ListBean> list = new ArrayList<>();
//                list.addAll(recommendResultsHttpResultModel.data.getList());
//                showData(recommendResultsHttpResultModel.current_page, recommendResultsHttpResultModel.total_page, list);
//            }
//        }, new Consumer<NetError>() {
//            @Override
//            public void accept(NetError netError) throws Exception {
//                showError(netError);
//            }
//        });

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
            mAdapter = new MyAccountAdapter(context);
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.getRecyclerView().setRefreshEnabled(false);

        if (errorView == null) {
            errorView = new StateView(context);
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

        xRecyclerContentLayout.loadingView(loadingView);
        xRecyclerContentLayout.showLoading();

    }

    public void showError(NetError error) {
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        xRecyclerContentLayout.showError();
    }

    private void initView() {
        mHeadTittle.setText("主题游戏");
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
}
