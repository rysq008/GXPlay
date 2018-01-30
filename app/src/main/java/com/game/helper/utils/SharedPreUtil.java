package com.game.helper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.model.H5UrlListResults;
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

    public static final String H5_URL_MARKET = "market_url";
    public static final String H5_URL_VIP = "vip_url";
    public static final String H5_URL_ACCOUNT_GUIDE = "account_guide_url";
    public static final String EXPECTED_URL = "expected_url";
    public static final String SHARE_DISCOUNT_URL = "share_discount_url";
    public static final String TEMP_HUANXIN_NAME = "temp_hanxin_name";
    private static final String HUAN_XIN_GREETING_TEXT = "huan_xin_greeting_text";
    private static final String ALERT_DIALOG_ENTER = "enter_alert_dialog";
    private static final String H5_URL_RESULTS_LIST_GATHER = "h5_url_results_gather";

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

    public static String getCookies() {
        return sp.getString(KEY_COOKIES, "");
    }

    public static boolean saveCookies(String cookies) {
        return sp.edit().putString(KEY_COOKIES, cookies).commit();
    }

    public static void saveMac(String mac) {
        sp.edit().putString(KEY_MAC, mac).commit();
    }

    public static String getMac() {
        return sp.getString(KEY_MAC, "");
    }

    public static void saveSearchHistoryList(List list) {
        saveObject(SEARCH_HISTORY_LIST, list);
    }

    public static List<String> getSearchHistoryList() {
        return getObject(SEARCH_HISTORY_LIST);
    }


    /*****************************          h5 url list start                 ******************************/

    /**
     * 存
     */
    public static void saveH5Url(H5UrlListResults results) {
//        if (sp.contains(key)) {
//            return updateH5Url(key, url);
//        }
        saveObject(H5_URL_RESULTS_LIST_GATHER, results);
    }

    /**
     * 取
     */
    public static H5UrlListResults getH5url() {
        return getObject(H5_URL_RESULTS_LIST_GATHER);
    }

    /**
     * 更新
     */
//    public static boolean updateH5Url(String key, String url) {
//        sp.edit().remove(key).commit();
//        return saveH5Url(key, url);
//    }

    /*****************************          h5 url list end                 ******************************/


    /*****************************          login about start                 ******************************/

    public static boolean isAlertDialogEnter() {
        return sp.getBoolean(ALERT_DIALOG_ENTER, false);
    }

    public static void enterAlertDialog() {
        sp.edit().putBoolean(ALERT_DIALOG_ENTER, true).commit();
    }

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        String sessionid = getSessionId();
        return !TextUtils.isEmpty(sessionid);
    }

    /**
     * 获取登录用户
     */
    public static LoginUserInfo getLoginUserInfo() {
        return getObject(USER_INFO);
    }

    /**
     * 保存登录用户
     */
    public static void saveLoginUserInfo(LoginUserInfo loginUserInfo) {
        saveObject(USER_INFO, loginUserInfo);
        BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, loginUserInfo.icon));
    }

    /**
     * 清除登录
     */
    public static void cleanLoginUserInfo() {
        sp.edit().remove(USER_INFO).apply();
        BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, ""));
    }

    /**
     * session id
     */
    public static String getSessionId() {
        return sp.getString(SharedPreference_SessionId, "");
    }

    public static void saveSessionId(String sissonid) {
        sp.edit().putString(SharedPreference_SessionId, sissonid).apply();
    }

    public static void clearSessionId() {
        sp.edit().remove(SharedPreference_SessionId).apply();
    }

    /**
     * 更新登录账户密码设置状态
     */
    public static void updateUserPasswdStatus(Context context, boolean hasPasswd) {
        LoginUserInfo savedUser = getLoginUserInfo();
        savedUser.has_passwd = hasPasswd;
        cleanLoginUserInfo();
        saveLoginUserInfo(savedUser);
    }

    /**
     * 更新登录账户交易密码设置状态
     */
    public static void updateUserTradePasswdStatus(Context context, boolean hasTradePasswd) {
        LoginUserInfo savedUser = getLoginUserInfo();
        savedUser.has_trade_passwd = hasTradePasswd;
        cleanLoginUserInfo();
        saveLoginUserInfo(savedUser);
    }

    /**
     * 更新登录账户支付宝设置状态
     */
    public static void updateUserAlipayStatus(Context context, boolean hasAlipay) {
        LoginUserInfo savedUser = getLoginUserInfo();
        savedUser.has_alipay_account = hasAlipay;
        cleanLoginUserInfo();
        saveLoginUserInfo(savedUser);
    }

    /*****************************          login about end                 ******************************/


    /*****************************          public method start                 ******************************/

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

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /*****************************          public method end                 ******************************/


    /*****************************          huanxin about start                 ******************************/
    public static void saveRobot(String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(HUAN_XIN_GREETING_TEXT, value);
        editor.apply();
    }

    public static String getRobot(String defValue) {
        return sp.getString(HUAN_XIN_GREETING_TEXT, defValue);
    }


    /*****************************          huanxin about end                 ******************************/

}
