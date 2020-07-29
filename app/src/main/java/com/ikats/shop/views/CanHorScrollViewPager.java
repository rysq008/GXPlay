package com.ikats.shop.views;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自动适应高度的ViewPager
 *
 * @author
 */
public class CanHorScrollViewPager extends ViewPager {

    public CanHorScrollViewPager(Context context) {
        super(context);
    }

    public CanHorScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof CanHorScrollViewPager && ((CanHorScrollViewPager) v).getCurrentItem() == 2) {
            return ((CanHorScrollViewPager) v).canScrollHorizontally(-dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
//        if (v instanceof iWebView) {
//            return ((iWebView) v).canScrollHor(-dx);
//        } else {
//            return super.canScroll(v, checkV, dx, x, y);
//        }
    }

}