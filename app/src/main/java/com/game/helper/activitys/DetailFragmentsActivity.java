package com.game.helper.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.game.helper.activitys.BaseActivity.XDetailBaseActivity;

public class DetailFragmentsActivity extends XDetailBaseActivity {

    private static Fragment currentFragment;

    public interface OnKeyDownListener {
        boolean onKeyDown(int keyCode, KeyEvent keyEvent);
    }

    public static void launch(Context context, final Fragment fra) {
        Intent intent = new Intent();
        intent.setClass(context, DetailFragmentsActivity.class);
        context.startActivity(intent);
        currentFragment = fra;
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
