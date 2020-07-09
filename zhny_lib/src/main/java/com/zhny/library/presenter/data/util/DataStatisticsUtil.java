package com.zhny.library.presenter.data.util;


import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 *
 */
public class DataStatisticsUtil {


    /**
     * @param d 一个double类型的数
     * @return d 保留两位小数，转为String类型
     */
    public static String covertDoubleFormat2(double d) {
        String result;
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            result = df.format(d);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = "";
        }
        if (result != null) result = result.replace(".00", "0");
        return result;
    }


    public static String getTime(String time, int index) {
        String result;
        try {
            String[] splits = time.split(":");
            if (index == 1) {
                int temp = Integer.parseInt(splits[index + 1]);
                int res = temp >= 30 ? Integer.parseInt(splits[index]) + 1 : Integer.parseInt(splits[index]);
                result = (res > 60 ? 60 : res) + "";
            } else {
                result = Integer.parseInt(splits[index]) + "";

            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        if (TextUtils.isEmpty(result)) result = "0";
        return result;
    }

    public static int getMonth(String time) {
        int result = -1;
        try {
            String[] splits = time.split(":");
            result = Integer.parseInt(splits[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String dealEmptyString(String s, String replaceEmpty) {
        s = TextUtils.isEmpty(s) ? replaceEmpty : s;
        return s;
    }

    public static String getStandardYMD(String time) {
        String result;
        try {
            String year = time.substring(0, 4);
            String month = time.substring(4, 6);
            String day = time.substring(6, 8);
            result = year + "." + month + "." + day;
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }
}
