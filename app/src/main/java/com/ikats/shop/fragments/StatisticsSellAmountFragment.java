package com.ikats.shop.fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.MPPointF;
import com.ikats.shop.R;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.present.FStatisticsPresenter;
import com.ikats.shop.utils.DayAxisValueFormatter;
import com.ikats.shop.utils.MyValueFormatter;
import com.ikats.shop.utils.XYMarkerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class StatisticsSellAmountFragment extends XBaseFragment<FStatisticsPresenter> implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    @BindView(R.id.statis_sell_amount_barchar_layout)
    BarChart chart;
    @BindView(R.id.seekBar1)
    SeekBar seekBarX;
    @BindView(R.id.seekBar2)
    SeekBar seekBarY;
    @BindView(R.id.tvXMax)
    TextView tvX;
    @BindView(R.id.tvYMax)
    TextView tvY;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
    String start, end;
//    private Typeface tfLight;

    @Override
    public void initData(Bundle savedInstanceState) {
//        tfLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");

        start = getArguments().getString("stime", simpleDateFormat.format(new Date()));
        end = getArguments().getString("etime", simpleDateFormat.format(new Date()));
//        start = (simpleDateFormat.format(System.currentTimeMillis()));
//        Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
//        Calendar.getInstance().add(Calendar.MONTH, -3);
//        end = simpleDateFormat.format(Calendar.getInstance().getTime());
//        getP().requestStatisListData(context, start, end);

        seekBarY.setOnSeekBarChangeListener(this);
        seekBarX.setOnSeekBarChangeListener(this);
        chart.setOnChartValueSelectedListener(this);
        DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(start, end);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(xAxisFormatter.getTimes().size());
        xAxis.setValueFormatter(xAxisFormatter);

        ValueFormatter custom = new MyValueFormatter("$");

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(context, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        // setting data
        seekBarY.setProgress(50);
        seekBarX.setProgress(12);

//         chart.setDrawLegend(false);


        setData(start, end);
        chart.animateXY(1000, 1000);
    }

    private final RectF onValueSelectedRectF = new RectF();

    private void setData(String begin, String end) {
        String sstart = begin, send = end;
        ArrayList<BarEntry> values = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        try {
            Date sdate = simpleDateFormat.parse(sstart);
            Date edate = simpleDateFormat.parse(send);
            int n = 0;
            while (!sdate.after(edate)) {
                float val = (float) (Math.random() * (100 + 1));
                values.add(new BarEntry(n, val));
                cal.setTime(sdate);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                n++;
                sdate = cal.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BarDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "The year" + cal.get(Calendar.YEAR));
            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(context, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(context, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(context, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(context, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(context, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(context, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(context, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(context, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(context, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(context, android.R.color.holo_orange_dark);

            List<GradientColor> gradientFills = new ArrayList<>();
            gradientFills.add(new GradientColor(startColor1, endColor1));
            gradientFills.add(new GradientColor(startColor2, endColor2));
            gradientFills.add(new GradientColor(startColor3, endColor3));
            gradientFills.add(new GradientColor(startColor4, endColor4));
            gradientFills.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }


        chart.invalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (getUserVisibleHint() && null != chart) {//界面可见时
                chart.animateXY(1000, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FStatisticsPresenter newP() {
        return new FStatisticsPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_sell_amount_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public static StatisticsSellAmountFragment newInstance(String... params) {
        return new StatisticsSellAmountFragment(params);
    }

    public StatisticsSellAmountFragment(String... params) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("stime", params[0]);
        bundle.putCharSequence("etime", params[1]);
        StatisticsSellAmountFragment.this.setArguments(bundle);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        RectF bounds = onValueSelectedRectF;
        chart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = chart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + chart.getLowestVisibleX() + ", high: "
                        + chart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));
//        setData(seekBarX.getProgress(), seekBarY.getProgress());
        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
