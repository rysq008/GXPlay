package com.game.helper.fragments.wallet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.adapters.RechargeCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.CashListResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.RechargeListResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletListFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = WalletListFragment.class.getSimpleName();
    private int Show_List_Type = RechargeCommonAdapter.Type_Account_Consume;

    @BindView(R.id.rc_wallet_list)
    XRecyclerContentLayout mContent;

    private RechargeCommonAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    public static WalletListFragment newInstance(int type){
        return new WalletListFragment(type);
    }
    public WalletListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public WalletListFragment(int type) {
        Show_List_Type = type;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        errorView.setLoadDataType(StateView.REFRESH,1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_list;
    }

    private void initView(){
        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(mContent.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }
        if (null != errorView.getParent()) ((ViewGroup) errorView.getParent()).removeView(errorView);
        if (loadingView == null) loadingView = View.inflate(context, R.layout.view_loading, null);
        if (null != loadingView.getParent()) ((ViewGroup) loadingView.getParent()).removeView(loadingView);
        mContent.errorView(errorView);
        mContent.loadingView(loadingView);
        mContent.showLoading();
        initList();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromNet(1);
    }

    /**
     * 获取数据
     * */
    private void getDataFromNet(int page){
        if (Show_List_Type == RechargeCommonAdapter.Type_Account_Consume) getConsumeListData(page);
        if (Show_List_Type == RechargeCommonAdapter.Type_Account_Recharge) getRechargeListData(page);
        if (Show_List_Type == RechargeCommonAdapter.Type_Account_Cash) getCashListData(page);
        if (Show_List_Type == RechargeCommonAdapter.Type_Account_Profit) getProfitListData(page);
    }

    private void initList(){
        mAdapter = null;
        mAdapter = new RechargeCommonAdapter(getContext(), null, Show_List_Type);
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.getRecyclerView().setHasFixedSize(true);
        mContent.getRecyclerView().verticalLayoutManager(context);
        mContent.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mContent.getRecyclerView().setAdapter(mAdapter);
        mContent.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(1);
            }

            @Override
            public void onLoadMore(int page) {
                getDataFromNet(page);
            }
        });
        mContent.getRecyclerView().useDefLoadMoreView();
    }

    /**
     * 获取消费明细
     * */
    private void getConsumeListData(final int page) {
        Flowable<HttpResultModel<ConsumeListResults>> fr = DataService.getConsumeListData(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<ConsumeListResults>>() {
            @Override
            public void accept(HttpResultModel<ConsumeListResults> consumeListResultsHttpResultModel) throws Exception {
                notifyData(consumeListResultsHttpResultModel.data,page);
                mContent.getRecyclerView().setPage(consumeListResultsHttpResultModel.current_page,consumeListResultsHttpResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 获取充值明细
     * */
    private void getRechargeListData(final int page) {
        Flowable<HttpResultModel<RechargeListResults>> fr = DataService.getRechargeListData(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<RechargeListResults>>() {
            @Override
            public void accept(HttpResultModel<RechargeListResults> rechargeListResultsHttpResultModel) throws Exception {
                notifyData(rechargeListResultsHttpResultModel.data,page);
                mContent.getRecyclerView().setPage(rechargeListResultsHttpResultModel.current_page,rechargeListResultsHttpResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 获取提现明细
     * */
    private void getCashListData(final int page) {
        Flowable<HttpResultModel<CashListResults>> fr = DataService.getCashListData(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CashListResults>>() {
            @Override
            public void accept(HttpResultModel<CashListResults> cashListResultsHttpResultModel) throws Exception {
                notifyData(cashListResultsHttpResultModel.data,page);
                mContent.getRecyclerView().setPage(cashListResultsHttpResultModel.current_page,cashListResultsHttpResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    /**
     * 获取收益明细
     * */
    private void getProfitListData(final int page) {
        Flowable<HttpResultModel<ProfitListResults>> fr = DataService.getProfitListData(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<ProfitListResults>>() {
            @Override
            public void accept(HttpResultModel<ProfitListResults> profitListResultsHttpResultModel) throws Exception {
                notifyData(profitListResultsHttpResultModel.data,page);
                mContent.getRecyclerView().setPage(profitListResultsHttpResultModel.current_page,profitListResultsHttpResultModel.total_page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }

    private void notifyData(XBaseModel data,int page){
        mAdapter.setData(data,page == 1 ? true : false);
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showContent();
    }

    public void showError(NetError error) {
        mContent.getLoadingView().setVisibility(View.GONE);
        mContent.refreshState(false);
        mContent.showError();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public Object newP() {
        return null;
    }
}
