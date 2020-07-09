package com.zhny.library.presenter.data.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * created by liming
 */
public class DateUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

    /**
     * 获取日期
     */
    public static String getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        calendar.add(Calendar.MONTH, -1);
        return dateFormat.format(calendar.getTime());
    }

}
