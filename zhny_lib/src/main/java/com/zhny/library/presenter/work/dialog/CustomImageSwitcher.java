package com.zhny.library.presenter.work.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.zhny.library.utils.ImageLoaderUtil;

/**
 * created by liming
 */
public class CustomImageSwitcher extends ImageSwitcher implements View.OnTouchListener, GestureDetector.OnGestureListener{

    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int CLICK = 0;

    private static final float FLIP_DISTANCE = 50;

    private GestureDetector detector = new GestureDetector(getContext(), this);
    private LeftOrRightScrollListener listener;

    public CustomImageSwitcher(Context context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    public CustomImageSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        listener.onLeftOrRightScroll(CLICK);
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
        if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
            listener.onLeftOrRightScroll(LEFT); //向左滑
            return true;
        }
        if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
            listener.onLeftOrRightScroll(RIGHT); //向右滑
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    public void setImageUrl(String url) {
        ImageView image = (ImageView) this.getNextView();
        ImageLoaderUtil.loadImage(getContext(), url, image);
        showNext();
    }

    public interface LeftOrRightScrollListener{
        void onLeftOrRightScroll(int tag);
    }

    public void setOnScrollListener(LeftOrRightScrollListener listener) {
        this.listener = listener;
    }
}
