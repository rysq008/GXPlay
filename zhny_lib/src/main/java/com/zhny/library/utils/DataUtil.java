package com.zhny.library.utils;

/**
 * created by liming
 */
public class DataUtil {

    //获取两位小数
    public static String get2Point(double value) {
        return String.valueOf((double) Math.round(value * 100) / 100);
    }

    //获取1位小数
    public static String get1Point(double value) {
        return String.valueOf((double) Math.round(value * 10) / 10);
    }

}
