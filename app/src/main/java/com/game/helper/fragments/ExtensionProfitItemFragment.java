package com.game.helper.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.adapters.ExtensionCommonAdapter;
import com.game.helper.adapters.RechargeCommonAdapter;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.wallet.WalletListFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.MarketExpectedFlowlistResults;
import com.game.helper.model.MarketFlowlistResults;
import com.game.helper.model.RechargeListResults;
import com.game.helper.net.DataService;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.views.widget.StateView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * 推广收益
 */
public class ExtensionProfitItemFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = ExtensionProfitItemFragment.class.getSimpleName();
    public static final int Type_Extension_Gold = 0;
    public static final int Type_Plan_Gold = 1;
    private int type;

    @BindView(R.id.rc_extension_list)
    XRecyclerContentLayout mContent;
    @BindView(R.id.tv_left)
    TextView left;
    @BindView(R.id.tv_center)
    TextView center;
    @BindView(R.id.tv_right)
    TextView right;

    private ExtensionCommonAdapter mAdapter;
    private StateView errorView;
    private View loadingView;

    @SuppressLint("ValidFragment")
    public ExtensionProfitItemFragment(int type) {
        this.type = type;
    }

    public static ExtensionProfitItemFragment newInstance(int type){
        return new ExtensionProfitItemFragment(type);
    }

    public ExtensionProfitItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_extension_profit_item;
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
        getDataFromNet(1);
    }

    /**
     * 获取数据
     * */
    private void getDataFromNet(int page){
        if (type == Type_Extension_Gold) {
            getMarketFlowList(page);
            left.setText("时间");
            center.setText("收入类型");
            right.setText("金币");
        }
        if (type == Type_Plan_Gold) {
            getMarketExpectedFlowList(page);
            left.setText("昵称");
            center.setText("金额");
            right.setText("状态");
        }
    }

    private void initList(){
        mAdapter = null;
        mAdapter = new ExtensionCommonAdapter(getContext(), null, type);
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
     * 获取推广收益
     * */
    private void getMarketFlowList(final int page) {
        Flowable<HttpResultModel<MarketFlowlistResults>> fr = DataService.getMarketFlowList(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MarketFlowlistResults>>() {
            @Override
            public void accept(HttpResultModel<MarketFlowlistResults> marketFlowlistResultsHttpResultModel) throws Exception {
                notifyData(marketFlowlistResultsHttpResultModel.data,page);
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
     * 获取预期收益
     * */
    private void getMarketExpectedFlowList(final int page) {
        Flowable<HttpResultModel<MarketExpectedFlowlistResults>> fr = DataService.getMarketExpectedFlowList(new SinglePageRequestBody(page));
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<MarketExpectedFlowlistResults>>() {
            @Override
            public void accept(HttpResultModel<MarketExpectedFlowlistResults> marketExpectedFlowlistResultsHttpResultModel) throws Exception {
                notifyData(marketExpectedFlowlistResultsHttpResultModel.data,page);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                showError(netError);
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
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
    }

    @Override
    public Object newP() {
        return null;
    }
}
