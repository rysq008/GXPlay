package com.game.helper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.game.helper.R;

import cn.droidlover.xrecyclerview.XRecyclerContentLayout;


public class XReloadableRecyclerContentLayout extends XRecyclerContentLayout {

    private OnReloadListener onReloadListener;

    public XReloadableRecyclerContentLayout(Context context) {
        this(context, null);
    }

    public XReloadableRecyclerContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XReloadableRecyclerContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(XReloadableRecyclerContentLayout reloadableFrameLayout);
    }

    /**
     * set a onReloadListener to listener reload button click event
     *
     * @param onReloadListener see #OnReloadListener
     */
    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (null == getLoadingView()) {
            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
        }
        if (null == getErrorView()) {
//            addView(View.inflate(getContext(), R.layout.view_error_state,null));
            errorView(View.inflate(getContext(), R.layout.view_error_state, null));
        }
        if (null == getEmptyView()) {
//            addView(View.inflate(getContext(), R.layout.view_empty_state,null));
            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
        }
//        for (int index = 0; index < getChildCount(); index++) {
//            getChildAt(index).setVisibility(GONE);
//        }
        setDisplayState(STATE_CONTENT);
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
}
