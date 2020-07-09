package com.zhny.library.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
   private static final SimpleDateFormat minuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);
   private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
   private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
   private static final SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);

   //获取当天的日期
   public static String getTodayStr(Date date) {
       return dataFormat.format(date);
   }

   //获取某日期的其几天
   public static String getDateStr(Date date, int index) {
       Calendar calendar = Calendar.getInstance();
       calendar.clear();
       calendar.setTime(date);
       calendar.add(Calendar.DATE, index);
       return dataFormat.format(calendar.getTime());
   }

    public static long convertSecond(String trackDate){
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        try {
            Date date = minuteFormat.parse(trackDate);
            hours = date.getHours();
            minutes = date.getMinutes();
            seconds = date.getSeconds();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hours * 3600 + minutes * 60 + seconds;
    }

    /**
     * 秒转换成时分
     */
    public static String getTimeMs(long progress) {
        if (progress < 3600){
            int minute = Math.round(progress / 60);
            return "00:".concat(minute < 10 ? "0" + minute : String.valueOf(minute));
        }
        int hour = Math.round(progress / 3600);
        int minute = Math.round((progress - (hour * 3600)) / 60);

        return  (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : String.valueOf(minute));
    }


    /**
     * 时间戳转换为x小时x分钟
     *
     * @param timeStamp 时间戳
     * @return          转换时间
     */
    public static String timeStamp2Hm(long timeStamp) {
        long second = timeStamp / 1000;
		if (second < 3600) return Math.round(second / 60) + "min";
		int hour = Math.round(second / 3600);
		int minute = Math.round((second - (hour * 3600)) / 60);
		return hour + "h " + (minute == 0 ? "" : minute + "min");
    }


    /**
     * 判断日期是否大于今天
     *
     * @param dateStr   dataStr
     * @return          result
     */
    public static boolean maxToday(String dateStr) {
        try {
            Date date = dataFormat.parse(dateStr);
            return date.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static int timeStamp2Hour(long timeStamp) {
         long second = timeStamp / 1000;
         return Math.round(second / 3600) ;
    }


    //获取今天0点的时间
    public static String getTodayZero() {
        String format = dataFormat.format(new Date());
        return format.concat(" ").concat("00:00:00");
    }

    public static String getCurTime() {
        return TimeUtils.format.format(new Date());
    }


    /**
     * 比较两个日期
     *
     * @return  sort
     */
    public static int compareTime(String dateStr1, String dateStr2) {
        try {
            Date date1 = format.parse(dateStr1);
            Date date2 = format.parse(dateStr2);
            return date1.before(date2) ? 1 : -1;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获取时间
    public static String getTime(long lastUpdateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        if (lastUpdateTime == 0) {
            calendar.setTimeInMillis(lastUpdateTime);
        }else {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        return timeFormat.format(calendar.getTime());
    }

    //判断日期是否是今日
    public static boolean isToday(String date) {
        String today = dataFormat.format(new Date());
        return TextUtils.equals(today, date);
    }
}
