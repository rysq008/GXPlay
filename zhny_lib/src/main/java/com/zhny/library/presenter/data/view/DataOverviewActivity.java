package com.zhny.library.presenter.data.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.zhny.library.R;
import com.zhny.library.databinding.ActivityDataOverviewBinding;
import com.zhny.library.presenter.data.adapter.DataDetailAdapter;
import com.zhny.library.presenter.data.custom.group.GroupItemDecoration;
import com.zhny.library.presenter.data.custom.render.CustomYAxisRender;
import com.zhny.library.presenter.data.model.dto.DataStatisticsDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.model.vo.DataDetailVo;
import com.zhny.library.presenter.data.util.DataStatisticsUtil;
import com.zhny.library.presenter.data.util.TableDataHelper;
import com.zhny.library.presenter.data.viewmodel.DataOverviewViewModel;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


public class DataOverviewActivity extends BaseChartActivity implements View.OnClickListener, OnChartGestureListener {

    private Context context;
    private ActivityDataOverviewBinding binding;
    private DataOverviewViewModel viewModel;
    private DataStatisticsDto dataStatisticsDto;
    private CombinedChart chart;
    private final int count = 12;
    private int showYear;
    private int nowYear;
    protected static final float FLIP_DISTANCE = 30;
    private DataDetailAdapter detailAdapter;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(DataOverviewViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_overview);
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        init();
    }

    public void init() {
        context = this;
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.datatitle));
        dataStatisticsDto = new DataStatisticsDto();
        Calendar rightNow = Calendar.getInstance();
        nowYear = rightNow.get(Calendar.YEAR);
        showYear = nowYear;
        initChart();

        detailAdapter = new DataDetailAdapter(getApplicationContext());
        binding.rlYesDetail.setLayoutManager(new LinearLayoutManager(this));
        binding.rlYesDetail.addItemDecoration(new GroupItemDecoration<String, DataDetailVo>());
        binding.rlYesDetail.setAdapter(detailAdapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initChart() {
        chart = binding.combinedChart;
        chart.setNoDataText("暂无数据");
        chart.setNoDataTextColor(R.color.color_999999);

        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        chart.setHighlightFullBarEnabled(false);
        chart.getDescription().setEnabled(false); // 不显示描述
        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(true);//可点击
        chart.setOnChartGestureListener(this); //监听手势事件
        chart.setOnClickListener(this);
        setAxis();
        setLegend();

        //解决NestedScrollView和Chart的滑动冲突
        chart.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    binding.nScrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    binding.nScrollView.requestDisallowInterceptTouchEvent(false);
                    break;
                }
            }
            return false;
        });
    }

    /**
     * 坐标轴初始化
     */
    private void setAxis() {
        // 设置x轴
        XAxis xAxis = binding.combinedChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置x轴显示在下方，默认在上方
        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setLabelCount(12);  // 设置x轴上的标签个数

        final String[] labelName = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        /*解决左右两端柱形图只显示一半的情况 只有使用CombinedChart时会出现，如果单独使用BarChart不会有这个问题*/
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(labelName.length - 0.5f);

        // 转换要显示的标签文本，value值默认是int从0开始
        xAxis.setValueFormatter((value, axis) -> labelName[(int) value]);


        //right y
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setDrawZeroLine(true);
        rightYAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        CustomYAxisRender rightYAxisRender = new CustomYAxisRender(chart.getViewPortHandler(), rightYAxis, chart.getTransformer(YAxis.AxisDependency.RIGHT));
        rightYAxisRender.setLabelAxis("时间/h", DisplayUtils.sp2px(11), Color.parseColor("#666666"), true);
        chart.setRendererRightYAxis(rightYAxisRender);

        //left y
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setDrawZeroLine(true);
        leftYAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        CustomYAxisRender leftYAxisRender = new CustomYAxisRender(chart.getViewPortHandler(), leftYAxis, chart.getTransformer(YAxis.AxisDependency.LEFT));
        leftYAxisRender.setLabelAxis("面积/亩", DisplayUtils.sp2px(11), Color.parseColor("#666666"), false);
        chart.setRendererLeftYAxis(leftYAxisRender);
    }


    //设置图例
    private void setLegend() {
        Legend l = chart.getLegend();
        l.setFormSize(getResources().getDimension(R.dimen.dp_0)); // 图例的图形大小
        int px = DisplayUtils.sp2px(15.3600f);
        l.setTextSize(DisplayUtils.px2dp(px)); // 图例的文字大小
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
    }


    //设置折线图数据
    private LineData generateLineData(int[] array) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            entries.add(new Entry(index, array[index]));
        }
        lineDataSet = new LineDataSet(entries, null);
        lineDataSet.setColor(getResources().getColor(R.color.color_data_line));
        lineDataSet.setLineWidth(1.02f);
        lineDataSet.setCircleColor(getResources().getColor(R.color.color_data_line));
        lineDataSet.setCircleRadius(2.88f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleHoleColor(getResources().getColor(R.color.color_data_line));
        lineDataSet.setCircleHoleRadius(2.4f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(getResources().getColor(R.color.color_data_line));
        lineDataSet.setHighlightEnabled(false); //设置为true选择点后，会自动聚焦

        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        return lineData;
    }

    //设置柱状图数据
    private BarData generateBarData(double[] arrayBar) {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) arrayBar[i];
            values.add(new BarEntry(i, val));
        }
        barDataSet = new BarDataSet(values, null);

        barDataSet.setColor(getResources().getColor(R.color.color_data_bar)); // 柱子的颜色
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(getResources().getColor(R.color.color_data_bar));

        barDataSet.setLabel(showYear + "年数据统计图"); // 设置标签之后，图例的内容默认会以设置的标签显示
        barDataSet.setHighlightEnabled(false);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.6f); // 设置柱子的宽度
        return barData;
    }

    public void showDataDetail(View view) {
        //进入数据明细页面
        startActivity(DataDetailActivity.class);
    }

    @Override
    protected boolean isShowChart() {
        return true;
    }

    @Override
    protected void onChartListener() {
        startActivity(ChartActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestData();
    }

    private void requestData() {
        //请求统计数据
        viewModel.getDataStatistics().observe(this, baseDto -> {
            dataStatisticsDto = baseDto.getContent();
            if (dataStatisticsDto != null) {
                showYear = nowYear;
                updateData();
            }
        });

        //请求今日数据
        String today = TimeUtils.getDateStr(new Date(), 0);
        viewModel.getJobReport(today).observe(this, baseDto -> {
            if (baseDto.getContent() == null) {
                return;
            }
            List<JobReportDto.JobBean> jobList = baseDto.getContent().jobList;
            if (jobList != null) {
                List<DataDetailVo> data = TableDataHelper.getDataDetail(jobList, true);
                detailAdapter.refresh(data);
            } else {
                detailAdapter.refresh(new ArrayList<>());
            }
        });
    }

    private void updateData() {
        binding.setDataStatisticsDto(dataStatisticsDto);
        //统计 update 总亩数/总时长
        dataStatisticsDto.convertTotalArea = DataStatisticsUtil.covertDoubleFormat2(dataStatisticsDto.totalArea);
        dataStatisticsDto.totalTimeHour = DataStatisticsUtil.getTime(dataStatisticsDto.totalTime, 0);
        dataStatisticsDto.totalTimeMin = DataStatisticsUtil.getTime(dataStatisticsDto.totalTime, 1);
        updateBottomAndChart(showYear);
    }

    //更新统计图以及底部数据
    private void updateBottomAndChart(int year) {
        DataStatisticsDto.YearData nowYearData = new DataStatisticsDto.YearData();
        binding.bottomLayout.setYearData(nowYearData);
        int[] arrayLine = new int[count];
        double[] arrayBar = new double[count];
        for (int i = 0; i < count; i++) {
            arrayLine[i] = 0;
            arrayBar[i] = 0;
        }
        //bottom总计 update 单年亩数/时长
        List<DataStatisticsDto.YearData> yearDataList = dataStatisticsDto.yearDataList;

        if (yearDataList != null && yearDataList.size() != 0) {

            for (DataStatisticsDto.YearData yearData : yearDataList) {
                if (TextUtils.equals(year + "", yearData.year)) {
                    //单年亩数/时长 update
                    nowYearData.convertYearArea = DataStatisticsUtil.covertDoubleFormat2(yearData.yearArea);
                    nowYearData.yearTimeHour = DataStatisticsUtil.getTime(yearData.yearTime, 0);
                    nowYearData.yearTimeMin = DataStatisticsUtil.getTime(yearData.yearTime, 1);
                    //monthDataList 统计图数据
                    nowYearData.monthDataList = yearData.monthDataList;
                    List<DataStatisticsDto.YearData.MonthData> monthDataList = nowYearData.monthDataList;
                    if (monthDataList != null && monthDataList.size() > 0) {
                        for (DataStatisticsDto.YearData.MonthData monthData : monthDataList) {
                            //月份
                            int month = DataStatisticsUtil.getMonth(monthData.month);

                            //月作业时长/h right Y轴
                            String monthTimeHour = DataStatisticsUtil.getTime(monthData.monthTime, 0);
                            //月作业亩数 left Y轴
                            String monthArea = DataStatisticsUtil.covertDoubleFormat2(monthData.monthArea);

                            if (month > 0 && month < 13) {
                                try {
                                    arrayLine[month - 1] = Integer.parseInt(monthTimeHour);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    arrayBar[month - 1] = Double.parseDouble(monthArea);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        //空数据处理
        nowYearData.convertYearArea = DataStatisticsUtil.dealEmptyString(nowYearData.convertYearArea, "0.00");
        nowYearData.yearTimeHour = DataStatisticsUtil.dealEmptyString(nowYearData.yearTimeHour, "0");
        nowYearData.yearTimeMin = DataStatisticsUtil.dealEmptyString(nowYearData.yearTimeMin, "0");

        //更新统计图
        CombinedData data = new CombinedData();
        data.setData(generateBarData(arrayBar));
        data.setData(generateLineData(arrayLine));
        chart.setDrawValueAboveBar(true);
        chart.setData(data);
        refreshCombinedView();
    }

    //刷新chart
    private void refreshCombinedView() {
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private BarDataSet barDataSet;
    private LineDataSet lineDataSet;
    private int index = 0;

    //修改图表的展示
    private void updateShowValue() {
        if (index < 2) index++;
        else index = 0;
        if (barDataSet != null && lineDataSet != null) {
            if (index == 1) {
                barDataSet.setDrawValues(true);
                lineDataSet.setDrawValues(false);
            } else if (index == 2) {
                barDataSet.setDrawValues(false);
                lineDataSet.setDrawValues(true);
            } else {
                barDataSet.setDrawValues(false);
                lineDataSet.setDrawValues(false);
            }
        }
        refreshCombinedView();
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        updateShowValue();
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        if (me1.getX() - me2.getX() > FLIP_DISTANCE) {
            if (dataStatisticsDto != null) {
                if (showYear < nowYear) {
                    updateBottomAndChart(++showYear);
                } else {
                    ++showYear;
                    Toast.makeText(context, "暂无" + showYear + "年数据！", Toast.LENGTH_SHORT).show();
                    --showYear;
                }
            }

        }
        if (me2.getX() - me1.getX() > FLIP_DISTANCE) {
            if (dataStatisticsDto != null) {
                updateBottomAndChart(--showYear);
            }
        }
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
