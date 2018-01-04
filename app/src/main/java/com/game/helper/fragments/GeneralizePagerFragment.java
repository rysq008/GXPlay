package com.game.helper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.activitys.DetailFragmentsActivity;
import com.game.helper.activitys.MainActivity;
import com.game.helper.activitys.RankingListActivity;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.fragments.login.LoginFragment;
import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CommonShareResults;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.model.model.MemberBean;
import com.game.helper.net.DataService;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.BannerView;
import com.game.helper.views.HeadImageView;
import com.game.helper.views.widget.StateView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xstatecontroller.XStateController;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by zr on 2017-10-13.
 */

public class GeneralizePagerFragment extends XBaseFragment implements View.OnClickListener {

    public static final String TAG = "GeneralizePagerFragment";
    @BindView(R.id.generalize_root_layout)
    XStateController xStateController;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.generalize_banner_view)
    BannerView bannerView;
    @BindView(R.id.generalize_total_income_tv)
    TextView total_tv;
    @BindView(R.id.generalize_expect_income_tv)
    TextView expect_tv;
    @BindView(R.id.generalize_income_tv)
    TextView generalize_tv;
    @BindView(R.id.generalize_head_iv)
    HeadImageView head_iv;
    StateView stateView;
    @BindView(R.id.rangeTv)
    TextView rangeTv;
    @BindView(R.id.mallTv)
    TextView mallTv;
    @BindView(R.id.activityTv)
    TextView activityTv;
    @BindView(R.id.shareIncome)
    TextView shareIncome;
    @BindView(R.id.shareDiscount)
    TextView shareDiscount;
    @BindView(R.id.loginTv)
    TextView loginTv;
    @BindView(R.id.login_layout)
    RelativeLayout loginLayout;
    @BindView(R.id.loginRl)
    RelativeLayout loginRl;


    @Override
    public void initData(Bundle savedInstanceState) {

        rangeTv.setOnClickListener(this);
        mallTv.setOnClickListener(this);
        activityTv.setOnClickListener(this);
        shareIncome.setOnClickListener(this);
        shareDiscount.setOnClickListener(this);
        expect_tv.setOnClickListener(this);
        total_tv.setOnClickListener(this);
        generalize_tv.setOnClickListener(this);
        loginTv.setOnClickListener(this);

        swipeRefreshLayout.setColorSchemeResources(
                cn.droidlover.xrecyclerview.R.color.x_red,
                cn.droidlover.xrecyclerview.R.color.x_blue,
                cn.droidlover.xrecyclerview.R.color.x_yellow,
                cn.droidlover.xrecyclerview.R.color.x_green
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        if (null == stateView) {
            stateView = new StateView(context);
            stateView.setCustomClickListener(new StateView.StateViewClickListener() {
                @Override
                public void doAction() {
                    refreshData();
                }
            });
        }
        xStateController.errorView(stateView);
        xStateController.loadingView(View.inflate(context, R.layout.view_loading, null));
//        xStateController.showLoading();
        refreshData();
        getBannerInfo();
    }

    private void getBannerInfo() {
        Flowable<HttpResultModel<BannerResults>> fb = DataService.getHomeBanner(new BannerRequestBody(2));

        RxLoadingUtils.subscribe(fb, this.bindToLifecycle(), new Consumer<HttpResultModel<BannerResults>>() {
            @Override
            public void accept(HttpResultModel<BannerResults> data) throws Exception {
                bannerView.setData(data.data);
                xStateController.showContent();
                xStateController.getLoadingView().setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                xStateController.getLoadingView().setVisibility(View.GONE);
                xStateController.showError();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshData() {
        Flowable<HttpResultModel<GeneralizeResults>> flowable = DataService.getGeneralizeData();
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<GeneralizeResults>>() {
            @Override
            public void accept(HttpResultModel<GeneralizeResults> generalizeResultsHttpResultModel) throws Exception {

                GeneralizeResults generalizeResults = generalizeResultsHttpResultModel.data;
                if (!Kits.Empty.check(generalizeResults)) {
                    MemberBean memberBean = generalizeResults.getMember();
                    BusProvider.getBus().post(new MsgEvent<String>(0, RxConstant.Head_Image_Change_Type, memberBean.getIcon()));
                    total_tv.setText(generalizeResults.zongshouyi);
                    generalize_tv.setText(generalizeResults.yue);
                    expect_tv.setText(generalizeResults.yujizongshouyi);
                }
                xStateController.showContent();
                xStateController.getLoadingView().setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                xStateController.getLoadingView().setVisibility(View.GONE);
                xStateController.showError();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_generalize_layout;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static GeneralizePagerFragment newInstance() {
        return new GeneralizePagerFragment();
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.rangeTv://排行榜
                intent.setClass(getActivity(), RankingListActivity.class);
                startActivity(intent);
                break;
            case R.id.mallTv://商城
                //取Market_url
                String expected_url = SharedPreUtil.getH5url(SharedPreUtil.EXPECTED_URL);
                Bundle shopBundle = new Bundle();
                shopBundle.putString(WebviewFragment.PARAM_URL, expected_url.concat("?" + SharedPreUtil.getSessionId()));
                shopBundle.putString(WebviewFragment.PARAM_TITLE, "敬请期待");
                DetailFragmentsActivity.launch(getContext(), shopBundle, WebviewFragment.newInstance());
                break;
            case R.id.activityTv://活动
                //取Market_url
                String s1 = SharedPreUtil.getH5url(SharedPreUtil.EXPECTED_URL);
                Bundle activityBundle = new Bundle();
                activityBundle.putString(WebviewFragment.PARAM_URL, s1.concat("?" + SharedPreUtil.getSessionId()));
                activityBundle.putString(WebviewFragment.PARAM_TITLE, "敬请期待");
                DetailFragmentsActivity.launch(getContext(), activityBundle, WebviewFragment.newInstance());
                break;
            case R.id.generalize_total_income_tv:
            case R.id.generalize_expect_income_tv:
            case R.id.generalize_income_tv:
                DetailFragmentsActivity.launch(getContext(), null, ExtensionProfitFragment.newInstance());
                break;
            case R.id.shareIncome://分享收益
                //取Market_url
                String market_url = SharedPreUtil.getH5url(SharedPreUtil.H5_URL_MARKET);
                Bundle bundle = new Bundle();
                bundle.putString(WebviewFragment.PARAM_URL, market_url.concat("?" + SharedPreUtil.getSessionId()));
                bundle.putString(WebviewFragment.PARAM_TITLE, "分享收益");
                DetailFragmentsActivity.launch(getContext(), bundle, WebviewFragment.newInstance());
                break;
            case R.id.shareDiscount://分享折扣
                fetchShareInfo();
                break;
            case R.id.loginTv://登录
                DetailFragmentsActivity.launch(getContext(), null, LoginFragment.newInstance());
                break;

        }
    }

    /**
     * 获取分享信息
     */
    private void fetchShareInfo() {
        Flowable<HttpResultModel<CommonShareResults>> fr = DataService.getG9Info();
        RxLoadingUtils.subscribe(fr, bindToLifecycle(), new Consumer<HttpResultModel<CommonShareResults>>() {
            @Override
            public void accept(HttpResultModel<CommonShareResults> shareInfoHttpResultModel) throws Exception {
                if (shareInfoHttpResultModel.isSucceful()) {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).umShare(shareInfoHttpResultModel.data);
                    }
                }
            }
        }, new Consumer<NetError>() {
            @Override
            public void accept(NetError netError) throws Exception {
                Log.e(TAG, "Link Net Error! Error Msg: " + netError.getMessage().trim());
            }
        });
    }


    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        if (loginRl == null || loginLayout == null)
            return;
        if (!TextUtils.isEmpty(SharedPreUtil.getSessionId())) {
            loginRl.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            refreshData();
        } else {
            loginRl.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, info == null ? "" : info.icon));
        }
    }
}
