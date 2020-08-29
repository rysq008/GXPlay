package com.ikats.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ikats.shop.activitys.DetailFragmentsActivity;
import com.ikats.shop.database.MyObjectBox;
import com.ikats.shop.database.SkuTableEntiry;
import com.ikats.shop.database.VipTableEntiry;
import com.ikats.shop.fragments.LoginFragment;
import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.GoodsBean;
import com.ikats.shop.model.LoginBean;
import com.ikats.shop.model.ProvinceBean;
import com.ikats.shop.model.SettingBean;
import com.ikats.shop.net.DataService;
import com.ikats.shop.net.api.Api;
import com.ikats.shop.net.api.ApiService;
import com.ikats.shop.net.model.SkuDataRequestBody;
import com.ikats.shop.utils.RxLoadingUtils;
import com.ikats.shop.utils.ShareUtils;
import com.ikats.shop.utils.SocketClient;
import com.tamsiree.rxkit.RxTool;
import com.tencent.smtt.sdk.QbSdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
import ikidou.reflect.TypeToken;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
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
    private static BoxStore boxStore;
    public static ArrayMap<String, GoodsBean> products = new ArrayMap();
    public static List<ProvinceBean> provinceBeans = new ArrayList<>();
    private static SettingBean settingBean;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        RxTool.init(this);

        CrashUtils.init("");

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
//        // 在调用TBS初始化、创建WebView之前进行如下配置
//        HashMap map = new HashMap();
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
//        QbSdk.initTbsSettings(map);

        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
