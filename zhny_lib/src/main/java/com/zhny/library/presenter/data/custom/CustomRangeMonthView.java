package com.zhny.library.presenter.data.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.RangeMonthView;

/**
 * 范围选择月视图
 * Created by huanghaibin on 2018/9/13.
 */

public class CustomRangeMonthView extends RangeMonthView {

    private int mRadius;

    public CustomRangeMonthView(Context context) {
        super(context);
    }


    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 2 - 5;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme,
                                     boolean isSelectedPre, boolean isSelectedNext) {
        //copy一份mSelectedPaint。使得p用来绘制中间部分，mSelectedPaint绘制两端。
        Paint p = new Paint(mSelectedPaint);
        p.setColor(Color.parseColor("#89C6C0"));

        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        if (isSelectedPre) {
            if (isSelectedNext) {
                canvas.drawRect(x, cy - mRadius, x + mItemWidth, cy + mRadius, p);
            } else {//最后一个，the last
                canvas.drawRect(x, cy - mRadius, cx, cy + mRadius, p);
                canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
            }
        } else {
            if (isSelectedNext) {
                canvas.drawRect(cx, cy - mRadius, x + mItemWidth, cy + mRadius, p);
            }
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }

        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 8;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top - 5,
                    mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5,
                    mSelectedLunarTextPaint);


        } else {
            boolean isEnable = !onCalendarIntercept(calendar);
            //可用
            if (isEnable) {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top - 5,
                        mCurMonthTextPaint);
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5,
                        mCurMonthLunarTextPaint);
            } else {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top - 5,
                        mOtherMonthTextPaint);
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5,
                        mOtherMonthLunarTextPaint);
            }

        }
    }
}
