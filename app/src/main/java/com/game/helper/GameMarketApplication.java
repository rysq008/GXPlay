package com.game.helper;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.facebook.stetho.Stetho;
import com.game.helper.net.api.Api;
import com.game.helper.utils.PersistentCookieStore;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.widget.TotoroToast;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.NetProvider;
import cn.droidlover.xdroidmvp.net.RequestHandler;
import cn.droidlover.xdroidmvp.net.XApi;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameMarketApplication extends MultiDexApplication {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initUmengShare();

        Stetho.initializeWithDefaults(this);
        SharedPreUtil.init(this);

        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
                Interceptor[] ins = new Interceptor[1];
                Interceptor in = new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Cookie", SharedPreUtil.getSessionId())
//                                .addHeader("imei", SystemUtil.getIMEI(context))
//                                .addHeader("os", SystemUtil.getAppVersionName(context))
                                .addHeader("plam", System.getProperty("os.name"))
                                .build();
                        return chain.proceed(request);
                    }
                };
                ins[0] = in;
                return ins;
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {

            }

            @Override
            public CookieJar configCookie() {
                return new CookieJar() {
                    //                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    private final PersistentCookieStore cookieStore = new PersistentCookieStore(context);

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        StringBuilder sb = new StringBuilder();
                        for (Cookie cookie : cookies) {
                            sb.append(cookie.toString());
                            sb.append(";");
                            if (cookie.name().equals("sessionid")) {
                                SharedPreUtil.saveSessionId(sb.toString());
                            }
                            cookieStore.add(url, cookie);
                        }
//                        cookieStore.put(url.host(), cookies);

                        SharedPreUtil.saveObject("Cookie", cookies);

                        String cookes = cookieHeader(cookies);
                        CookieSyncManager.createInstance(getApplicationContext());
                        CookieManager cm = CookieManager.getInstance();
                        cm.setAcceptCookie(true);
                        cm.removeSessionCookie();//移除    
                        cm.setCookie(Api.API_BASE_URL, cookes);
                        CookieSyncManager.getInstance().sync();

                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        //List<Cookie> cookies = SharedPreUtil.getObject("Cookie");
//                        List<Cookie> cookies = cookieStore.get(url.host());
                        List<Cookie> cookies = cookieStore.get(url);
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
                return 20000;
            }

            @Override
            public long configReadTimeoutMills() {
                return 20000;
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

    private void initUmengShare() {
        //查看log时候打开
        Config.DEBUG = true;
        UMShareAPI.get(this);
        //配置各个平台的id和secret
        PlatformConfig.setWeixin("wx1d5e45ad3dc2019a", "d33400dd7f4e358a435602e26d45e881");
        PlatformConfig.setQQZone("1105689325", "hMJbCLDB4eiTTTSy");
        PlatformConfig.setSinaWeibo("734669220", "4c643b2c952fd78d86902e007607e377", "https://api.weibo.com/oauth2/default.html");
    }

    /**
     * Returns a 'Cookie' HTTP request header with all cookies, like {@code a=b; c=d}.
     */
    private String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
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