//        QbSdk.setDownloadWithoutWifi(true);//不是weifi也下载x5内核，但是感觉不起作用


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
                            .addHeader("userId", ShareUtils.getLoginInfo().userId)
                            .addHeader("brand", Build.BRAND)
                            .addHeader("model", Build.MODEL)
                            .addHeader("systemversion", Build.VERSION.CODENAME)
                            .addHeader("sdkVersion", Build.VERSION.RELEASE)
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
                return true;
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
                if (getActivity() instanceof DetailFragmentsActivity) {
                    Fragment fragment = ((DetailFragmentsActivity) getActivity()).getCurrentFragment();
                    if (error.getType() == NetError.AuthError && null != fragment && !(fragment instanceof LoginFragment)) {
                        ShareUtils.clearLoginInfo();
                        ILFactory.getLoader().clearMemoryCache(mApp);
                        Executors.newSingleThreadExecutor().execute(() -> ILFactory.getLoader().clearDiskCache(mApp));
                        DetailFragmentsActivity.launch(getActivity(), null, Intent.FLAG_ACTIVITY_NEW_TASK, LoginFragment.newInstance());
                        return true;
                    }
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

        boxStore = MyObjectBox.builder().androidContext(this).build();
//        if (BuildConfig.DEBUG) {
        new AndroidObjectBrowser(boxStore).start(this);
//        }

        XDroidConf.IL_ERROR_RES = R.drawable.ic_launcher_background;
        XDroidConf.IL_ERROR_RES = R.drawable.ic_launcher_background;

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String str = null;
//                StringReader stringReader = new StringReader(RxConstant.PRODUCTS);
//                BufferedReader bufferedReader = new BufferedReader(stringReader);
//                while ((str = bufferedReader.readLine()) != null) {
//                    String[] strs = str.split(",");
//                    GoodsBean goodsBean = new GoodsBean();
//                    goodsBean.productId = strs[0].trim();
//                    goodsBean.barcode = strs[1].trim();
//                    goodsBean.name = strs[2].trim();
//                    goodsBean.url = strs[3].trim();
//                    goodsBean.count = 0;
//
//                    Log.i("aaa", "products: --->" + goodsBean.barcode + "," + goodsBean.url + "," + goodsBean.name);
//                    products.put(goodsBean.barcode, goodsBean);
//                }
//                Log.i("aaa", "onCreate: --->" + products.size());
//                bufferedReader.close();
//                stringReader.close();
                Gson gson = new Gson();
                InputStream inputStream = getAssets().open("province_json.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                String province_json = sb.toString();
                provinceBeans = gson.fromJson(province_json, new TypeToken<List<ProvinceBean>>() {
                }.getType());
            } catch (Exception e) {

            }
        });
        boxStore.runInTxAsync(() -> {
            Box box = boxStore.boxFor(VipTableEntiry.class);
            box.removeAll();
            Random random = new Random();
            String[] names = {"小星星", "小海豚", "小可爱", "小甜甜"};
            String[] phones = {"13411112222", "13511112222", "13611112222", "13811112222"};
            for (int i = 0; i < 4; i++) {
                VipTableEntiry entiry = new VipTableEntiry();
                entiry.balance = 100 * i;
                entiry.name = names[i];
                entiry.phone = phones[i];
                entiry.level = "普通会员";
                entiry.integtal = 200;
                box.put(entiry);
            }
        }, (result, error) -> {
            if (error == null) {
                ToastUtils.showLong("vip box init data success !");
            }
        });
        settingBean = ShareUtils.getSettingInfo();
        SocketClient.initWebSocket(this, 1111);


        Flowable<HttpResultModel> f_token =
                DataService.builder().buildReqUrl(App.getSettingBean().manage_url+"login/getToken")
                        .buildReqParams("appKey", "POS")
                        .buildReqParams("security", "81014bf5f79050e6a85739320d8c6540")
                        .request(ApiService.HttpMethod.POST).flatMap((Function<HttpResultModel, Flowable<HttpResultModel<List<SkuTableEntiry>>>>) httpResultModel -> {
                    LoginBean loginBean = new LoginBean();
                    loginBean.access_token = (String) ((LinkedTreeMap) httpResultModel.resultData).get("token");
                    ShareUtils.saveLoginInfo(loginBean);

                    return DataService.builder().buildReqUrl(App.getSettingBean().manage_url+"ownersku/queryshopsku")
                            .builderRequestBody(new SkuDataRequestBody(App.getSettingBean().shop_code))
//                            .buildParseDataClass(S)
                            .buildParseDataType(new TypeToken<HttpResultModel<List<SkuTableEntiry>>>() {
                            }.getType())
                            .request(ApiService.HttpMethod.POST_JSON)
                            .map((Function<HttpResultModel<List<SkuTableEntiry>>, HttpResultModel>) listHttpResultModel -> {
//                                    ArrayMap<String, GoodsBean> products = new ArrayMap();
                                Box skuTableEntiry = boxStore.boxFor(SkuTableEntiry.class);
                                skuTableEntiry.removeAll();
                                if (listHttpResultModel.isSucceful()) {
                                    for (SkuTableEntiry tableEntiry : listHttpResultModel.resultData) {
                                        GoodsBean goodsBean = new GoodsBean();
                                        goodsBean.productId = tableEntiry.shopskucode;
                                        goodsBean.barcode = tableEntiry.shoppncode;
                                        goodsBean.name = tableEntiry.shopskuname;
                                        goodsBean.url = "";
                                        goodsBean.sell_price = Float.parseFloat(tableEntiry.shopskuprice);
                                        goodsBean.origin_price = Float.parseFloat(tableEntiry.shopskuprice);
                                        goodsBean.count = 0;

                                        Log.i("aaa", "products: --->" + goodsBean.barcode + "," + goodsBean.url + "," + goodsBean.name);
                                        products.put(goodsBean.barcode, goodsBean);
                                    }
                                    skuTableEntiry.put(listHttpResultModel.resultData);
                                }
                                HttpResultModel resultModel = new HttpResultModel();
                                resultModel.resultCode = 1;
                                resultModel.resultData = products;
                                return resultModel;
                            })
                            ;
                });
        RxLoadingUtils.subscribe(f_token, null, httpResultModel -> {
            if (httpResultModel.isSucceful()) {
                ToastUtils.showLong("fetch sku data success !");
            }
        }, netError -> ToastUtils.showLong(netError.getMessage()));
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

    public static BoxStore getBoxStore() {
        return boxStore;
    }

    public static App getApp() {
        return mApp;
    }

    public static SettingBean getSettingBean() {
        return settingBean;
    }

    public static void setSettingBean(SettingBean settingBean) {
        App.settingBean = settingBean;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
