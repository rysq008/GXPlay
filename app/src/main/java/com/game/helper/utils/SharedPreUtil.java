package com.game.helper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.model.LoginUserInfo;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SharedPreUtil {
    private static final String KEY_WX_TOKEN = "wxToken";
    private static final String KEY_WX_USER_INFO = "wxUserInfo";

    private static final String KEY_MAC = "mac";
    private static final String KEY_RESTART = "restart";
    private static final String KEY_FIRST_OPEN_APP = "first_open_app";
    private static final String KEY_LOGIN_INFO = "login_info";
    private static final String SharedPreference_Name = "g9_android_myConfig_lbb";
    private static final String SharedPreference_SessionId = "sessionId";
    private static final String KEY_COOKIES = "cookies";
    private static final String USER_INFO = "user_info";
    private static final String SEARCH_HISTORY_LIST = "search_history_word";

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
        String sessionid = getSessionId();
        return !TextUtils.isEmpty(sessionid);
    }

    public static String getCookies() {
        return sp.getString(KEY_COOKIES, "");
    }

    public static boolean saveCookies(String cookies) {
        return sp.edit().putString(KEY_COOKIES, cookies).commit();
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

    public static LoginUserInfo getLoginUserInfo() {
        return getObject(USER_INFO);
    }

    public static void saveLoginUserInfo(LoginUserInfo loginUserInfo) {
        saveObject(USER_INFO, loginUserInfo);
        BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, loginUserInfo.icon));
    }

    public static void cleanLoginUserInfo() {
        sp.edit().remove(USER_INFO).apply();
        BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, ""));
    }

    public static void saveSearchHistoryList(List list){
        saveObject(SEARCH_HISTORY_LIST,list);
    }

    public static List<String> getSearchHistoryList(){
        return getObject(SEARCH_HISTORY_LIST);
    }

    //********************************************************************************************************************************************************************************//
    public static String getSessionId() {
        return sp.getString(SharedPreference_SessionId, "");
    }

    public static void saveSessionId(String sissonid) {
        sp.edit().putString(SharedPreference_SessionId, sissonid).apply();
    }

    public static void clearSessionId() {
        sp.edit().remove(SharedPreference_SessionId).apply();
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
//            String decryptedJson = des.decrypt(encryptedJson);
            return new Gson().fromJson(encryptedJson, c);
        } catch (Throwable t) {
            return null;
        }
    }

    public static void saveObject(String key, Object object) {
        if (null == object) return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oosw = new ObjectOutputStream(baos);
            oosw.writeObject(object);
//            String objstr = DESUtil.encrypt(baos.toString()/*new String(baos.toByteArray(), "utf-8")*/, DESUtil.DEFAULT_KEY);
            String objstr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            sp.edit().putString(key, objstr).commit();
            baos.close();
            oosw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(String key) {
        T t = null;
        String objstr = sp.getString(key, "");
        if (TextUtils.isEmpty(objstr)) {
            return t;
        }
        try {
//            byte[] bytes = DESUtil.decrypt(objstr, DESUtil.DEFAULT_KEY).getBytes();
            byte[] bytes = Base64.decode(objstr, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            t = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

}
