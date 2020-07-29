package com.ikats.shop.activitys.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ikats.shop.R;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.droidlover.xdroidmvp.XDroidConf;
import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XBaseActivity<P extends IPresent> extends XActivity<P> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        super.onCreate(savedInstanceState);
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, fragment).commitAllowingStateLoss();
            return;
        }
    }

    public Fragment getCurrentFragment() {
        return null;
    }

    ;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof XBaseFragment) {
            Log.e("aaa", "onBackPressed: XBaseActivity.java");
//            ((XBaseFragment) getStartFragment()).onKeyBackPressed(context);
            if (((XBaseFragment) getCurrentFragment()).onBackPress(context)) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public P newP() {
        return null;
    }

    public interface BackPressListner {
        boolean onBackPressListener(Activity activity);
    }

    public void setBackPressListner(BackPressListner backPressListner) {
        this.backPressListner = backPressListner;
    }

    BackPressListner backPressListner;

    @Override
    protected RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(XDroidConf.DEV);
        return rxPermissions;
    }
}
