package com.ikats.shop.activitys.BaseActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ikats.shop.App;
import com.ikats.shop.fragments.BaseFragment.XBaseFragment;
import com.ikats.shop.utils.ScanKeyManager;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.droidlover.xdroidmvp.XDroidConf;
import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XBaseActivity<P extends IPresent> extends XActivity<P> {

    private ScanKeyManager scanKeyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, App.getSettingBean().parseColor);
        //拦截扫码器回调,获取扫码内容
        scanKeyManager = new ScanKeyManager(value -> Log.e("bbb", "ScanValue:" + value));
        Log.e("bbb", "onCreate: ");
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e("bbb", "onPostCreate: ");
    }

    /*监听键盘事件,除了返回事件都将它拦截,使用我们自定义的拦截器处理该事件*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
//            scanKeyManager.analysisKeyEvent(event);
//            return true;
//        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("bbb", "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("bbb", "onPause: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("bbb", "onStart: ");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("bbb", "onRestart: ");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("bbb", "onPostResume: ");
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof XBaseFragment) {
            Log.e("bbb", "onBackPressed: XBaseActivity.java");
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

    @Override
    protected RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(XDroidConf.DEV);
        return rxPermissions;
    }
}
