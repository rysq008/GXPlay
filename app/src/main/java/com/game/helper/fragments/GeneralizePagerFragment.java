package com.game.helper.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.game.helper.R;
import com.game.helper.fragments.BaseFragment.XBaseFragment;
import com.game.helper.views.widget.StateView;

import butterknife.BindView;
import cn.droidlover.xstatecontroller.XStateController;

/**
 * Created by zr on 2017-10-13.
 */

public class GeneralizePagerFragment extends XBaseFragment {

    @BindView(R.id.generalize_root_layout)
    XStateController xStateController;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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

                }
            });
        }
        xStateController.errorView(stateView);
        xStateController.loadingView(View.inflate(context, R.layout.view_loading, null));
        xStateController.showLoading();
        refreshData();
    }

    private void refreshData() {

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
