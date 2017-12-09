package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
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

public class GeneralizePagerFragment extends XBaseFragment {

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


    @Override
    public void initData(Bundle savedInstanceState) {
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
        xStateController.showLoading();
        refreshData();
    }

    private void refreshData() {
        Flowable<HttpResultModel<GeneralizeResults>> flowable = DataService.getGeneralizeData();
        RxLoadingUtils.subscribe(flowable, this.bindToLifecycle(), new Consumer<HttpResultModel<GeneralizeResults>>() {
            @Override
            public void accept(HttpResultModel<GeneralizeResults> generalizeResultsHttpResultModel) throws Exception {

                GeneralizeResults generalizeResults = generalizeResultsHttpResultModel.data;
                if (!Kits.Empty.check(generalizeResults)) {
                    BusProvider.getBus().post(new MsgEvent<String>(0, RxConstant.Head_Image_Change_Type, (String) generalizeResults.member.get("icon")));
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
}
