package com.zhny.library.presenter.data.fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.zhny.library.R;
import com.zhny.library.base.BaseFragment;
import com.zhny.library.databinding.FragmentBarLineBinding;
import com.zhny.library.presenter.data.custom.render.CustomXAxisRender;
import com.zhny.library.presenter.data.custom.render.CustomYAxisRender;
import com.zhny.library.presenter.data.dialog.CalendarRangeDialog;
import com.zhny.library.presenter.data.dialog.DataDevicePopWin;
import com.zhny.library.presenter.data.listener.OnTableDataListener;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.util.DataStatisticsUtil;
import com.zhny.library.presenter.data.util.TableDataHelper;
import com.zhny.library.presenter.data.viewmodel.TableDataViewModelViewModel;
import com.zhny.library.presenter.playback.customer.SingleBottomPopWin;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;


public class BarLineFragment extends BaseFragment implements OnTableDataListener,
        CalendarRangeDialog.OnCalendarRangeFinishListener,
        DataDevicePopWin.OnDataAddMachineListener,
        SingleBottomPopWin.OnSingleBottomPopWinListener, View.OnClickListener {

    private FragmentBarLineBinding binding;
    private TableDataViewModelViewModel viewModel;
    private SingleBottomPopWin singleBottomPopWin;
    private DataDevicePopWin dataDevicePopWin;
    private String startDate, endDate, snList;
    private List<String> sortList;
    private int selectSortIndex;
    private CalendarRangeDialog calendarRangeDialog;
    private List<JobReportDto.JobBean> jobBeanList = new ArrayList<>();
    private BarChart chart;
    private CustomYAxisRender leftYAxisRender;
    private int count;
    private static final int DEFAULT_COUNT = 10;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(TableDataViewModelViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bar_line, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        viewModel.setParams(getContext());

        binding.setOnTableDataListener(this);

        sortList = Arrays.asList(getResources().getStringArray(R.array.data_bar_line_sort));
        binding.tvTableDataSort.setText(sortList.get(0));
        singleBottomPopWin = new SingleBottomPopWin(getContext(), sortList, this);

        binding.tvTimeSelect.setText("选择时间");
        dataDevicePopWin = new DataDevicePopWin(getContext(), this);
        initChart();

        calendarRangeDialog = new CalendarRangeDialog(this);
    }

    private void initChart() {
        chart = binding.barChart;

        chart.setNoDataText("暂无数据");
        chart.setNoDataTextColor(R.color.color_999999);

        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        chart.setHighlightFullBarEnabled(false);
        chart.getDescription().setEnabled(false); // 不显示描述

        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(true);//可触摸

        chart.setOnClickListener(this);

        chart.getLegend().setEnabled(true);
        chart.setViewPortOffsets(DisplayUtils.dp2px(38.4f), DisplayUtils.dp2px(38.4f),
                DisplayUtils.dp2px(33.6f), DisplayUtils.dp2px(30f));
        setLegend();


        // 设置x轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置x轴显示在下方，默认在上方
        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        // xAxis.setAxisLineWidth(2f);//设置x轴宽度

        //Y
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setTextSize(10f);
        leftYAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);
        leftYAxisRender = new CustomYAxisRender(chart.getViewPortHandler(), leftYAxis, chart.getTransformer(YAxis.AxisDependency.LEFT));
    }

    @Override
    public void onStart() {
        super.onStart();
        selectSortIndex = 0;
        snList = null;
        startDate = TimeUtils.getDateStr(new Date(), -7);
        endDate = TimeUtils.getDateStr(new Date(), -1);
        String start2EndTime = startDate.replace("-", ".").concat(" - ").concat(endDate.replace("-", "."));
        binding.tvTimeSelect.setText(start2EndTime);
        requestData(startDate, endDate, snList, selectSortIndex);
    }

    //请求数据
    private void requestData(String start, String end, String snList, int selectSortIndex) {
        showLoading();
        viewModel.setHasData(true);
        int sort = selectSortIndex + 1;
        viewModel.getJobReport(start, end, snList, sort).observe(this, baseDto -> {
            if (baseDto.getContent() != null && baseDto.getContent().jobList != null &&
                    baseDto.getContent().jobList.size() > 0) {
                updateData(baseDto.getContent());
                viewModel.setHasData(true);
            } else {
                viewModel.setHasData(false);
            }
            dismissLoading();
        });
    }

    private void updateData(JobReportDto content) {
        jobBeanList.clear();
        jobBeanList.addAll(content.jobList);
        if (jobBeanList == null || jobBeanList.size() == 0) return;

        count = jobBeanList.size();
        if (count > 10) {
            count = 10;
        }
//        count = 13;
        String labelYAxis = "";
        double[] arrayBar = new double[count];
        for (int i = 0; i < count; i++) {
            switch (selectSortIndex) {
                case 0:
                    labelYAxis = "时间/h";
                    arrayBar[i] = TimeUtils.timeStamp2Hour(jobBeanList.get(i).workTime);
                    break;
                case 1:
                    labelYAxis = "面积/亩";
                    String formatArea = DataStatisticsUtil.covertDoubleFormat2(jobBeanList.get(i).area);
                    try {
                        arrayBar[i] = Double.parseDouble(formatArea);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    labelYAxis = "时间/h";
                    arrayBar[i] = TimeUtils.timeStamp2Hour(jobBeanList.get(i).runningTime);
                    break;
                case 3:
                    labelYAxis = "时间/h";
                    arrayBar[i] = TimeUtils.timeStamp2Hour(jobBeanList.get(i).offLineTime);
                    break;
                default:
                    break;
            }


        }

        try{
            setAxis(labelYAxis);
            setData(arrayBar);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAxis(String labelYAxis) throws Exception{
        List<String> xLabels = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xLabels.add(jobBeanList.get(i).name);
        }

        chart.getXAxis().setLabelCount(xLabels.size());  // 设置x轴上的标签个数
        // 转换要显示的标签文本，value值默认是int从0开始
        chart.getXAxis().setValueFormatter((value, axis) -> {
            int index = (int) value;
            if (index < 0 || index >= xLabels.size()) {
                return "";
            } else {
                return xLabels.get(index);
            }
        });

        chart.setExtraBottomOffset(2 * 9f); //设置X轴文本为2行
        chart.getXAxis().setTextSize(9f);
        chart.setXAxisRenderer(new CustomXAxisRender(chart.getViewPortHandler(), chart.getXAxis() , chart.getTransformer(YAxis.AxisDependency.LEFT)));


        // 设置y轴
        leftYAxisRender.setLabelAxis(labelYAxis, DisplayUtils.sp2px(11), Color.parseColor("#666666"), false);
        chart.setRendererLeftYAxis(leftYAxisRender);

        chart.invalidate();
    }

    /**
     * 设置图例
     */
    private void setLegend() {
        Legend l = chart.getLegend();
        l.setFormSize(DisplayUtils.px2dp(60f)); // 图例的图形大小
        l.setTextSize(10f); // 图例的文字大小
        l.setTextColor(Color.parseColor("#999999"));
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setXOffset(DisplayUtils.px2dp(-40));
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.LINE);
        float[] floats = {10, 5};
        l.setFormLineDashEffect(new DashPathEffect(floats, 0));
        l.setFormLineWidth(DisplayUtils.px2dp(4));
        l.setDrawInside(false);
    }

    private void setData(double[] arrayBar) throws Exception {
        ArrayList<BarEntry> values = new ArrayList<>();
        float totalValue = 0f;
        for (int i = 0; i < count; i++) {
            float val = (float) arrayBar[i];
            totalValue += val;
            values.add(new BarEntry(i, val));
        }
        barDataSet = new BarDataSet(values, getString(R.string.chart_bar_line_bar_tv_average));
        barDataSet.setColor(getResources().getColor(R.color.color_89C6C0)); // 柱子的颜色
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setDrawValues(isShowValues);
        barDataSet.setHighlightEnabled(false);
        barDataSet.setValueTextColor(getResources().getColor(R.color.lib_color_primary));
        barDataSet.setValueTextSize(10f);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f); // 设置柱子的宽度

        chart.setData(barData);

        refreshChart();

        //最大最小显示范围都设置为10，使得柱子宽度固定
        chart.setVisibleXRangeMaximum(DEFAULT_COUNT);
//        chart.setVisibleXRangeMinimum(DEFAULT_COUNT);
        if (count > 0) {
            int avgValue = Math.round(totalValue / count);
            showLimitLine(chart.getAxisLeft(), avgValue, avgValue + "");
        }

    }

    private void refreshChart() {
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void showLimitLine(YAxis yAxis, float avgValue, String label) {
        LimitLine ll = new LimitLine(avgValue, label);
        ll.enableDashedLine(15f, 10f, 0f);
        ll.setLineColor(Color.parseColor("#009688"));
        ll.setTextColor(Color.parseColor("#999999"));
        ll.setTextSize(10f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.setYOffset(DisplayUtils.px2dp(24f));
        ll.setXOffset(0f);
        yAxis.removeAllLimitLines(); //先清除原来的线，后面再加上，防止add方法重复绘制
        yAxis.addLimitLine(ll);
        chart.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    public void onSelectSortClick() {
        if (!singleBottomPopWin.isShowing()){
            Window window = Objects.requireNonNull(getActivity()).getWindow();
            singleBottomPopWin.show(binding.getRoot(), window, selectSortIndex, 2);
        }
    }

    @Override
    public void onSelectTimeClick() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            calendarRangeDialog.show(activity.getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onAddDevices() {
        //点击添加机具
        if (!dataDevicePopWin.isShowing()) {
            dataDevicePopWin.show(binding.getRoot(), Objects.requireNonNull(getActivity()).getWindow());
        }
        //请求数据
        viewModel.getDataDevices().observe(this, baseDto -> {
            if (baseDto.getContent() == null) {
                return;
            }
            List<DataDeviceDto> deviceDtos = baseDto.getContent();
            List<String> tableSnList = TableDataHelper.getDevicesSnList(jobBeanList);
            boolean hasSelected = false;
            for (DataDeviceDto dto : deviceDtos) {
//                if (snList != null && snList.contains(dto.sn)) {
//                    dto.checkType = 1;
//                }
                if (tableSnList != null && tableSnList.contains(dto.sn)) {
                    dto.checkType = 1;
                    hasSelected = true;
                }
                dto.brandAndModel = dto.productBrandMeaning + "-" + dto.productModel;
            }
            dataDevicePopWin.refreshData(hasSelected, baseDto.getContent());
        });
    }

    @Override
    public void onSingleBottomPopWinClick(int type, int index) {
        selectSortIndex = index;
        binding.tvTableDataSort.setText(sortList.get(index));
        requestData(startDate, endDate, snList, selectSortIndex);
        if (singleBottomPopWin.isShowing()) {
            singleBottomPopWin.dismiss();
        }
    }

    @Override
    public void onCalendarRangeFinish(@Nullable String start, @Nullable String end) {
        if (start != null && end != null) {
            String start2EndTime = start + " - " + end;
            binding.tvTimeSelect.setText(start2EndTime);
            startDate = start.replace(".", "-");
            endDate = end.replace(".", "-");
            requestData(startDate, endDate, snList, selectSortIndex);
        }
    }

    @Override
    public void onAddMachineListener(String snList) {
        this.snList = snList;
        requestData(startDate, endDate, snList, selectSortIndex);
    }


    private BarDataSet barDataSet;
    private boolean isShowValues = true;

    @Override
    public void onClick(View v) {
        isShowValues = !isShowValues;
        if (barDataSet != null) {
            barDataSet.setDrawValues(isShowValues);

            refreshChart();
        }
    }
}
