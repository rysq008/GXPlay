package com.game.helper.activitys.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.game.helper.R;
import com.jaeger.library.StatusBarUtil;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XBaseActivity<P extends IPresent> extends XActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_color));
    }

    private P p;

    @Override
    protected P getP() {
        if (p == null) {
            p = (P) newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }
}
