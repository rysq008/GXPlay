package com.game.helper.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.adapters.AvailableRedpackAdapter;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.RedPackEvent;
import com.game.helper.model.AvailableRedpackResultModel;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.net.DataService;
import com.game.helper.net.model.AvailableRedpackRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class ChoiceRedPackActivity extends XBaseActivity implements View.OnClickListener {

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.game_adapter_layout)
    XRecyclerContentLayout xRecyclerContentLayout;

    AvailableRedpackAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    private int option_game_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getIntentData(getIntent());
        initListeners();
        initView();
        errorView.setLoadDataType(StateView.REFRESH, 1);
        fetchAvailableRedpackInfo(1,option_game_id);
    }

    /**
     * 获取可用红包/卡券
     */
    private void fetchAvailableRedpackInfo(int page,int option_game_id) {
        Flowable<HttpResultModel<AvailableRedpackResultModel>> flowable = DataService.getRedPackInfo(new AvailableRedpackRequestBody(page,option_game_id));
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<AvailableRedpackResultModel>>() {
            @Override
            public void accept(HttpResultModel<AvailableRedpackResultModel> generalizeResultsHttpResultModel) throws Exception {
                if(generalizeResultsHttpResultModel.isSucceful()){
                    showData(generalizeResultsHttpResultModel.current_page,generalizeResultsHttpResultModel.total_page,generalizeResultsHttpResultModel.data.getList());
                }

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

    public void showError(NetError error) {
        xRecyclerContentLayout.getLoadingView().setVisibility(View.GONE);
        xRecyclerContentLayout.refreshState(false);
        xRecyclerContentLayout.showError();
    }

    private void initView() {
        mHeadTittle.setText("可使用卡券");

        xRecyclerContentLayout.getRecyclerView().verticalLayoutManager(context);
        if (null == mAdapter) {
            mAdapter = new AvailableRedpackAdapter(context);
        }
        xRecyclerContentLayout.getRecyclerView().setAdapter(mAdapter);
        xRecyclerContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                errorView.setLoadDataType(StateView.REFRESH, 1);
                fetchAvailableRedpackInfo(1,option_game_id);
            }

            @Override
            public void onLoadMore(int page) {
                errorView.setLoadDataType(StateView.LOADMORE, page);
                fetchAvailableRedpackInfo(page,option_game_id);
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

    private void initListeners() {
        mHeadBack.setOnClickListener(this);
    }

    private void getIntentData(Intent intent) {
        option_game_id = intent.getIntExtra(OrderConfirmActivity.OPTION_GAME_ID,0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choice_red_pack;
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
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AvailableRedpackResultModel.ListBean bean = mAdapter.getRecordAccount();
        if(null!=bean){
            BusProvider.getBus().post(new RedPackEvent<>(0, RxConstant.Chooice_RedPack, bean));
        }
        super.onBackPressed();
    }
}