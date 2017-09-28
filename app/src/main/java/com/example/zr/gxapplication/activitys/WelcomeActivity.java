package com.example.zr.gxapplication.activitys;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.zr.gxapplication.R;
import com.example.zr.gxapplication.utils.SharedPreUtil;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

public class WelcomeActivity extends XActivity {


    @Override
    public void initData(Bundle savedInstanceState) {
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (SharedPreUtil.isFirstOpenApp()) {
                    SharedPreUtil.saveFirstOpenApp();
                    Router.newIntent(WelcomeActivity.this)
                            .to(GuideActivity.class)
                            .data(new Bundle())
                            .launch();
                } else {
                    Router.newIntent(WelcomeActivity.this)
                            .to(MainActivity.class)
                            .data(new Bundle())
                            .launch();
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public Object newP() {
        return null;
    }
}
