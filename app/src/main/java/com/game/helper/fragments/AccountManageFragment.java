package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.adapters.AccountManagerAdapter;
import com.game.helper.adapters.ExtensionCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.GameAccountResultModel;
import com.game.helper.model.MarketFlowlistResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.GameAccountRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
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

public class AccountManageFragment extends XBaseFragment implements View.OnClickListener{
    private static final String TAG = AccountManageFragment.class.getSimpleName();

    @BindView(R.id.action_bar_back)
    View mHeadBack;
    @BindView(R.id.action_bar_tittle)
    TextView mHeadTittle;
    @BindView(R.id.rc_account_list)
    XRecyclerContentLayout mContent;

    private AccountManagerAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    public static AccountManageFragment newInstance(){
        return new AccountManageFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account_mannager;
    }

    private void initView() {
        mHeadTittle.setText(getResources().getString(R.string.common_account_mannager));
        mHeadBack.setOnClickListener(this);

        if (errorView == null) {
            errorView = new StateView(context);
            errorView.setOnRefreshAndLoadMoreListener(mContent.getRecyclerView().getOnRefreshAndLoadMoreListener());
        }
        if (null != errorView.getParent())
            ((ViewGroup) errorView.getParent()).removeView(errorView);
        if (loadingView == null) loadingView = View.inflate(context, R.layout.view_loading, null);
        if (null != loadingView.getParent())
            ((ViewGroup) loadingView.getParent()).removeView(loadingView);
        mContent.errorView(errorView);
        mContent.loadingView(loadingView);
        mContent.showLoading();
        initList();
        getDataFromNet(1);
    }

    /**
     * 获取数据
     * */
    private void getDataFromNet(int page){
        getGameAccountInfo(page);
    }

    private void initList(){
        mAdapter = null;
        mAdapter = new AccountManagerAdapter(getContext(), null);
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

    private void getGameAccountInfo(final int page) {
        Flowable<HttpResultModel<GameAccountResultModel>> fr = DataService.getGameAccountList(new GameAccountRequestBody(page, 1, 0, 0));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<GameAccountResultModel>>() {
            @Override
            public void accept(HttpResultModel<GameAccountResultModel> gameAccountResultModelHttpResultModel) throws Exception {
                notifyData(gameAccountResultModelHttpResultModel.data,page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
            }
        });

    }

    private void notifyData(XBaseModel data, int page){
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
        if (v == mHeadBack){
            getActivity().onBackPressed();
        }
    }

    @Override
    public Object newP() {
        return null;
    }
}
