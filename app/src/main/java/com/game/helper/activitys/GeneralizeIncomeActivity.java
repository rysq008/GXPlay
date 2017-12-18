package com.game.helper.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.IncomeListAdapter;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GeneralizeAccountInfoResultModel;
import com.game.helper.model.IncomeResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.FriendRangeRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * 推广收益页面
 */
public class GeneralizeIncomeActivity extends XBaseActivity implements View.OnClickListener {

    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.incomeTv)
    TextView incomeTv;
    @BindView(R.id.canWithdrawals)
    TextView canWithdrawals;
    @BindView(R.id.waittingWithdrawals)
    TextView waittingWithdrawals;
    @BindView(R.id.game_adapter_layout)
    XRecyclerContentLayout gameAdapterLayout;

    IncomeListAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mHeadTittle.setText("累计收益");
        mHeadBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.action_bar_back:
                onBackPressed();
            break;
        default:
            break;
}
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        errorView.setLoadDataType(StateView.REFRESH, 1);
        loadIncomeListData(1);
        loadGeneralizeAccountInfo();
    }

    private void loadGeneralizeAccountInfo() {
        Flowable<HttpResultModel<GeneralizeAccountInfoResultModel>> fr = DataService.getGeneralizeAccountInfo();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GeneralizeAccountInfoResultModel>>() {
            @Override
            public void accept(HttpResultModel<GeneralizeAccountInfoResultModel> recommendResultsHttpResultModel) throws Exception {
                //累计收益
                incomeTv.setText(recommendResultsHttpResultModel.data.getZongshouyi());
                //可提现
                canWithdrawals.setText(recommendResultsHttpResultModel.data.getYue());
                //预计收益
                waittingWithdrawals.setText(recommendResultsHttpResultModel.data.getYujizongshouyi());
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });

    }

    private void initAdapter() {
        gameAdapterLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == mAdapter) {
            mAdapter = new IncomeListAdapter(context);
        }
        gameAdapterLayout.getRecyclerView().setAdapter(mAdapter);
        gameAdapterLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH, 1);
                loadIncomeListData(1);
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.LOADMORE, page);
                loadIncomeListData(page);
            }
        });

        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(gameAdapterLayout.getRecyclerView().getOnRefreshAndLoadMoreListener());
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
        gameAdapterLayout.errorView(errorView);
        gameAdapterLayout.getRecyclerView().useDefLoadMoreView();

        gameAdapterLayout.loadingView(loadingView);
        gameAdapterLayout.showLoading();

    }

    public void showError(NetError error) {
        gameAdapterLayout.getLoadingView().setVisibility(View.GONE);
        gameAdapterLayout.refreshState(false);
        gameAdapterLayout.showError();
    }

    public void showData(int cur_page, int total_page, List model) {
        if (cur_page > 1) {
            mAdapter.addData(model);
        } else {
            mAdapter.setData(model);
        }
        gameAdapterLayout.getRecyclerView().setPage(cur_page, total_page);
        gameAdapterLayout.getLoadingView().setVisibility(View.GONE);
        if (mAdapter.getItemCount() < 1) {
            gameAdapterLayout.showEmpty();
            return;
        } else {
            gameAdapterLayout.showContent();
            gameAdapterLayout.getSwipeRefreshLayout().setVisibility(View.VISIBLE);
            return;
        }
    }

    public void loadIncomeListData(int page) {
        Flowable<HttpResultModel<IncomeResultModel>> fr = DataService.getIncomeList(new FriendRangeRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<IncomeResultModel>>() {
            @Override
            public void accept(HttpResultModel<IncomeResultModel> recommendResultsHttpResultModel) throws Exception {
                List<IncomeResultModel.ListBean> list = new ArrayList<>();
                list.addAll(recommendResultsHttpResultModel.data.getList());
                showData(recommendResultsHttpResultModel.current_page, recommendResultsHttpResultModel.total_page, list);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_generalize_income;
    }

    @Override
    public Object newP() {
        return null;
    }
}
