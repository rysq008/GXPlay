package com.zhny.zhny_app.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 *
 26
 * 简   述：<功能简述>
 */
public class ScrollViewpager extends ViewPager {
    private final GestureDetectorCompat detector;

    public ScrollViewpager(@NonNull Context context) {
        super(context);
        detector = new GestureDetectorCompat(context,(simpleOnGestureListener));
    }

    public ScrollViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        detector = new GestureDetectorCompat(context,(simpleOnGestureListener));
    }
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final float x = ev.getX();
//        final float y = ev.getY();
//
//        final int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mDownPosX = x;
//                mDownPosY = y;
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float deltaX = Math.abs(x - mDownPosX);
//                final float deltaY = Math.abs(y - mDownPosY);
//                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
//                if (deltaX > deltaY) {
//                    return false;
//                }
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(Math.abs(distanceX)>Math.abs(distanceY)&&getCurrentItem()==getChildCount()-1){
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if(getCurrentItem() == getChildCount())
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }
}
