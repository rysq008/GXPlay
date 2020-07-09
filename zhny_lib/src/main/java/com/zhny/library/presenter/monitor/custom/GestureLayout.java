package com.zhny.library.presenter.monitor.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * created by liming
 *
 * 可监听滑动的layout
 */
public class GestureLayout extends ConstraintLayout implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private static final float FLIP_DISTANCE = 50;
    private GestureDetector detector = new GestureDetector(this);
    private OnUpOrDownScrollListener listener;

    public GestureLayout(Context context) {
        super(context);
    }

    public GestureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
        this.setLongClickable(true);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
            listener.onUpOrDownScroll(1); //向上滑
            return true;
        }
        if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
            listener.onUpOrDownScroll(2); //向下滑
            return true;
        }
        if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
            listener.onUpOrDownScroll(3); //向左滑
            return true;
        }
        if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
            listener.onUpOrDownScroll(4); //向右滑
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    public interface OnUpOrDownScrollListener {
        void onUpOrDownScroll(int tag); //单击事件处理接口
    }

    public void setOnUpOrDownScrollListener(OnUpOrDownScrollListener listener) {
        this.listener = listener;
    }

}
