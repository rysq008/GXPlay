package com.ikats.shop.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerView;

public class StatisticsFragment extends XBaseFragment<FStatisticsPresenter> {

    @BindView(R.id.statis_start_time_tv)
    TextView startTv;
    @BindView(R.id.statis_end_time_tv)
    TextView endTv;
    @BindView(R.id.statis_today_time_tv)
    TextView todayTv;
    @BindView(R.id.statis_near_time_tv)
    TextView nearTv;
    @BindView(R.id.statis_week_time_tv)
    TextView weekTv;
    @BindView(R.id.statis_month_time_tv)
    TextView monthTv;
    @BindView(R.id.statis_seasons_time_tv)
    TextView seasonsTv;
    @BindView(R.id.statis_reload_list_layout)
    XReloadableListContentLayout reloadableListContentLayout;
    StatisItemAdapter statisItemAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String start, end;

    @Override
    public void initData(Bundle savedInstanceState) {
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

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.statis_start_time_tv, R.id.statis_end_time_tv, R.id.statis_today_time_tv,
            R.id.statis_near_time_tv, R.id.statis_week_time_tv, R.id.statis_month_time_tv
            , R.id.statis_seasons_time_tv})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.statis_start_time_tv:
            case R.id.statis_end_time_tv:
                DialogFragmentHelper.builder(context -> {
                    DatePickerDialog pickerDialog = new DatePickerDialog(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    Locale locale = getResources().getConfiguration().locale;
                    Locale.setDefault(locale);
                    pickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                        int time = year * 1000 + month * 100 + dayOfMonth;
                        view.setTag(time);
                        ((TextView) view).setText(year + "年" + month + "月" + dayOfMonth + "日");
                        if (view == startTv) {
                            if (endTv.getTag() != null && ((int) (endTv.getTag())) < time) {
                                ToastUtils.showLong("开始时间必须大于结束时间");
                                return;
                            }
                        } else if (view == endTv) {
                            if (startTv.getTag() != null && (int) (startTv.getTag()) <= time) {
                                getP().requestStatisListData(getActivity(), startTv.getTag().toString(), time + "");
                            }
                        }
                        pickerDialog.cancel();
                    });
                    return pickerDialog;
                }, true).show(getChildFragmentManager(), "");
                break;
            case R.id.statis_today_time_tv:
                start = (simpleDateFormat.format(System.currentTimeMillis()));
                end = (simpleDateFormat.format(System.currentTimeMillis()));
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_near_time_tv:
                start = (simpleDateFormat.format(System.currentTimeMillis()));
                Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
                Calendar.getInstance().add(Calendar.DAY_OF_WEEK_IN_MONTH, -3);
                end = simpleDateFormat.format(Calendar.getInstance().getTime());
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_week_time_tv:
                start = (simpleDateFormat.format(System.currentTimeMillis()));
                Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
                Calendar.getInstance().add(Calendar.WEEK_OF_MONTH, -1);
                end = simpleDateFormat.format(Calendar.getInstance().getTime());
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_month_time_tv:
                start = (simpleDateFormat.format(System.currentTimeMillis()));
                Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
                Calendar.getInstance().add(Calendar.MONTH, -1);
                end = simpleDateFormat.format(Calendar.getInstance().getTime());
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_seasons_time_tv:
                start = (simpleDateFormat.format(System.currentTimeMillis()));
                Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
                Calendar.getInstance().add(Calendar.MONTH, -3);
                end = simpleDateFormat.format(Calendar.getInstance().getTime());
                getP().requestStatisListData(context, start, end);
                break;
        }
    }

    @Override
    public FStatisticsPresenter newP() {
        return new FStatisticsPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

}
