package com.game.helper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.droidlover.xrecyclerview.XRecyclerContentLayout;


public class XReloadableRecyclerContentLayout extends XRecyclerContentLayout {

    private OnReloadListener onReloadListener;

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(XReloadableRecyclerContentLayout reloadableFrameLayout);
    }

    public XReloadableRecyclerContentLayout(Context context) {
        this(context, null);
    }

    public XReloadableRecyclerContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

//        if (getLoadingView() != null) {
//            setDisplayState(STATE_LOADING);
//        } else {
        setDisplayState(STATE_CONTENT);
//        }
    }


    @Override
    public void showError() {
        if (null != getErrorView()) {
            getErrorView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableRecyclerContentLayout.this.onReloadListener != null) {
                        showLoading();
                        XReloadableRecyclerContentLayout.this.onReloadListener.onReload(XReloadableRecyclerContentLayout.this);
                    }
                }
            });
        }
        super.showError();
    }

    @Override
    public void showEmpty() {
        if (null != getEmptyView()) {
            getEmptyView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableRecyclerContentLayout.this.onReloadListener != null) {
                        showLoading();
                        XReloadableRecyclerContentLayout.this.onReloadListener.onReload(XReloadableRecyclerContentLayout.this);
                    }
                }
            });
        }
        super.showEmpty();
    }

    /**
     * set a onReloadListener to listener reload button click event
     *
     * @param onReloadListener see #OnReloadListener
     */
    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

}
