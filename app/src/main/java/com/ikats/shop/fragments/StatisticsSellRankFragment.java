package com.ikats.shop.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.ikats.shop.R;
import com.ikats.shop.adapters.StatisItemAdapter;
import com.ikats.shop.dialog.CommonDialogFragment;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.model.StatisItemBean;
import com.ikats.shop.present.FStatisticsPresenter;
import com.ikats.shop.views.XReloadableListContentLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;

public class StatisticsSellRankFragment extends XBaseFragment<FStatisticsPresenter> {

    @BindView(R.id.statis_sell_rank_reload_list_layout)
    XReloadableListContentLayout reloadableListContentLayout;
    StatisItemAdapter statisItemAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
    String start, end;

    @Override
    public void initData(Bundle savedInstanceState) {
        start = getArguments().getString("stime", simpleDateFormat.format(new Date()));
        end = getArguments().getString("etime", simpleDateFormat.format(new Date()));
        reloadableListContentLayout.getRecyclerView().verticalLayoutManager(context);
        statisItemAdapter = new StatisItemAdapter(context);
        statisItemAdapter.setRecItemClick(new RecyclerItemCallback<StatisItemBean, StatisItemAdapter.ViewHolder>() {
            @Override
            public void onItemClick(int position, StatisItemBean model, int tag, StatisItemAdapter.ViewHolder holder) {
                super.onItemClick(position, model, tag, holder);
                DialogFragmentHelper.builder(new CommonDialogFragment.OnCallDialog() {
                    @Override
                    public Dialog getDialog(Context context) {
                        return new AlertDialog.Builder(context).setMessage("别点了，终究不会有结果的！").setPositiveButton("确定"
                                , (dialog, which) -> dialog.cancel()).create();
                    }
                }, true).show(getChildFragmentManager(), "");
            }
        });
        reloadableListContentLayout.setInterceptTouch(true);
        reloadableListContentLayout.getRecyclerView().setAdapter(statisItemAdapter);
        reloadableListContentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showLong("onRefresh");
                reloadableListContentLayout.postDelayed(() -> {
                    reloadableListContentLayout.refreshState(false);
                    reloadableListContentLayout.getRecyclerView().setPage(1, 1);
//                    reloadableListContentLayout.
                }, 1000);
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
        for (int i = 0; i < 5; i++) {
            StatisItemBean bean = new StatisItemBean();
            bean.sort = String.valueOf(i);
            bean.goods = "藤椒琵琶腿200g";
            bean.qrcode = "20309941667";
            bean.sellCount = "100";
            bean.sellAmount = "1000";
            statisItemAdapter.addElement(bean);
        }
//        getP().requestStatisListData(context, start, end);
    }

    @Override
    public FStatisticsPresenter newP() {
        return new FStatisticsPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_sell_rank_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static StatisticsSellRankFragment newInstance(String... params) {
        return new StatisticsSellRankFragment(params);
    }

    public StatisticsSellRankFragment(String... params) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("stime", params[0]);
        bundle.putCharSequence("etime", params[1]);
        StatisticsSellRankFragment.this.setArguments(bundle);
    }
}
