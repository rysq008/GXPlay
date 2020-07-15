package com.zhny.zhny_app.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhny.zhny_app.activitys.BaseActivity.XDetailBaseActivity;

import cn.droidlover.xdroidmvp.router.Router;

public class DetailFragmentsActivity extends XDetailBaseActivity {
    public static final String TAG = "DetailFragmentsActivity";
    public static long PressTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static Fragment currentFragment;

    public interface OnKeyDownListener {
        boolean onKeyDown(int keyCode, KeyEvent keyEvent);
    }

    public static void launchForResult(Activity context, Fragment fra, int flag, Bundle bundle, int requestCode) {
        long NowTime = System.currentTimeMillis();
        if (NowTime - PressTime > 1000) {
            PressTime = NowTime;
            currentFragment = fra;
            currentFragment.setArguments(bundle);
            Router.newIntent(context).to(DetailFragmentsActivity.class).addFlags(flag).data(bundle).requestCode(requestCode).launch();
        }
    }

    public static void launchForResult(Fragment from, Fragment to, int flag, Bundle bundle, int requestCode) {
        long NowTime = System.currentTimeMillis();
        if (NowTime - PressTime > 1000) {
            PressTime = NowTime;
            currentFragment = to;
            currentFragment.setArguments(bundle);
            Intent it = new Intent(from.getActivity(), DetailFragmentsActivity.class);
            if (flag != 0)
                it.setFlags(flag);
            from.startActivityForResult(it, requestCode, bundle);
        }
    }

    public static void launch(Context context, Bundle bundle, final Fragment fra) {
        launch(context, bundle, 0, fra);
    }

    public static void launch(Context context, Bundle bundle, int flag, final Fragment fra) {
        long NowTime = System.currentTimeMillis();
        if (NowTime - PressTime > 1000) {
            PressTime = NowTime;
            Intent intent = new Intent(context, DetailFragmentsActivity.class);
            if (flag != 0)
                intent.addFlags(flag);
            currentFragment = fra;
            if (null != bundle)
                currentFragment.setArguments(bundle);
            context.startActivity(intent);
        }
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
    }

    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
