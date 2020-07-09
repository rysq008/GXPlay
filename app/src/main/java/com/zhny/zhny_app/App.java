package com.zhny.zhny_app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * created by liming
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        if (!Constant.IS_LIB_TOKEN && !LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
//        Utils.init(this);

    }

}
