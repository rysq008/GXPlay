package com.example.zr.gxapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SharedPreUtil {
    private static final String KEY_WX_TOKEN = "wxToken";
    private static final String KEY_WX_USER_INFO = "wxUserInfo";

    private static final String KEY_MAC = "mac";
    private static final String KEY_RESTART = "restart";
    private static final String KEY_FIRST_OPEN_APP = "first_open_app";
    private static final String KEY_LOGIN_INFO = "login_info";
    private static SharedPreferences sp;

    public static void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isFirstOpenApp() {
        return sp.getBoolean(KEY_FIRST_OPEN_APP, true);
    }

    public static boolean saveFirstOpenApp() {
        return sp.edit().putBoolean(KEY_FIRST_OPEN_APP, false).commit();
    }

    public static boolean isLogin() {
        String sessionid = sp.getString(KEY_LOGIN_INFO, "");
        return !TextUtils.isEmpty(sessionid);
    }

    public static boolean saveLoginStatus(String sessionid) {
        return sp.edit().putString(KEY_LOGIN_INFO, sessionid).commit();
    }

    public static void saveMac(String mac) {
        sp.edit().putString(KEY_MAC, mac).commit();
    }

    public static String getMac() {
        return sp.getString(KEY_MAC, "");
    }


    private static <T> String saveObject(String key, T t) {
        if (t == null) {
            return "";
        }
        String json = new Gson().toJson(t);
        try {
            sp.edit().putString(key, json).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private static <T> T getObject(String key, Class<T> c) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String encryptedJson = sp.getString(key, null);
        if (TextUtils.isEmpty(encryptedJson)) {
            return null;
        }
        try {
            return new Gson().fromJson(encryptedJson, c);
        } catch (Throwable t) {
            return null;
        }
    }

    private static <T> T getObject(String key, Type typeOfT) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String encryptedJson = sp.getString(key, null);
        if (TextUtils.isEmpty(encryptedJson)) {
            return null;
        }
        try {
            return new Gson().fromJson(encryptedJson, typeOfT);
        } catch (Throwable t) {
            return null;
        }
    }
}
