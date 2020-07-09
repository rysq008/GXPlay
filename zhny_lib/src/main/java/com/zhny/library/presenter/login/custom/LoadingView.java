package com.zhny.library.presenter.login.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhny.library.R;

/**
 * created by liming
 */
public class LoadingView extends View {

    private int centerX;
    private int centerY;
    private Paint paint;
    private static final int mSegmentLength = 12;
    private int control = 1;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        int loadColor = a.getResourceId(R.styleable.LoadingView_load_color, Color.parseColor("#009688"));
        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(context.getResources().getColor(loadColor));
        paint.setStrokeWidth(4f);
    }


    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        centerX = mWidth / 2;
        centerY = mHeight / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 12; i++) {
            paint.setAlpha(((i + 1 + control) % 12 * 255) / 12);
            canvas.drawLine(centerX,
                    centerY - mSegmentLength,
                    centerX,
                    centerY - mSegmentLength * 2,
                    paint);
            canvas.rotate(30, centerX, centerY);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(12, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(animation -> {
            control = (int) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.start();
    }


}
