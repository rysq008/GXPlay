package com.zhny.zhny_app.activitys.BaseActivity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jaeger.library.StatusBarUtil;
import com.zhny.zhny_app.R;

import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XDetailBaseActivity extends XActivity {
    private String TAG = XDetailBaseActivity.class.getName();

    public abstract Fragment getStartFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));

        super.onCreate(savedInstanceState);
        Fragment fragment = getStartFragment();
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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

    @Override
    public void onBackPressed() {
        if(null != backPressListner){
            backPressListner.onBackPressListener(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public interface BackPressListner{
       boolean onBackPressListener(Activity activity);
    }

    public void setBackPressListner(BackPressListner backPressListner) {
        this.backPressListner = backPressListner;
    }

    BackPressListner backPressListner;
}
