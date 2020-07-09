package com.zhny.library.presenter.data.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.zhny.library.R;
import com.zhny.library.databinding.ActivityDataDetailBinding;
import com.zhny.library.presenter.data.adapter.DataDetailAdapter;
import com.zhny.library.presenter.data.custom.group.GroupItemDecoration;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.model.vo.DataDetailVo;
import com.zhny.library.presenter.data.util.TableDataHelper;
import com.zhny.library.presenter.data.viewmodel.DataDetailViewModel;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DataDetailActivity extends BaseChartActivity implements CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener, CalendarView.OnCalendarInterceptListener, CalendarView.OnMonthChangeListener {

    private ActivityDataDetailBinding binding;
    private DataDetailViewModel viewModel;
    private DataDetailAdapter detailAdapter;
    private List<String> hasDataDate = new ArrayList<>();
    private int mYear;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(DataDetailViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_detail);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.data_detail_title));

        binding.dataDetailCalendarView.setOnCalendarSelectListener(this);
        binding.dataDetailCalendarView.setOnMonthChangeListener(this);
        binding.dataDetailCalendarView.setOnYearChangeListener(this);
        binding.dataDetailCalendarView.setOnCalendarInterceptListener(this);

        detailAdapter = new DataDetailAdapter(getApplicationContext());
        binding.rlDataDetail.setLayoutManager(new LinearLayoutManager(this));
        binding.rlDataDetail.addItemDecoration(new GroupItemDecoration<String, DataDetailVo>());
        binding.rlDataDetail.setAdapter(detailAdapter);


        //点击年月textView
        binding.tvDataDetail.setOnClickListener(v -> {
            if (!binding.dataCalendarLayout.isExpand()) {
                binding.dataCalendarLayout.expand();
                return;
            }
            binding.dataDetailCalendarView.showYearSelectLayout(mYear);
            binding.tvDataDetail.setText(mYear + "年");
        });


        mYear = binding.dataDetailCalendarView.getCurYear();
        int month = binding.dataDetailCalendarView.getCurMonth();

        String monthStr = "".concat(String.valueOf(mYear)).concat("年")
                .concat(month < 10 ? "0" : "")
                .concat(String.valueOf(month)).concat("月");
        binding.tvDataDetail.setText(monthStr);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getYearData();
    }

    //获取2004-01-01到今天的所有数据
    private void getYearData() {
        viewModel.getWorkDate("2020-01-01", TimeUtils.getTodayStr(new Date())).observe(this, baseDto -> {
            if (baseDto.getContent() != null) {
                hasDataDate.clear();
                hasDataDate.addAll(baseDto.getContent());
                String today = TimeUtils.getDateStr(new Date(), 0);
                hasDataDate.add(today);
                try {
                    //刷新当前页面
                    Calendar calendar = new Calendar();
                    String[] dateArr = today.split("-");
                    calendar.setYear(Integer.valueOf(dateArr[0]));
                    calendar.setMonth(Integer.valueOf(dateArr[1]));
                    calendar.setDay(Integer.valueOf(dateArr[2]));

                    //刷新当前日历界面
                    binding.dataDetailCalendarView.scrollToCalendar(calendar.getYear(), calendar.getMonth(), calendar.getDay());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    public void prePage(View view) {
        binding.dataDetailCalendarView.scrollToPre();
    }

    public void nextPage(View view) {
        binding.dataDetailCalendarView.scrollToNext();
    }


    @Override
    public void onYearChange(int year) {
        binding.tvDataDetail.setText("".concat(String.valueOf(year)).concat("年"));
    }

    @Override
    public void onMonthChange(int year, int month) {
        String monthStr = "".concat(String.valueOf(year)).concat("年")
                .concat(month < 10 ? "0" : "")
                .concat(String.valueOf(month)).concat("月");
        binding.tvDataDetail.setText(monthStr);
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        String date = getDate(calendar);
        getSelectDateData(date);
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }


    //选择某日后请求数据请求数据
    private void getSelectDateData(String date) {
        if (TextUtils.isEmpty(date) || TimeUtils.maxToday(date)) return;
        boolean isToday = TimeUtils.isToday(date);
        Log.d(TAG, "==> getSelectDateData: " + date + ", isToday :" + isToday);
        viewModel.getJobReport(date).observe(this, baseDto -> {
            if (baseDto.getContent() == null) {
                return;
            }
            List<JobReportDto.JobBean> jobList = baseDto.getContent().jobList;
            if (jobList != null) {
                List<DataDetailVo> data = TableDataHelper.getDataDetail(jobList, isToday);
                detailAdapter.refresh(data);
            } else {
                detailAdapter.refresh(new ArrayList<>());
            }
        });
    }


    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        String curDate = getDate(calendar);
        //拦截无数据的日期
        return !hasDataDate.contains(curDate);
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
    }




    private String getDate(Calendar calendar) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

}
