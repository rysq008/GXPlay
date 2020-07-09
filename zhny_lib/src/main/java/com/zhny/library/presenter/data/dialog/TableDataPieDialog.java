package com.zhny.library.presenter.data.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.zhny.library.R;
import com.zhny.library.databinding.DialogTableDataPieBinding;
import com.zhny.library.presenter.data.custom.render.CustomPieChartRender;
import com.zhny.library.presenter.data.model.vo.PieDataVo;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

/**
 * 数据饼图
 *
 * created by liming
 */
public class TableDataPieDialog extends AppCompatDialogFragment implements View.OnClickListener {


    private DialogTableDataPieBinding binding;
    private Window window;
    private PieChart pieChart;
    private PieDataVo pieDataVo;
    private String title;
    private List<Integer> colors = Arrays.asList(Color.parseColor("#009688"),
            Color.parseColor("#3F51B5"), Color.parseColor("#999999"));

    public void setParams(String title, PieDataVo pieDataVo) {
        this.pieDataVo = pieDataVo;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_table_data_pie, container, false);
        pieChart = binding.pcDialogTableData;
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rlCloseDialogTableDataPie.setOnClickListener(this);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);

        window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        initPieChart();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (title != null && title.length() > 12) {
            title = title.substring(0, 12) + "...";
        }
        binding.tvDialogTableDataTitle.setText(String.format("%s 明细", title));
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(pieDataVo.workingTime, TimeUtils.timeStamp2Hm(pieDataVo.workingTime)));
        entries.add(new PieEntry(pieDataVo.runningTime, TimeUtils.timeStamp2Hm(pieDataVo.runningTime)));
        entries.add(new PieEntry(pieDataVo.offlineTime, TimeUtils.timeStamp2Hm(pieDataVo.offlineTime)));
        refreshData(entries);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.white);
            window.setLayout(DisplayUtils.dp2px(331.2f), DisplayUtils.dp2px(331.2f));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) binding.unbind();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }


    private void initPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        int offset = DisplayUtils.dp2px(6);
        pieChart.setExtraOffsets(offset, offset, offset, offset);
        pieChart.setDragDecelerationFrictionCoef(0.95F);//拖动减速
        pieChart.setDrawHoleEnabled(false);
//        pieChart.setRotationEnabled(false);
        pieChart.setRotationAngle(90f);
        pieChart.setHighlightPerTapEnabled(true);
        List<String> topLabel = Arrays.asList("作业时间", "转运时间", "离线时间");
        CustomPieChartRender render = new CustomPieChartRender(pieChart, topLabel, colors,
                pieChart.getAnimator(), pieChart.getViewPortHandler());
        pieChart.setRenderer(render);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        // 输入标签样式
        pieChart.setEntryLabelColor(Color.parseColor("#009688"));
        pieChart.setEntryLabelTextSize(11f);
    }


    public void refreshData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSelectionShift(5f);

        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.6f);
        dataSet.setValueLineWidth(1.7f);
        dataSet.setValueLineColor(Color.parseColor("#009688"));

        //设置数据块颜色
        dataSet.setColors(colors);

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }


}
