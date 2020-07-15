package com.zhny.zhny_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.CrashUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.zhny.zhny_app.activitys.DetailFragmentsActivity;
import com.zhny.zhny_app.fragments.LoginFragment;
import com.zhny.zhny_app.net.api.Api;
import com.zhny.zhny_app.utils.ShareUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.droidlover.xdroidmvp.XDroidConf;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.NetProvider;
import cn.droidlover.xdroidmvp.net.RequestHandler;
import cn.droidlover.xdroidmvp.net.XApi;
import io.objectbox.BoxStore;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class App extends MultiDexApplication {

    //crash日志收集
    private static WeakReference<Activity> mActivity;
    private static App mApp;
    public static int w;
    public static int h;
    private BoxStore boxStore;


    @Override
    public void onCreate() {
        super.onCreate();

        CrashUtils.init();


        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

        mApp = this;


        ShareUtils.init(this);
        Api.API_BASE_URL = ShareUtils.getHost("host");
        XLog.d("", "======================app===============");
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d("", "===================onActivityCreated " + activity);
                mActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });

        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
                Interceptor[] ins = new Interceptor[1];
                Interceptor in = chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Cookie", ShareUtils.getSessionId())
                            .addHeader("enId", ShareUtils.getString("enId", ""))
                            .addHeader("uId", ShareUtils.getString("uId", ""))
                            .addHeader("userId", ShareUtils.getLoginInfo().userId)
                            .addHeader("brand", Build.BRAND)
                            .addHeader("model", Build.MODEL)
                            .addHeader("systemversion", Build.VERSION.CODENAME)
                            .addHeader("sdkVersion", Build.VERSION.RELEASE)
                            .addHeader("token", ShareUtils.getString("token", ""))
                            .addHeader("lat", ShareUtils.getString("lat", ""))
                            .addHeader("lon", ShareUtils.getString("lon", ""))
                            .addHeader("plam", System.getProperty("os.name"))
                            .addHeader("token", ShareUtils.getLoginInfo().access_token)
                            .build();
                    return chain.proceed(request);
                };
                ins[0] = in;
                return ins;
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {
                builder.writeTimeout(20 * 10001, TimeUnit.MILLISECONDS);
            }

            @Override
            public boolean dispatchProgressEnable() {
                return false;
            }

            @Override
            public CookieJar configCookie() {
                return new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        final StringBuilder sb = new StringBuilder();
                        for (Cookie cookie : cookies) {
                            sb.append(cookie.toString());
                            sb.append(";");
                            if (cookie.name().equals("sessionid") && !Kits.Empty.check(cookie.value())) {
                                ShareUtils.saveSessionId(sb.toString());
                            }
                        }
                        cookieStore.put(url.host(), cookies);
//                        ShareUtils.saveObject(url.host(), cookies);
                        synCookies(getApp(), Api.API_BASE_URL, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        List<Cookie> cookies = ShareUtils.getObject(url.host());
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                };
            }

            @Override
            public RequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutMills() {
                return 200000;
            }

            @Override
            public long configReadTimeoutMills() {
                return 200000;
            }

            @Override
            public boolean configLogEnable() {
                return true;
            }

            @Override
            public boolean handleError(NetError error) {//重新登录
                Fragment fragment = ((DetailFragmentsActivity) getActivity()).getStartFragment();
                if (error.getType() == NetError.AuthError && null != fragment && !(fragment instanceof LoginFragment)) {
                    ShareUtils.clearLoginInfo();
                    ILFactory.getLoader().clearMemoryCache(mApp);
                    Executors.newSingleThreadExecutor().execute(() -> ILFactory.getLoader().clearDiskCache(mApp));
                    DetailFragmentsActivity.launch(getActivity(), null, Intent.FLAG_ACTIVITY_NEW_TASK, LoginFragment.newInstance());
                    return true;
                }
                return false;
            }
        });

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        w = size.x;
        h = size.y;

//        boxStore = MyObjectBox.builder().androidContext(this).build();
//        if (BuildConfig.DEBUG) {
//            new AndroidObjectBrowser(boxStore).start(this);
//        }

        XDroidConf.IL_ERROR_RES = R.drawable.ic_launcher_background;
        XDroidConf.IL_ERROR_RES = R.drawable.ic_launcher_background;
    }


    public static App getInstance() {
        return mApp;
    }

    public static void synCookies(Context context, String url, List<Cookie> cookies) {
        if (!TextUtils.isEmpty(url)) {
            if (!Kits.Empty.check(cookies)) {
                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();
                StringBuilder sbCookie = new StringBuilder();//创建一个拼接cookie的容器,
                for (Cookie cookie : cookies) {
                    sbCookie.append(cookie.name() + "=" + cookie.value());
                    sbCookie.append(";domain=" + cookie.domain());
                    sbCookie.append(";path=" + cookie.path());
                    String cookieValue = sbCookie.toString();
//                    cookieManager.setCookie(url, cookieValue);//为url设置cookie
                    cookieManager.setCookie(url, cookie.name() + "=" + cookie.value());//为url设置cookie
                    cookieManager.setCookie(url, "domain=" + cookie.domain());//为url设置cookie
                    cookieManager.setCookie(url, "path=" + cookie.path());//为url设置cookie
                }
                if (Build.VERSION.SDK_INT < 21) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }
                String newCookie = cookieManager.getCookie(url);
                Log.i("同步后cookie", newCookie);
            }
        }
    }

    public static Activity getActivity() {
        return mActivity.get();
    }

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
