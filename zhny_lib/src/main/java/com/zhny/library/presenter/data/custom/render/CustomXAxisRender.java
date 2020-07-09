package com.zhny.library.presenter.data.custom.render;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zhny.library.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomXAxisRender extends XAxisRenderer {


    private static final int offset = DisplayUtils.dp2px(4);

    public CustomXAxisRender(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {

        List<String> lines = new ArrayList<>();
        if (formattedLabel.length() <= 3) {
            lines.add(formattedLabel);
        } else {
            String firstLine = formattedLabel.substring(0, 3);
            String secLine = formattedLabel.length() > 6 ?
                    formattedLabel.substring(3, 6) : formattedLabel.substring(3);

            lines.add(firstLine);
            lines.add(secLine);
        }
        for (int i = 0; i < lines.size(); i++) {
            float vOffset = Utils.calcTextHeight(mAxisLabelPaint, lines.get(i)) * i;
            if (i != 0) {
                vOffset += 8f;
            }
//            float vOffset = i * mAxisLabelPaint.getTextSize();
            Utils.drawXAxisValue(c, lines.get(i), x, y + vOffset, mAxisLabelPaint, anchor, angleDegrees);
        }
    }


    @Override
    public void renderAxisLine(Canvas c) {

        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
        mAxisLinePaint.setPathEffect(mXAxis.getAxisLineDashPathEffect());

        if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP
                || mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE
                || mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop(), mAxisLinePaint);
        }

        if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM
                || mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE
                || mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);

            //绘制末端箭头
            c.drawLine(mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight() - offset,
                    mViewPortHandler.contentBottom() - offset, mAxisLinePaint);

            c.drawLine(mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight() - offset,
                    mViewPortHandler.contentBottom() + offset, mAxisLinePaint);

        }
    }


}