package com.game.helper.net.api;

import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by wanglei on 2016/12/31.
 */

public class Api {
    public static final String API_BASE_URL = "http://60.205.204.218:8000";  // "http://gank.io/api/";（基础接口地址）
    public static final String API_PAY_OR_IMAGE_URL = "http://60.205.204.218:8080";  // 支付或者图片地址

    private static ApiService gankService;

    public static ApiService CreateApiService() {
        if (gankService == null) {
            synchronized (Api.class) {
                if (gankService == null) {
                    gankService = XApi.getInstance().getRetrofit(API_BASE_URL, true).create(ApiService.class);
                }
            }
        }
        return gankService;
    }
}