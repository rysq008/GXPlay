package com.ikats.shop.fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.ikats.shop.R;
import com.ikats.shop.dialog.DialogFragmentHelper;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.present.FStatisticsPresenter;
import com.ikats.shop.views.SetScrollViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.kit.Kits;

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
    @BindView(R.id.statis_search_commit_btn)
    Button statis_btn;
    @BindView(R.id.statis_rank_list_vp)
    SetScrollViewPager rank_vp;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
    String start = "", end = "";
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    public void initData(Bundle savedInstanceState) {
        rank_vp.setSaveFromParentEnabled(false);
        mFragments.add(StatisticsSellAmountFragment.newInstance(start, end));
        mFragments.add(StatisticsSellRankFragment.newInstance(start, end));
        rank_vp.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragments, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.statis_start_time_tv, R.id.statis_end_time_tv, R.id.statis_today_time_tv,
            R.id.statis_near_time_tv, R.id.statis_week_time_tv, R.id.statis_month_time_tv
            , R.id.statis_seasons_time_tv, R.id.statis_sell_sort_rb, R.id.statis_sell_amount_rb,
            R.id.statis_search_commit_btn})
    public void OnViewClick(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        switch (view.getId()) {
            case R.id.statis_start_time_tv:
            case R.id.statis_end_time_tv:
                DialogFragmentHelper.builder(context -> {
                    DatePickerDialog pickerDialog = new DatePickerDialog(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    Locale locale = getResources().getConfiguration().locale;
//                    Locale.setDefault(locale);

                    pickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                        month++;
                        ((TextView) view).setText(year + "年" + month + "月" + dayOfMonth + "日");
                        if (view == startTv)
                            start = String.format("%d_%02d_%02d", year, month, dayOfMonth);
                        else if (view == endTv)
                            end = String.format("%d_%02d_%02d", year, month, dayOfMonth);
                        pickerDialog.cancel();
                    });
                    return pickerDialog;
                }, true).show(getChildFragmentManager(), "");
                break;
            case R.id.statis_today_time_tv:
                start = (simpleDateFormat.format(System.currentTimeMillis()));
                end = (simpleDateFormat.format(System.currentTimeMillis()));
                startTv.setText(start);
                endTv.setText(end);
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_near_time_tv:
                end = (simpleDateFormat.format(System.currentTimeMillis()));

//                Calendar calendar = Calendar.getInstance();
//                calendar.add(Calendar.DAY_OF_MONTH, 3);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
//                Date date = calendar.getTime();
//                System.out.println("获取当前时间未来的第三天：" + date);
//                calendar.setTime(date);
//                String time = simpleDateFormat.format(date);
//                System.out.println("格式化获取当前时间未来的第三天：" + time);


                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.DAY_OF_MONTH, -2);
                start = simpleDateFormat.format(calendar.getTime());
                startTv.setText(start);
                endTv.setText(end);
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_week_time_tv:
                end = (simpleDateFormat.format(System.currentTimeMillis()));
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.WEEK_OF_MONTH, -1);
                start = simpleDateFormat.format(calendar.getTime());
                startTv.setText(start);
                endTv.setText(end);
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_month_time_tv:
                end = (simpleDateFormat.format(System.currentTimeMillis()));
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MONTH, -1);
                start = simpleDateFormat.format(calendar.getTime());
                startTv.setText(start);
                endTv.setText(end);
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_seasons_time_tv:
                end = (simpleDateFormat.format(System.currentTimeMillis()));
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MONTH, -3);
                start = simpleDateFormat.format(calendar.getTime());
                startTv.setText(start);
                endTv.setText(end);
                getP().requestStatisListData(context, start, end);
                break;
            case R.id.statis_sell_amount_rb://金额统计
                rank_vp.setCurrentItem(0);
                break;
            case R.id.statis_sell_sort_rb://排行统计
                rank_vp.setCurrentItem(1);
                break;
            case R.id.statis_search_commit_btn:
                int st = Kits.Empty.check(start) ? 0 : Integer.parseInt((start.replace("_", "")));
                int et = Kits.Empty.check(end) ? 0 : Integer.parseInt((end.replace("_", "")));
                if (et > 0 && st > et) {
                    ToastUtils.showLong("开始时间必须小于结束时间");
                    return;
                }
                startTv.setText(start);
                endTv.setText(end);
                getP().requestStatisListData(getActivity(), start, end);
                break;
        }
    }

    public void showData() {
        clear();
        mFragments.add(StatisticsSellAmountFragment.newInstance(start, end));
        mFragments.add(StatisticsSellRankFragment.newInstance(start, end));
        rank_vp.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragments, null));
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

    public void clear() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        for (Fragment fragment : mFragments) {
            transaction.remove(fragment).commitNow();
            fragment = null;
        }
        mFragments.clear();
    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

}