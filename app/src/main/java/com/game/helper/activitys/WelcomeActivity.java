package com.game.helper.activitys;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.game.helper.R;
import com.game.helper.activitys.BaseActivity.XBaseActivity;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.H5UrlListResults;
import com.game.helper.net.DataService;
import com.game.helper.utils.RxLoadingUtils;
import com.game.helper.utils.SharedPreUtil;
import com.jude.swipbackhelper.SwipeBackHelper;

import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends XBaseActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();


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
                WelcomeActivity.this.finish();
            }
        };
        countDownTimer.start();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
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
