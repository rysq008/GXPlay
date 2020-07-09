package com.zhny.library.presenter.data.custom.detail;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.zhny.library.utils.DisplayUtils;

import java.util.Date;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class ColorfulMonthView extends MonthView {

    private int mRadius;

    private Paint todayPaint = new Paint();

    public ColorfulMonthView(Context context) {
        super(context);

//        //兼容硬件加速无效的代码
//        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
//        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint);
        mSchemePaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        todayPaint.setColor(0xFF009688);
        todayPaint.setAntiAlias(true);
        todayPaint.setStyle(Paint.Style.STROKE);
        todayPaint.setStrokeWidth(DisplayUtils.dp2px(1));
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return false 则不绘制onDrawScheme，因为这里背景色是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
//        boolean isEnable = !isIntercept(calendar);
        boolean isEnable = !onCalendarIntercept(calendar);
        if (isEnable) {
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;

        int top = y - mItemHeight / 8;


        if (isToday(calendar)) {
            canvas.drawCircle(cx, cy, mRadius, todayPaint);
        }

//        boolean isEnable = !isIntercept(calendar);
         boolean isEnable = !onCalendarIntercept(calendar);
        if (isEnable) {
            if (isSelected) {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5, mSelectedLunarTextPaint);
            } else if (hasScheme) {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                        calendar.isCurrentDay() ? mCurDayTextPaint :
                                calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5, mSchemeLunarTextPaint);
            } else {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                        calendar.isCurrentDay() ? mCurDayTextPaint :
                                calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5,
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint : mCurMonthLunarTextPaint);
            }
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10 + 5,
                    mOtherMonthLunarTextPaint);
        }


    }

//    private boolean isIntercept(Calendar calendar) {
//        java.util.Calendar nowC = java.util.Calendar.getInstance();
//        nowC.setTime(new Date());
//        nowC.add(java.util.Calendar.MONTH, 1);
//        Calendar now = new Calendar();
//        now.setYear(nowC.get(java.util.Calendar.YEAR));
//        now.setMonth(nowC.get(java.util.Calendar.MONTH));
//        now.setDay(nowC.get(java.util.Calendar.DATE));
//        return calendar.compareTo(now) > 0;
//    }

    private boolean isToday(Calendar calendar) {
        java.util.Calendar nowC = java.util.Calendar.getInstance();
        nowC.setTime(new Date());
        nowC.add(java.util.Calendar.MONTH, 1);
        Calendar now = new Calendar();
        now.setYear(nowC.get(java.util.Calendar.YEAR));
        now.setMonth(nowC.get(java.util.Calendar.MONTH));
        now.setDay(nowC.get(java.util.Calendar.DATE));
        return calendar.compareTo(now) == 0;
    }


    @Override
    public void onClick(View v) {
        Calendar calendar = getIndex();
//        boolean isEnable = !isIntercept(calendar);
        boolean isEnable = !onCalendarIntercept(calendar);
        if (isEnable) {
            super.onClick(v);
        }
    }
}
