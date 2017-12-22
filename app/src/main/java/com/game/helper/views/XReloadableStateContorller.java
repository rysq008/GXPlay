package com.game.helper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.droidlover.xstatecontroller.XStateController;


public class XReloadableStateContorller extends XStateController {

    private OnReloadListener onReloadListener;

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(XReloadableStateContorller reloadableFrameLayout);
    }

    public XReloadableStateContorller(Context context) {
        this(context, null);
    }

    public XReloadableStateContorller(Context context, AttributeSet attrs) {
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
                    if (XReloadableStateContorller.this.onReloadListener != null) {
                        showLoading();
                        XReloadableStateContorller.this.onReloadListener.onReload(XReloadableStateContorller.this);
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
                    if (XReloadableStateContorller.this.onReloadListener != null) {
                        showLoading();
                        XReloadableStateContorller.this.onReloadListener.onReload(XReloadableStateContorller.this);
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
