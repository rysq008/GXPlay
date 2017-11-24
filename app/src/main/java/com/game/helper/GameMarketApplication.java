package com.game.helper;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.widget.TotoroToast;

import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.NetProvider;
import cn.droidlover.xdroidmvp.net.RequestHandler;
import cn.droidlover.xdroidmvp.net.XApi;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class GameMarketApplication extends MultiDexApplication {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Stetho.initializeWithDefaults(this);
        SharedPreUtil.init(this);

        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
                return new Interceptor[0];
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {

            }

            @Override
            public CookieJar configCookie() {
                return null;
            }

            @Override
            public RequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutMills() {
                return 10000;
            }

            @Override
            public long configReadTimeoutMills() {
                return 10000;
            }

            @Override
            public boolean configLogEnable() {
                return true;
            }

            @Override
            public boolean handleError(NetError error) {
                if (error.getType() == NetError.AuthError) {
                    TotoroToast.makeText(getApplicationContext(), "goto login !!", 1).show();
                    return true;
                }
                return false;
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
