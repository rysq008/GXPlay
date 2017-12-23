package com.game.helper.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.game.helper.activitys.BaseActivity.XDetailBaseActivity;
import com.jude.swipbackhelper.SwipeBackHelper;

public class DetailFragmentsActivity extends XDetailBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
                .setSwipeBackEnable(true);
    }

    private static Fragment currentFragment;

    public interface OnKeyDownListener {
        boolean onKeyDown(int keyCode, KeyEvent keyEvent);
    }

    public static void launch(Context context, Bundle bundle, final Fragment fra) {
//        Router.newIntent((Activity) context).to(DetailFragmentsActivity.class).data(bundle).launch();
        Intent intent = new Intent(context, DetailFragmentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentFragment = fra;
        currentFragment.setArguments(bundle);
        context.startActivity(intent);
    }

    @Override
    public Fragment getStartFragment() {
        return currentFragment;
    }

    public static Fragment getCurrentFragment() {
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
