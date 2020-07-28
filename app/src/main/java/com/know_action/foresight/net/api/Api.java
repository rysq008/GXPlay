package com.know_action.foresight.net.api;

import cn.droidlover.xdroidmvp.net.XApi;


public class Api {
    private static final String API_BASE_URL_RELEASE = "http://114.116.217.149:30088/";
    /**
     * test
     */
    public static final String API_BASE_URL_TEST = "https://www.android.com/debug/"; //测试地址


    //微信接口地址
    public static final String API_WEIXIN_URL = "https://api.weixin.qq.com/";

    public static String API_BASE_URL = API_BASE_URL_RELEASE;

    private static ApiService baseService;
    private static ApiService authService;
    private static ApiService DynamicService;
    public static final int URL_TYPE_WEIXIN = 1;
    public static int mCurrentType;

    public static void resetIp(String baseUrl) {
        API_BASE_URL = baseUrl;
        baseService = null;
    }

    public static boolean isOnlineHost() {
        return API_BASE_URL_RELEASE.equals(Api.API_BASE_URL);
    }

    public static ApiService CreateApiService() {
        if (baseService == null) {
            synchronized (Api.class) {
                if (baseService == null) {
                    baseService = XApi.getInstance().getRetrofit(/*BuildConfig.DEBUG ? API_BASE_URL_TEST :*/ API_BASE_URL, true).create(ApiService.class);
                }
            }
        }
        return baseService;
    }

    public static ApiService CreateDynamicApiService(String urlHost) {
        if (DynamicService == null) {
            DynamicService = XApi.get(urlHost, ApiService.class);
        }
        return DynamicService;
    }
}
