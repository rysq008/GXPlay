package com.game.helper.utils;


import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * 数字比较类
 */
public class NumberUtil {

    public static String Zero = "0.0";

    public static int compare(String arg1, String arg2) {

        if (TextUtils.isEmpty(arg1)) {
            arg1 = "0";
        }
        if (TextUtils.isEmpty(arg2)) {
            arg2 = "0";
        }
        try {
            return new BigDecimal(arg1).compareTo(new BigDecimal(arg2));
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }
}
