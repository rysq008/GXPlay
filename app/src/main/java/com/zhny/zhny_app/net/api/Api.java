package com.zhny.zhny_app.net.api;

import cn.droidlover.xdroidmvp.net.XApi;


public class Api {
    private static final String IP = "http://129.204.70.220:1003";
    /**
     * 教员相关接口
     */
    public static final String API_BASE_URL_RELEASE = IP + "/teacher/";//"https://www.android.com/release/";
    /**
     * 生源相关接口
     */
    public static final String API_BASE_URL_STUDENT = IP + "/student/";
    /**
     * 管理相关接口
     */
    public static final String API_BASE_URL_ADMIN = IP + "/admin/";
    /**
     * 账号体系相关接口
     */
    public static final String API_BASE_URL_AUTH = IP + "/auth/";
    /**
     * 课程管理相关接口
     */
    public static final String API_BASE_URL_COURSE = IP + "/course/";
    /**
     * test
     */
    public static final String API_BASE_URL_TEST = "https://www.android.com/debug/"; //测试地址


    //微信接口地址
    public static final String API_WEIXIN_URL = "https://api.weixin.qq.com/";

    public static String API_BASE_URL = API_BASE_URL_RELEASE;

    private static ApiService baseService;
    private static ApiService adminService;
    private static ApiService authService;
    private static ApiService DynamicService;
    public static final int URL_TYPE_WEIXIN = 1;
    public static int mCurrentType;

    public enum HostType {
        /**
         * 使用默认API（教员）
         */
        RELEASE(0),
        /**
         * 使用管理API
         */
        ADMIN(1),
        /**
         * 使用鉴权API
         */
        AUTH(2),
        /**
         * 使用生源API（学生）
         */
        STUDENT(3),
        /**
         * 使用课程管理API
         */
        COURSE(4);

        HostType(int i) {
        }
    }

    ;

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

    public static ApiService CreateApiStudentService() {
        if (baseService == null) {
            synchronized (Api.class) {
                if (baseService == null) {
                    baseService = XApi.getInstance().getRetrofit(/*BuildConfig.DEBUG ? API_BASE_URL_TEST :*/ API_BASE_URL_STUDENT, true).create(ApiService.class);
                }
            }
        }
        return baseService;
    }

    public static ApiService CreateApiAdminService() {
        if (adminService == null) {
            synchronized (Api.class) {
                if (adminService == null) {
                    adminService = XApi.getInstance().getRetrofit(/*BuildConfig.DEBUG ? API_BASE_URL_TEST :*/ API_BASE_URL_ADMIN, true).create(ApiService.class);
                }
            }
        }
        return adminService;
    }

    public static ApiService CreateApiAuthService() {
        if (authService == null) {
            synchronized (Api.class) {
                if (authService == null) {
                    authService = XApi.getInstance().getRetrofit(/*BuildConfig.DEBUG ? API_BASE_URL_TEST :*/ API_BASE_URL_AUTH, true).create(ApiService.class);
                }
            }
        }
        return authService;
    }

    public static ApiService CreateDynamicApiService(int type, String urlHost) {
        if (mCurrentType > 0 && mCurrentType == type && DynamicService != null) {
            return DynamicService;
        }
        switch (type) {
            case URL_TYPE_WEIXIN:
                DynamicService = XApi.getInstance().getRetrofit(urlHost, true).create(ApiService.class);
                break;
            default:
                break;
        }

        return DynamicService;
    }

    public static ApiService CreateApiCourseService() {
        if (baseService == null) {
            synchronized (Api.class) {
                if (baseService == null) {
                    baseService = XApi.getInstance().getRetrofit(/*BuildConfig.DEBUG ? API_BASE_URL_TEST :*/ API_BASE_URL_COURSE, true).create(ApiService.class);
                }
            }
        }
        return baseService;
    }
}
