package com.zhny.library.presenter.playback.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhny.library.R;
import com.zhny.library.presenter.playback.model.ColorInfo;

import java.util.List;

/**
 * created by liming
 */
public class RangeSeekBar extends View {
    private static final String TAG = "RangeSeekBar";

    private int mWidth;
    private int mHeight;


    //-------------------------------------
    public static final int SCALE_MAX = 86400;
    private static final int SCALE_MIN = 0;//刻度最小值
    private static final int SCALE_MAX_LENGTH = 600;//刻度尺的长度
    private static final int EACH_SCALE_PIX = 8;//每个刻度值的像素
    private static final float SINGLE_SIZE = SCALE_MAX_LENGTH * EACH_SCALE_PIX * 1f / SCALE_MAX;
    private static final int mDrawTextSpace = 25;//画刻度值的间隔
    private int mSlidingMoveX = 0;//滑动的差值
    private boolean isRenderCenter;
    private int selectValue;
    private int mDownX, mDownY;
    private double totalX = 0;//滑动总距离
    private int mCenterX, mCenterY, mPreY;
    private SelectScaleListener selectScaleListener;//选择器监听
    private List<ColorInfo> colorList;
    private Paint centerTextPaint;//当前值画笔
    private Paint centerLinePaint;//指针画笔
    private TextPaint scaleTextPaint;//刻度值画笔
    private Paint scaleLinePaint;//刻度线画笔
    private Paint rectPaint;
    private Bitmap bitmap;
    private double currentValue = 0;// 当前刻度值
    private MyTimer myTimer;
    private static final long TIME_MISS = 10000; //8s消失
    //--------------------------------


    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        initPaint();
        myTimer = new MyTimer(TIME_MISS, 1000);
    }

    private void initPaint() {
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));

        scaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaleLinePaint.setColor(Color.WHITE);
        scaleLinePaint.setAntiAlias(true);
        scaleLinePaint.setStrokeCap(Paint.Cap.ROUND);
        scaleLinePaint.setStyle(Paint.Style.STROKE);

        scaleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        scaleTextPaint.setColor(Color.BLACK);
        scaleTextPaint.setTypeface(Typeface.SANS_SERIF);
        scaleTextPaint.setTextSize(26);
        scaleTextPaint.setStrokeWidth(2);
        scaleTextPaint.setTextAlign(Paint.Align.CENTER);

        centerLinePaint = new Paint();
        centerLinePaint.setAntiAlias(true);
        centerLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        centerLinePaint.setStrokeCap(Paint.Cap.ROUND);
        centerLinePaint.setColor(Color.parseColor("#009688"));
        centerLinePaint.setStrokeWidth(2);

        centerTextPaint = new Paint();
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setTextSize(30);
        centerTextPaint.setColor(Color.parseColor("#009688"));
        centerTextPaint.setTextAlign(Paint.Align.CENTER);

        //绘制三角符号
        bitmap = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_play_progress), 0.5f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mPreY = getMeasuredHeight() / 4;
        mCenterY = getMeasuredHeight() / 2;
        mCenterX = getMeasuredWidth() / 2;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //绘制直尺刻度尺选择器
        drawRectBg(canvas);
        drawCenterScale(canvas);
        drawNum(canvas);
    }

    //绘制背景进度矩形
    private void drawRectBg(Canvas canvas) {
        if (colorList == null || colorList.size() == 0) {
            rectPaint.setColor(getResources().getColor(R.color.grey));
            canvas.drawRect(0, mPreY, SCALE_MAX_LENGTH * EACH_SCALE_PIX, mCenterY, rectPaint);
        } else {
            rectPaint.setColor(Color.WHITE);
            canvas.drawRect(-mCenterX, mPreY, SCALE_MAX_LENGTH * EACH_SCALE_PIX + mCenterX, mCenterY, rectPaint);

            for (int i = 0; i < colorList.size(); i++) {
                rectPaint.setColor(colorList.get(i).colorInt);
                float left = (float) (totalX + SCALE_MAX_LENGTH * EACH_SCALE_PIX * colorList.get(i).from * 1f / SCALE_MAX);
                float to = (float) (totalX + SCALE_MAX_LENGTH * EACH_SCALE_PIX * colorList.get(i).to * 1f / SCALE_MAX);
                canvas.drawRect(left, mPreY, to, mCenterY, rectPaint);
            }
        }
    }


    //绘画数字
    private void drawNum(Canvas canvas) {
        for (int i = 0; i < mWidth; i++) {
            int bottom = mPreY + 72;
            int offsetY = 10;
            //绘制文字
            if (((int)(-totalX + i)) % (EACH_SCALE_PIX * mDrawTextSpace) == 0) {
                offsetY = -8;
                if ((-totalX + i) >= 0 && (-totalX + i) <= SCALE_MAX_LENGTH * EACH_SCALE_PIX) {
                    int hour = (int) (((-totalX + i) / EACH_SCALE_PIX + SCALE_MIN) / 25);
                    String hourStr = (hour < 10 ? "0" + hour : String.valueOf(hour)).concat(":00");
                    canvas.drawText(hourStr, i, bottom + 35, scaleTextPaint);
                }
            }
            //绘制刻度
            if (((int)(-totalX + i)) % EACH_SCALE_PIX == 0) {
                if ((-totalX + i) >= 0 && (-totalX + i) <= SCALE_MAX_LENGTH * EACH_SCALE_PIX) {
                    canvas.drawLine(i, mPreY + 35 + offsetY, i, bottom, scaleLinePaint);
                }
            }
        }
    }

    //绘画中间刻度
    private RectF roundRectF = new RectF();
    private void drawCenterScale(Canvas canvas) {
        currentValue = (mCenterX - totalX) * 1f / EACH_SCALE_PIX + SCALE_MIN;
        selectValue = (int) (currentValue * 144);

        //绘制中间的线
        roundRectF.left = mCenterX - 0.5f;
        roundRectF.right = mCenterX + 0.5f;
        roundRectF.top = mPreY - 15;
        roundRectF.bottom = mPreY + 80;
        canvas.drawRoundRect(roundRectF, 6, 6, centerLinePaint);

        if (isRenderCenter) {
            //绘制中间的文字
            int hour = (int) (currentValue / 25);
            int minute = (int) (currentValue % 25 * 60 / 25);
            String hourStr = hour < 10 ? ("0" + hour) : String.valueOf(hour);
            String minuteStr = (minute < 10 ? ("0" + minute) : String.valueOf(minute));
            canvas.drawText(hourStr + ":" + minuteStr, mCenterX, mPreY - 25, centerTextPaint);
            //绘制三角形
            canvas.drawBitmap(bitmap, mCenterX - bitmap.getWidth() / 2, mCenterY + mPreY + 5, centerTextPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDownY < mCenterY + mPreY) {
                    isRenderCenter = true;
                    myTimer.cancel();
//                    Log.d(TAG, "onTouchEvent: 拖动了进度条，拖动到某个值： " + selectValue);
                    myTimer.start();
                    mSlidingMoveX = (int) (event.getX() - mDownX);//滑动距离
                    totalX = totalX + mSlidingMoveX;
                    //向左滑动,刻度值增大
                    if (mSlidingMoveX < 0) {
                        if (-totalX + mCenterX > SCALE_MAX_LENGTH * EACH_SCALE_PIX) {
                            //向左滑动如果刻度值大于最大值，则不能滑动了
                            totalX = -SCALE_MAX_LENGTH * EACH_SCALE_PIX + mCenterX;
                            invalidate();
                            return true;
                        } else {
                            invalidate();
                        }
                    } else {
                        //向右滑动，刻度值减小  向右滑动刻度值小于最小值则不能滑动了
                        if (totalX - mCenterX > 0) {
                            totalX = mCenterX;
                            invalidate();
                            return true;
                        } else {
                            invalidate();
                        }
                    }
                    mDownX = (int) event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
                int mUpX = (int) event.getX();
                int mUpY = (int) event.getX();
                if (Math.abs(mUpX - mDownX) < 10 && mDownY > (mCenterY + mPreY) && mUpY > (mCenterY + mPreY)
                        &&  mUpX > mCenterX - bitmap.getWidth() * 3 && mUpX < mCenterX + bitmap.getWidth() * 3
                        && isRenderCenter && selectScaleListener != null) {
                    isRenderCenter = false;
                    myTimer.cancel();
                    selectScaleListener.onRangePlay();
                    this.moveProgress(selectValue);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public interface SelectScaleListener {
        /**
         * 刻度值自动变化
         */
        void onScaleChange(long progress);

        /**
         * 点击了中间三角形
         */
        void onRangePlay();
    }

    public void setSelectScaleListener(SelectScaleListener listener) {
        this.selectScaleListener = listener;
    }

    //设置颜色段
    public void setColorData(List<ColorInfo> colorList) {
        this.colorList = colorList;
        this.selectValue = 0;
        invalidate();
    }

    //滚动到某个刻度
    private long progress;
    public synchronized void moveProgress(long progress) {
        if (progress == SCALE_MAX) {
            isRenderCenter = false;
        }
        totalX -= SINGLE_SIZE;
        this.progress = progress;
        invalidate();
        selectScaleListener.onScaleChange(progress);
    }

    //滚动到某个值
    public synchronized void scrollerToProgress(int progress) {
        totalX = (int) (mCenterX - EACH_SCALE_PIX * 1f * (progress * 1f / 144 - SCALE_MIN));
        invalidate();
//        selectScaleListener.onScaleChange(progress);
    }

    //首次滚动
    public void firstMoveTo(long firstPosition) {
        this.progress = firstPosition;
        totalX = (int) (mCenterX - EACH_SCALE_PIX * 1f * (progress * 1f / 144 - SCALE_MIN));
        invalidate();
        selectScaleListener.onScaleChange(progress);
    }



    //获取当前值
    public synchronized int getProgress() {
        return (int) progress;
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


    //拖拽计时
    class MyTimer extends CountDownTimer {

        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            scrollerToProgress((int) progress);
            isRenderCenter = false;
        }
    }


}