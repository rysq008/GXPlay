package com.game.helper.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.game.helper.activitys.BaseActivity.XDetailBaseActivity;

import cn.droidlover.xdroidmvp.router.Router;

public class DetailFragmentsActivity extends XDetailBaseActivity {

    private static Fragment currentFragment;

    public interface OnKeyDownListener {
        boolean onKeyDown(int keyCode, KeyEvent keyEvent);
    }

    public static void launch(Context context, Bundle bundle, final Fragment fra) {
        Router.newIntent((Activity) context).to(DetailFragmentsActivity.class).data(bundle).launch();
        currentFragment = fra;
        currentFragment.setArguments(bundle);
    }

    @Override
    public Fragment getStartFragment() {
        return currentFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (currentFragment instanceof OnKeyDownListener) {
            return ((OnKeyDownListener) currentFragment).onKeyDown(keyCode, keyEvent);
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentFragment = null;
    }
}
