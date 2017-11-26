package com.game.helper.fragments.BaseFragment;//package com.game.helper.fragments;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.mvp.XLazyFragment;

/**
 * Created by zr on 2017-10-13.
 */

public abstract class XBaseFragment<P extends IPresent> extends XLazyFragment {


    private P p;

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
