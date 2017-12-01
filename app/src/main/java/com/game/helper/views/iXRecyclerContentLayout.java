package com.game.helper.views;

import android.content.Context;
import android.util.AttributeSet;

import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;

import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import io.reactivex.functions.Consumer;

/**
 * Created by zr on 2017-10-13.
 */

class iXRecyclerContentLayout extends XRecyclerContentLayout {


    public iXRecyclerContentLayout(Context context) {
        super(context);
    }

    public iXRecyclerContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public iXRecyclerContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusProvider.getBus().receive(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            @Override
            public void accept(MsgEvent msgEvent) throws Exception {
                if (msgEvent.getData().equals("cancel_request")) {
                    refreshState(false);
                    getRecyclerView().setPage(0, 1);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getBus().unregister(this);
    }
}
