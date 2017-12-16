package com.game.helper;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.facebook.stetho.Stetho;
import com.game.helper.data.RxConstant;
import com.game.helper.net.api.Api;
import com.game.helper.utils.SharedPreUtil;
import com.game.helper.views.widget.TotoroToast;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initWX();
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

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        final StringBuilder sb = new StringBuilder();
                        for (Cookie cookie : cookies) {
                            sb.append(cookie.toString());
                            sb.append(";");
                            if (cookie.name().equals("sessionid")) {
                                SharedPreUtil.saveSessionId(sb.toString());
                            }
                        }
//                        cookieStore.put(url.host(), cookies);
                        SharedPreUtil.saveObject(url.host(), cookies);

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
                        List<Cookie> cookies = SharedPreUtil.getObject(url.host());
//                        List<Cookie> cookies = cookieStore.get(url.host());
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

    /**
     * 初始化UMeng
     */
    private void initUmengShare() {
        //查看log时候打开
        Config.DEBUG = true;
        UMShareAPI.get(this);
        //配置各个平台的id和secret
        PlatformConfig.setWeixin(RxConstant.ThirdPartKey.WeixinId, RxConstant.ThirdPartKey.WeixinSecret);
        PlatformConfig.setQQZone(RxConstant.ThirdPartKey.QQId, RxConstant.ThirdPartKey.QQKey);
        PlatformConfig.setSinaWeibo(RxConstant.ThirdPartKey.SinaWeiboKey, RxConstant.ThirdPartKey.SinaWeiboSecret, RxConstant.ThirdPartKey.SinaWeiboRedirectUrl);
    }

    /**
     * 初始化微信支付SDK
     */
    private void initWX() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, RxConstant.ThirdPartKey.WeixinId);
        api.registerApp(RxConstant.ThirdPartKey.WeixinId);

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
