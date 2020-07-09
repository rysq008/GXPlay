package com.zhny.library.presenter.alarm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AlarmUtil {

    /**
     * 根据毫秒时间戳来格式化字符串
     *
     * @param timeStamp 毫秒值
     * @return 当日显示：上午 9:00         下午 2:12
     * 昨日显示：昨日上午 9:00     昨日下午 2:12
     * 更早显示：日期+上下午+时间   2019年6月30日上午 9:00
     */
    public static String formatTime(long timeStamp) {
        String result = "";
        if (timeStamp < 0) return result;
        try {


            long curTimeMillis = System.currentTimeMillis();

            Calendar rightNow = Calendar.getInstance();
            int todayHoursSeconds = rightNow.get(Calendar.HOUR_OF_DAY) * 60 * 60;
            int todayMinutesSeconds = rightNow.get(Calendar.MINUTE) * 60;
            int todaySeconds = rightNow.get(Calendar.SECOND);
            int todayMillis = rightNow.get(Calendar.MILLISECOND);

            int todayTotalMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000 + todayMillis;
            long todayStartMillis = curTimeMillis - todayTotalMillis;
            int oneDayMillis = 24 * 60 * 60 * 1000;
            Locale locale = new Locale("zh", "ZN");


            Date date = new Date(timeStamp);
            //月份，日期不自动补0为M d,自动补0为MM dd.此处设置不自动补0
            SimpleDateFormat dfYMD = new SimpleDateFormat("yyyy年M月d日", locale);
            SimpleDateFormat dfHH = new SimpleDateFormat("HH", locale);
            SimpleDateFormat dfmm = new SimpleDateFormat("mm", locale);

            String hour = dfHH.format(date);
            String min = dfmm.format(date);


            int intHour = Integer.parseInt(hour);
            int intMin = Integer.parseInt(min);

            if (timeStamp >= todayStartMillis) {
                result += "";
            } else {
                long yesterdayStartMilis = todayStartMillis - oneDayMillis;
                if (timeStamp >= yesterdayStartMilis) {
                    result += "昨日";
                } else {
                    result += dfYMD.format(date);
                }
            }

            if (intHour >= 0 && intHour < 12) {
                result += "上午 " + intHour;
            } else if (intHour == 12) {
                result += "中午 " + intHour;
            } else {
                result += "下午 " + (intHour - 12);
            }
            result += ":" + min;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param date   日期字符串
     * @param format 日期字符串的格式
     * @return 时间戳 单位ms
     */
    public static long date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
