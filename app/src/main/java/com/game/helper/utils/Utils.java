package com.game.helper.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.game.helper.GameMarketApplication;
import com.game.helper.R;
import com.game.helper.data.RxConstant;
import com.game.helper.model.LoginUserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            case 0:
                return R.mipmap.ic_member_vip1;
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
     * 获取邀请记录vip等级图标
     *
     * @param level 等级
     * */
    public static int getExtensionVipIcon(int level){
        switch (level){
            case 0:
                return R.mipmap.vip_0;
            case 1:
                return R.mipmap.vip_1;
            case 2:
                return R.mipmap.vip_2;
            case 3:
                return R.mipmap.vip_3;
            default:
                return R.mipmap.vip_0;
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
     * 写login状态
     *
     * @param userInfo bean
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

    /**
     * 清除login信息
     * */
    public static void clearLoginInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RxConstant.LOGIN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.clear();
        Log.e(TAG, "clear login info! " );
        edit.commit();
    }

    /**
     * 获取login信息
     * */
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

    /**
     * 登陆状态
     * */
    public static boolean hasLoginInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(RxConstant.LOGIN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(RxConstant.LOGIN_PREFERENCE_KEY_STATUS)){
            return mSharedPreferences.getBoolean(RxConstant.LOGIN_PREFERENCE_KEY_STATUS, false);
        }
        return false;
    }

    /**
     * 更新登陆账户密码设置状态
     * */
    public static void updateUserPasswdStatus(Context context, boolean hasPasswd){
        LoginUserInfo savedUser = Utils.getLoginInfo(context);
        savedUser.has_passwd = hasPasswd;
        Utils.clearLoginInfo(context);
        Utils.writeLoginInfo(context,savedUser);
    }

    /**
     * 更新登陆账户交易密码设置状态
     * */
    public static void updateUserTradePasswdStatus(Context context, boolean hasTradePasswd){
        LoginUserInfo savedUser = Utils.getLoginInfo(context);
        savedUser.has_trade_passwd = hasTradePasswd;
        Utils.clearLoginInfo(context);
        Utils.writeLoginInfo(context,savedUser);
    }

    /**
     * 更新登陆账户支付宝设置状态
     * */
    public static void updateUserAlipayStatus(Context context, boolean hasAlipay){
        LoginUserInfo savedUser = Utils.getLoginInfo(context);
        savedUser.has_alipay_account = hasAlipay;
        Utils.clearLoginInfo(context);
        Utils.writeLoginInfo(context,savedUser);
    }

    /**
     * 字符串判空
     * */
    public static String emptyConverter(String str) {
        return TextUtils.isEmpty(str) || str.equals("null") ? "" : str;
    }

    /**
     * list判空
     * */
    public static JSONArray emptyConverter(List<String> list) {
        JSONArray jsonArray = new JSONArray(list);
        return jsonArray;
    }

    /**
     * 隐藏手机号中间数字
     * */
    public static String converterSecretPhone(String phone){
        if (StringUtils.isEmpty(phone)) return "";
        String converterResult = phone.substring(0,3)+"****"+phone.substring(phone.length()-4,phone.length());
        return converterResult;
    }

    public static File getRootDir() {
        final File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), RxConstant.ROOT_DIR);
        } else {
            file = new File(GameMarketApplication.getInstance().getCacheDir(), RxConstant.ROOT_DIR);
        }
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (!success) {
                throw new IllegalStateException("Unable to create directory: " +
                        file.getAbsolutePath());
            }
        }
        return file;
    }

    /**
     * 判断字符串中包不包含特殊字符
     * */
    public static boolean hasRestrictionString(String string){
        String limitEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(string);

        if( m.find()){
            return true;
        }
        return false;
    }

    /**
     * 复制内容到剪贴板
     * */
    public static boolean copyToClipboard(Context context, String text){
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
        return true;
    }

    /**
     * 通过包名启动app
     * */
    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "无当前应用！", Toast.LENGTH_SHORT).show();
        }
        if (packageinfo == null) {
            Toast.makeText(context, "无当前应用！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }else {
            Toast.makeText(context, "无当前应用！", Toast.LENGTH_SHORT).show();
        }
    }
}
