package com.zhny.library.presenter.playback.customer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhny.library.R;
import com.zhny.library.presenter.playback.model.ColorInfo;
import com.zhny.library.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatSeekBar;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-08 18:48
 */
@Deprecated
public class SeekBarView extends AppCompatSeekBar {

    private Paint timeDividerPaint;//底部时间文本
    private Paint textPaint;//实时进度文本
    private Paint rectPaint, thumbLinePaint, dividerPaint;

    private int size;

    //文字位置
    private Rect mProgressTextRect = new Rect();
    // 滑块按钮宽度
    private int offset = DisplayUtils.dp2px(getResources().getDimension(R.dimen.dp_20));

    private Bitmap bitmap;
    private List<ColorInfo> colorList = new ArrayList<>();
    private int maxDivider = 4;

    public SeekBarView(Context context) {
        super(context);
        init(context, null);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(DisplayUtils.sp2px(getResources().getDimension(R.dimen.sp_6)));
        textPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));


        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(getResources().getColor(R.color.grey));
        rectPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));


        thumbLinePaint = new Paint();
        thumbLinePaint.setAntiAlias(true);
        thumbLinePaint.setColor(Color.WHITE);
        thumbLinePaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));

        dividerPaint = new Paint();
        dividerPaint.setColor(Color.WHITE);
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));


        timeDividerPaint = new Paint();
        timeDividerPaint.setAntiAlias(true);
        timeDividerPaint.setTextSize(DisplayUtils.sp2px(getResources().getDimension(R.dimen.dp_6)));
        timeDividerPaint.setColor(getResources().getColor(R.color.grey));
        timeDividerPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));
//
//        // 如果不设置padding，当滑动到最左边或最右边时，滑块会显示不全
        setPadding(0, 0, 0, 0);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBarView);

        int thumpLineColor = typedArray.getColor(R.styleable.SeekBarView_thumpLineColor, getResources().getColor(R.color.color_009688));

        thumbLinePaint.setColor(thumpLineColor);

        typedArray.recycle();

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);


//        if(size == 0){
//            progressText =  "0%";
//        }else{
//            progressText = getProgress()*100/size +"%";
//        }


        int hour = getProgress() / 3600;//小时

        int minute = (getProgress() - hour * 3600) / 60;
        int second = (getProgress() - hour * 3600 - minute * 60);

        String hourStr = "00";
        String minuteStr = "00";

        if (String.valueOf(hour).length() == 1) {
            hourStr = "0" + String.valueOf(hour);
        } else {
            hourStr = String.valueOf(hour);
        }
        if (String.valueOf(minute).length() == 1) {
            minuteStr = "0" + String.valueOf(minute);
        } else {
            minuteStr = String.valueOf(minute);
        }
//        progressText = hourStr+":"+minuteStr+":"+second;
        String progressText = hourStr + ":" + minuteStr;

        //测量文字长度
        textPaint.getTextBounds(progressText, 0, progressText.length(), mProgressTextRect);


        //进度百分比
        float progressRatio = (float) getProgress() / getMax();

        //1.绘制背景进度矩形
        if (colorList.size() == 0) {
            canvas.drawRect(offset, getHeight() / 2, offset + (getWidth() - offset * 2) * 1, 3 * getHeight() / 4, rectPaint);

        } else {
            for (int i = 0; i < colorList.size(); i++) {
                rectPaint.setColor(colorList.get(i).colorInt);
                float left = offset + (getWidth() - offset * 2) * colorList.get(i).from * 1f / size;
                float to = offset + (getWidth() - offset * 2) * colorList.get(i).to * 1f / size;
                canvas.drawRect(left, getHeight() / 2,  to, 3 * getHeight() / 4, rectPaint);
            }
        }


        float progressTextX = (getWidth() - offset * 2) * progressRatio + offset - textPaint.measureText(progressText) / 2;
        float progressTextY = getHeight() / 2 - getResources().getDimension(R.dimen.dp_30);

        //2.****绘制进度文字
        canvas.drawText(progressText, progressTextX + getResources().getDimension(R.dimen.dp_35), progressTextY + getResources().getDimension(R.dimen.dp_10), textPaint);

        //3.绘制最上层箭头图标

        Bitmap bitmap = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_play_progress), 0.5f);
        canvas.drawBitmap(bitmap, (getWidth() - offset * 2) * progressRatio + offset - bitmap.getWidth() / 2, getHeight() / 4 - bitmap.getHeight(), textPaint);

        //4.当前进度刻度竖线
        canvas.drawLine(offset + (getWidth() - offset * 2) * progressRatio, getHeight() / 4, offset + (getWidth() - offset * 2) * progressRatio, 3 * getHeight() / 4, thumbLinePaint);

        //5.绘制底部线条
        canvas.drawLine(offset, 3 * getHeight() / 4, getWidth() - offset, 3 * getHeight() / 4, textPaint);

        //6.绘制刻度
        float startX = 0;
        float timeDividerWidth;
        String timeDivider = "";
        for (int i = 0; i < maxDivider + 1; i++) {
            startX = i * (getWidth() - offset * 2) / 4;
            if (i > 0 && i < maxDivider) {
                canvas.drawLine(offset + startX, 5 * getHeight() / 8, startX + offset, 3 * getHeight() / 4, dividerPaint);
            }

            timeDivider = ((i * 6) < 10 ? "0" + i * 6 : i * 6) + ":00";
            timeDividerWidth = textPaint.measureText(timeDivider);
            canvas.drawText(timeDivider, startX + offset - timeDividerWidth / 2, 3 * getHeight() / 4 + getResources().getDimension(R.dimen.dp_20), timeDividerPaint);
        }

    }


    /**
     * @param colorList  颜色段
     * @param maxDivider 几等份
     */
    public void setData(List<ColorInfo> colorList, int maxDivider, int size) {
        this.colorList = colorList;
        this.maxDivider = maxDivider;
        this.size = size;
        invalidate();

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int resultWidth = wSpecSize;
        int resultHeight = hSpecSize;

        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
            resultWidth = bitmap.getWidth() + getPaddingLeft() + getPaddingRight();
            resultHeight = bitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            resultWidth = bitmap.getWidth() + getPaddingLeft() + getPaddingRight();
            resultHeight = hSpecSize;
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            resultWidth = wSpecSize;
            resultHeight = bitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        }
        // 取Bitmap宽高和窗口宽高的较小的一个
        resultWidth = Math.min(resultWidth, wSpecSize);
        resultHeight = Math.min(resultHeight, hSpecSize);
        setMeasuredDimension(resultWidth, resultHeight);
    }

    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (colorList.size() == 0) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
