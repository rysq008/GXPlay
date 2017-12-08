package com.game.helper.activitys.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.game.helper.R;
import com.jaeger.library.StatusBarUtil;

import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XDetailBaseActivity extends XActivity {
    public abstract Fragment getStartFragment();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_color));
        Fragment fragment = getStartFragment();
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment).commitAllowingStateLoss();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
