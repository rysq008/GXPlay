package com.ikats.shop.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.ikats.shop.R;

import cn.droidlover.xrecyclerview.XRecyclerContentLayout;


public class XReloadableListContentLayout extends XRecyclerContentLayout {
    private static final String TAG = "XReloadableRecycler";
    int lastX = -1;
    int lastY = -1;

    private OnReloadListener onReloadListener;

    public XReloadableListContentLayout(Context context) {
        this(context, null);
    }

    public XReloadableListContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XReloadableListContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnReloadListener {
        /**
         * user click reload button will invoke this method
         * when you reload success, use {@link #} to hide reload ui
         *
         * @param reloadableFrameLayout this
         */
        void onReload(XReloadableListContentLayout reloadableFrameLayout);
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
        if (null == getErrorView()) {
//            addView(View.inflate(getContext(), R.layout.view_error_state,null));
            errorView(View.inflate(getContext(), R.layout.view_error_state, null));
        }
        if (null == getEmptyView()) {
//            addView(View.inflate(getContext(), R.layout.view_empty_state,null));
            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
        }
        if (null == getLoadingView()) {
            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
        }
        //        for (int index = 0; index < getChildCount(); index++) {
//            getChildAt(index).setVisibility(GONE);
//        }
//        setDisplayState(STATE_CONTENT);
//        registerStateChangeListener(null);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getRecyclerView().useDefLoadMoreView();
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("bbb", "onInterceptTouchEvent: ");
        requestDisallowInterceptTouchEvent(interceptTouch);
        return super.onInterceptTouchEvent(ev);
    }

    public void setInterceptTouch(boolean interceptTouch) {
        this.interceptTouch = interceptTouch;
    }

    boolean interceptTouch;

//    @Override
//    public void setDisplayState(int displayState) {
//        super.setDisplayState(displayState);
//        XRecyclerAdapter adapter = getRecyclerView().getAdapter();
//        if (adapter != null && adapter.getItemCount() > 1) {
//            super.setDisplayState(STATE_CONTENT);
//            return;
//        }
//    }

    @Override
    public void showEmpty() {
        if (null != getEmptyView()) {
            getEmptyView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XReloadableListContentLayout.this.onReloadListener != null) {
//                        if (null == getLoadingView()) {
//                            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
//                        }
//                        if (null == getEmptyView()) {
//                            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
//                        }
                        showLoading();
                        XReloadableListContentLayout.this.onReloadListener.onReload(XReloadableListContentLayout.this);
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
                    if (XReloadableListContentLayout.this.onReloadListener != null) {
//                        if (null == getLoadingView()) {
//                            loadingView(View.inflate(getContext(), R.layout.view_loading, null));
//                        }
//                        if (null == getEmptyView()) {
//                            emptyView(View.inflate(getContext(), R.layout.view_empty_state, null));
//                        }
                        showLoading();
                        XReloadableListContentLayout.this.onReloadListener.onReload(XReloadableListContentLayout.this);
                    }
                }
            });
        }
        super.showError();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        int cur_state = getState();
        super.onRestoreInstanceState(state);
        setDisplayState(cur_state);
//        if (getState() != STATE_CONTENT)
//            showLoading();
    }

    @Override
    public OnStateChangeListener getStateChangeListener() {
        return super.getStateChangeListener();
    }

    @Override
    public void registerStateChangeListener(OnStateChangeListener stateChangeListener) {
        super.registerStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int oldState, int newState) {

            }

            @Override
            public void animationState(final View exitView, final View enterView) {
                AnimatorSet set = new AnimatorSet();
                final ObjectAnimator enter = ObjectAnimator.ofFloat(enterView, View.ALPHA, 1f);
                ObjectAnimator exit = ObjectAnimator.ofFloat(exitView, View.ALPHA, 0f);
                set.playTogether(enter, exit);
                set.setDuration(300);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        enterView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getLoadingView().setVisibility(GONE);
                        exitView.setAlpha(1);
                        exitView.setVisibility(GONE);
                        checkView(enterView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                set.start();
            }

            private void checkView(View enterView) {
                int visibleChild = 0;
                FrameLayout parent = (FrameLayout) enterView.getParent();
                int childCount = parent.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    if (parent.getChildAt(index).getVisibility() == VISIBLE) {
                        visibleChild++;
                    }
                }
                if (visibleChild < 1) {
                    enterView.setVisibility(VISIBLE);
                    enterView.setAlpha(1);
                }
            }
        });

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                Log.i(TAG, "dealtX:=" + dealtX);
                Log.i(TAG, "dealtY:=" + dealtY);
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) { // 左右滑动请求父 View 不要拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
