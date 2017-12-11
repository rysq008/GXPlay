package com.game.helper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.model.LoginUserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import java.util.List;

/**
 * Created by sung on 2017/11/20.
 */

public class Utils {
    private static String TAG = Utils.class.getSimpleName();

    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 关闭软键盘
     *
     * @param context
     */
    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null
                && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context)
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 弹出输入法
     *
     * @param context
     * @param view
     */
    public static void setEditFocusable(final Context context, final View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * 获取vip等级图标
     *
     * @param level 等级
     * */
    public static int getVipLevel(int level){
        switch (level){
            case 1:
                return R.mipmap.ic_member_vip1;
            case 2:
                return R.mipmap.ic_member_vip2;
            case 3:
                return R.mipmap.ic_member_vip3;
            case 4:
                return R.mipmap.ic_member_vip4;
            case 5:
                return R.mipmap.ic_member_vip5;
            default:
                return 0;
        }
    }

    /**
     * 游戏下载状态图标
     * */
    public static int getGameDownloadStatusIcon(int status){
        switch (status){
            case 0:
                return R.mipmap.bg_game_list_item_install;
            case 1:
                return R.mipmap.bg_game_list_item_waitting;
            case 2:
                return R.mipmap.bg_game_list_item_pause;
            case 3:
                return R.mipmap.bg_game_list_item_continue;
            case 4:
                return R.mipmap.bg_game_list_item_sussceeful;
            default:
                return R.mipmap.bg_game_list_item_install;
        }
    }

    /**
     * login状态
     * */
    public static void writeLoginInfo(Context context, LoginUserInfo userInfo){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RxConstant.LOGIN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        if (userInfo == null) {
            edit.putBoolean(RxConstant.LOGIN_PREFERENCE_KEY_STATUS, false);
        }else {
            edit.putBoolean(RxConstant.LOGIN_PREFERENCE_KEY_STATUS, true);
            Gson gson = new GsonBuilder().create();
            String user = gson.toJson(userInfo);
            edit.putString(RxConstant.LOGIN_PREFERENCE_KEY_USER, user);
            Log.e(TAG, "writeLoginInfo: "+user.toString() );
        }
        edit.commit();
    }

    public static void clearLoginInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RxConstant.LOGIN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.clear();
        Log.e(TAG, "clear login info! " );
        edit.commit();
    }

    public static LoginUserInfo getLoginInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RxConstant.LOGIN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(RxConstant.LOGIN_PREFERENCE_KEY_USER)){
            String string = mSharedPreferences.getString(RxConstant.LOGIN_PREFERENCE_KEY_USER, "");
            if (string.length() != 0){
                Gson gson = new GsonBuilder().create();
                LoginUserInfo userInfo = gson.fromJson(string, LoginUserInfo.class);
                return userInfo;
            }
        }
        return null;
    }

    public static boolean hasLoginInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RxConstant.LOGIN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(RxConstant.LOGIN_PREFERENCE_KEY_STATUS)){
            return mSharedPreferences.getBoolean(RxConstant.LOGIN_PREFERENCE_KEY_STATUS, false);
        }
        return false;
    }

    public static String emptyConverter(String str) {
        return TextUtils.isEmpty(str) || str.equals("null") ? "" : str;
    }

    public static JSONArray emptyConverter(List<String> list) {
        JSONArray jsonArray = new JSONArray(list);
        return jsonArray;
    }
}