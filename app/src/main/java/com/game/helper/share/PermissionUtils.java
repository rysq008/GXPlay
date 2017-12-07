package com.game.helper.share;

import android.app.Activity;
import android.os.Build;


/**
 * Created by Administrator on 2017/12/7.
 */

public class PermissionUtils {

    public static final String TAG = "PermissionUtils";

    /**
     * 检测是否需要动态申请权限
     * @param activity
     */
    public static boolean PermissionCheck(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }else{
            return false;
        }
    }


}
