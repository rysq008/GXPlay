package com.zhny.library.presenter.data.custom.render;

import android.graphics.Canvas;
import android.text.TextUtils;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhny.library.utils.DisplayUtils;

/**
 * created by liming
 */
public class CustomYAxisRender extends YAxisRenderer {

    private static final int offset = DisplayUtils.dp2px(4);

    private String labelAxis;
    private float labelTextSize;
    private int color;
    private boolean isRight;

    public void setLabelAxis(String labelAxis, float labelTextSize, int color, boolean isRight) {
        this.labelAxis = labelAxis;
        this.labelTextSize = labelTextSize;
        this.color = color;
        this.isRight = isRight;
    }

    public CustomYAxisRender(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        // draw
        for (int i = from; i < to; i++) {

            String text = mYAxis.getFormattedLabel(i);

            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }

        if (!TextUtils.isEmpty(labelAxis)) {
            mAxisLabelPaint.setColor(color);
            mAxisLabelPaint.setTextSize(labelTextSize);
            int labelHeight = Utils.calcTextHeight(mAxisLabelPaint, labelAxis);
            int labelWidth = Utils.calcTextWidth(mAxisLabelPaint, labelAxis);
            if (isRight) {
                c.drawText(labelAxis, fixedPosition - (labelWidth >> 1), labelHeight * 3 / 2, mAxisLabelPaint);
            } else {
                c.drawText(labelAxis, fixedPosition + (labelWidth >> 1), labelHeight * 3 / 2, mAxisLabelPaint);
            }
        }

    }


    @Override
    public void renderAxisLine(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;

        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            c.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);

            //绘制末端箭头
            c.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft() - offset,
                    mViewPortHandler.contentTop() + offset, mAxisLinePaint);

            c.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft() + offset,
                    mViewPortHandler.contentTop() + offset, mAxisLinePaint);

        } else {
            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);

            //绘制末端箭头
            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight() - offset,
                    mViewPortHandler.contentTop() + offset, mAxisLinePaint);

            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight() + offset,
                    mViewPortHandler.contentTop() + offset, mAxisLinePaint);

        }
    }


}
