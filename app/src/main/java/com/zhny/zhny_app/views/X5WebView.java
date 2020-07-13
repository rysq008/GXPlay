package com.zhny.zhny_app.views;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;


//重定义webview 这里继承的是X5WebView
public class X5WebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public X5WebView(final Context context) {
        super(context);
    }

    public X5WebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public void super_onScrollChanged(int i, int i1, int i2, int i3) {
//        this.super_onScrollChanged(i, i1, i2, i3);
//        //X5WebView 父类屏蔽了 onScrollChanged 方法 要用该方法
//        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(i, i1);
//    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //普通webview用这个
        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
        void onScroll(int l, int t);
    }

    public boolean canScrollHor(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

}