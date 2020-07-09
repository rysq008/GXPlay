package com.zhny.library.presenter.data.custom.render;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * created by liming
 */
public class CustomPieChartRender extends PieChartRenderer {

    private List<String> topLabels;
    private List<Integer> colors;

    public CustomPieChartRender(PieChart chart, List<String> topLabels, List<Integer> colors,
                                ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        this.topLabels = topLabels;
        this.colors = colors;
    }


    private float ellipse(float angle) {
        float a;
        if (angle % 180f >= 0.0 && angle % 180 <= 90.0) a = (angle % 180f) / 2;
        else a = 90f - (angle % 180f) / 2;
        a = a * Utils.FDEG2RAD;
        if (angle * 360f >= 0.0 && angle * 360f <= 180.0)
            a = a * -1f;
        else
            a = a * 1f;
        return (float) Math.tan(a);
    }


    @Override
    public void drawValues(Canvas c) {
        MPPointF center = mChart.getCenterCircleBox();

        // get whole the radius
        float radius = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        final float holeRadiusPercent = mChart.getHoleRadius() / 100.f;
        float labelRadiusOffset = radius / 10f * 3.6f;

        if (mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;
        }

        final float labelRadius = radius - labelRadiusOffset;

        PieData data = mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();

        float yValueSum = data.getYValueSum();

        boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();

        float angle;
        int xIndex = 0;

        c.save();

        float offset = Utils.convertDpToPixel(5.f);

        for (int i = 0; i < dataSets.size(); i++) {

            IPieDataSet dataSet = dataSets.get(i);

            final boolean drawValues = dataSet.isDrawValuesEnabled();

            if (!drawValues && !drawEntryLabels)
                continue;

            final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
            final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            float lineHeight = Utils.calcTextHeight(mValuePaint, "Q")
                    + Utils.convertDpToPixel(4f);

            IValueFormatter formatter = dataSet.getValueFormatter();

            int entryCount = dataSet.getEntryCount();

            //设置线的颜色，此处无用， line 198 设置
//            mValueLinePaint.setColor(dataSet.getValueLineColor());
            mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));

            final float sliceSpace = getSliceSpace(dataSet);

            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

            for (int j = 0; j < entryCount; j++) {

                PieEntry entry = dataSet.getEntryForIndex(j);

                if (xIndex == 0)
                    angle = 0.f;
                else
                    angle = absoluteAngles[xIndex - 1] * phaseX;

                final float sliceAngle = drawAngles[xIndex];
                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);

                // offset needed to center the drawn text in the slice
                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;

                angle = angle + angleOffset;

                final float transformedAngle = rotationAngle + angle * phaseY;

                float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
                        / yValueSum * 100f : entry.getY();

                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);

                final boolean drawXOutside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawYOutside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawXInside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                final boolean drawYInside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;

                if (drawXOutside || drawYOutside) {

                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
                    final float valueLineLength2 = dataSet.getValueLinePart2Length();
                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;

                    float pt2x, pt2y;
                    float labelPtx, labelPty;

                    float line1Radius;

                    if (mChart.isDrawHoleEnabled())
                        line1Radius = (radius - (radius * holeRadiusPercent))
                                * valueLinePart1OffsetPercentage
                                + (radius * holeRadiusPercent);
                    else
                        line1Radius = radius * valueLinePart1OffsetPercentage;

                    final float polyline2Width = dataSet.isValueLineVariableLength()
                            ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(
                            transformedAngle * Utils.FDEG2RAD))
                            : labelRadius * valueLineLength2;

                    final float pt0x = line1Radius * sliceXBase + center.x;
                    final float pt0y = line1Radius * sliceYBase + center.y;

                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
                    //final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;
                    final float pt1y = (labelRadius * (1 + valueLineLength1 * ellipse(transformedAngle) * 1.5f) * sliceYBase + center.y);


                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {
                        pt2x = pt1x - polyline2Width;
                        pt2y = pt1y;

                        mValuePaint.setTextAlign(Paint.Align.RIGHT);

                        if (drawXOutside)
                            getPaintEntryLabels().setTextAlign(Paint.Align.RIGHT);

                        labelPtx = pt2x - offset;
                        labelPty = pt2y;
                    } else {
                        pt2x = pt1x + polyline2Width;
                        pt2y = pt1y;
                        mValuePaint.setTextAlign(Paint.Align.LEFT);

                        if (drawXOutside)
                            getPaintEntryLabels().setTextAlign(Paint.Align.LEFT);

                        labelPtx = pt2x + offset;
                        labelPty = pt2y;
                    }

                    //替换线的颜色
                    if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
                        if (value != 0) {
                            mValueLinePaint.setColor(colors.get(j));
                            c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
                            c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
                        }
                    }

                    // draw everything, depending on settings
                    if (drawXOutside && drawYOutside) {

                        drawValue(c,
                                formatter,
                                value,
                                entry,
                                0,
                                labelPtx,
                                labelPty,
                                dataSet.getValueTextColor(j));

                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(value, j, c, entry.getLabel(), labelPtx, labelPty + lineHeight);
                        }

                    } else if (drawXOutside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(value, j, c, entry.getLabel(), labelPtx, labelPty + lineHeight / 2.f);
                        }
                    } else if (drawYOutside) {

                        drawValue(c, formatter, value, entry, 0, labelPtx, labelPty + lineHeight / 2.f, dataSet
                                .getValueTextColor(j));
                    }
                }

                if (drawXInside || drawYInside) {
                    // calculate the text position
                    float x = labelRadius * sliceXBase + center.x;
                    float y = labelRadius * sliceYBase + center.y;

                    mValuePaint.setTextAlign(Paint.Align.CENTER);

                    // draw everything, depending on settings
                    if (drawXInside && drawYInside) {

                        drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));

                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(value, j, c, entry.getLabel(), x, y + lineHeight);
                        }

                    } else if (drawXInside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(value, j, c, entry.getLabel(), x, y + lineHeight / 2f);
                        }
                    } else {
                        drawValue(c, formatter, value, entry, 0, x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
                    }
                }


                xIndex++;
            }

            MPPointF.recycleInstance(iconsOffset);
        }
        MPPointF.recycleInstance(center);
        c.restore();
    }


    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        if (value != 0) {
            mValuePaint.setColor(color);
            c.drawText(formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler), x, y, mValuePaint);
        }
    }

    private void drawEntryLabel(float value, int index, Canvas c, String label, float x, float y) {
        if (value != 0) {
            Paint paint = getPaintEntryLabels();
            paint.setColor(colors.get(index));
            String topLabel = topLabels.get(index);
            //c.drawText(label, x, y, mEntryLabelsPaint);
            c.drawText(topLabel, x, y - Utils.calcTextHeight(paint, topLabel), paint);
            c.drawText(label, x, y /*+ (Utils.calcTextHeight(paint, label) >> 1)*/, paint);
        }
    }


}
