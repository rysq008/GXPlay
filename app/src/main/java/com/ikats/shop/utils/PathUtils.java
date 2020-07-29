package com.ikats.shop.utils;

import android.os.Environment;

import com.ikats.shop.BuildConfig;


public class PathUtils {
    public static final String app_base_dir = BuildConfig.APPLICATION_ID;//"/com.innovationai.verify";
    public static final String app_cache_dir = getDataPath() + app_base_dir + "/data/cache";

    public static final String app_data_dir = getDataPath() + app_base_dir + "/data/";
    public static final String warningDealRecordImgPath = getDataPath() + app_base_dir + "/data/picture/dealRecord";


    public static String getDataPath() {

        return isHaveSDCard() ? Environment.getExternalStorageDirectory().getPath() : Environment.getDataDirectory().getPath();
    }

    /**
     * 判断是否存在SD卡
     *
     * @return true，有SD卡。false，无SD卡
     */
    public static boolean isHaveSDCard() {

        String SDState = Environment.getExternalStorageState();
        return SDState.equals(Environment.MEDIA_MOUNTED);
    }
}
