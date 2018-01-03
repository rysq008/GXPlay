package com.game.helper.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;

import java.io.File;
import java.text.DecimalFormat;
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
     */
    public static int getVipLevel(int level) {
        switch (level) {
            case 0:
                return R.mipmap.ic_member_vip0;
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
     * 获取不带外框vip等级图标
     *
     * @param level 等级
     */
    public static int getMineVipLevel(int level) {
        switch (level) {
            case 0:
                return R.mipmap.vip_0;
            case 1:
                return R.mipmap.vip_1;
            case 2:
                return R.mipmap.vip_2;
            case 3:
                return R.mipmap.vip_3;
        }
        return 0;
    }

    /**
     * 获取邀请记录vip等级图标
     *
     * @param level 等级
     */
    public static int getExtensionVipIcon(int level) {
        int res = 0;
        switch (level) {
            case 1:
                res = R.drawable.vip1_with_white_bg;
                break;
            case 2:
                res = R.drawable.vip2_with_white_bg;
                break;
            case 3:
                res = R.drawable.vip3_with_white_bg;
                break;
        }
        return res;
    }

    /**
     * 游戏下载状态图标
     */
    public static int getGameDownloadStatusIcon(int status) {
        switch (status) {
            case 0:
                return R.mipmap.bg_game_list_item_download;
            case 1:
                return R.mipmap.bg_game_list_item_waitting;
            case 2:
                return R.mipmap.bg_game_list_item_pause;
            case 3:
                return R.mipmap.bg_game_list_item_continue;
            case 4:
                return R.mipmap.bg_game_list_item_sussceeful;
            default:
                return R.mipmap.bg_game_list_item_download;
        }
    }

    /**
     * 字符串判空
     */
    public static String emptyConverter(String str) {
        return TextUtils.isEmpty(str) || str.equals("null") ? "" : str;
    }

    /**
     * list判空
     */
    public static JSONArray emptyConverter(List<String> list) {
        JSONArray jsonArray = new JSONArray(list);
        return jsonArray;
    }

    /**
     * 隐藏手机号中间数字
     */
    public static String converterSecretPhone(String phone) {
        if (StringUtils.isEmpty(phone)) return "";
        String converterResult = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
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
     */
    public static boolean hasRestrictionString(String string) {
        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(string);

        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 复制内容到剪贴板
     */
    public static boolean copyToClipboard(Context context, String text) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
        return true;
    }

    /**
     * 通过包名启动app
     */
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
        } else {
            Toast.makeText(context, "无当前应用！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判斷APP是否已安裝
     *
     * @param uri
     * @return
     */
    public static boolean isAppInstalled(Context context, String uri) {
        boolean installed;
        try {
            context.getPackageManager().getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * DecimalFormat转换最简便
     */
    public static float m2(float arg) {
        DecimalFormat df = new DecimalFormat("#.00");
        Log.e(TAG, "m2: " + arg + "/" + df.format(arg));
        return Float.parseFloat(df.format(arg));
    }
}
