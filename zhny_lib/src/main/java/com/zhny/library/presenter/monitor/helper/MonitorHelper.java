package com.zhny.library.presenter.monitor.helper;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.amap.api.maps.model.LatLng;
import com.zhny.library.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * created by liming
 */
public class MonitorHelper {

    private static BigDecimal COEFFICIENT = new BigDecimal("100000");

    private static final long MINUTE = 1000 * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;
    private static final long MONTH = DAY * 30;


    public static String  getPreTimeStr(Context context, long dateTimeStamp) {
        if (dateTimeStamp == 0) dateTimeStamp = System.currentTimeMillis();
        long diffValue = System.currentTimeMillis() - dateTimeStamp;
        if (diffValue < 0) {
            return context.getString(R.string.monitor_str_not_null);
        }
        long monthC = diffValue / MONTH;
        long weekC = diffValue / (7 * DAY);
        long dayC = diffValue / DAY;
        long hourC = diffValue / HOUR;
        long minC = diffValue / MINUTE;
        if (monthC >= 1) {
            return  monthC + context.getString(R.string.monitor_str_time_month);
        } else if (weekC >= 1) {
            return  weekC + context.getString(R.string.monitor_str_time_week);
        } else if (dayC >= 1) {
            return  dayC + context.getString(R.string.monitor_str_time_day);
        } else if (hourC >= 1) {
            return  hourC + context.getString(R.string.monitor_str_time_hour);
        } else if (minC >= 1) {
            return  minC + context.getString(R.string.monitor_str_time_minute);
        } else {
            return context.getString(R.string.monitor_str_time_just);
        }
    }


    /**
     * 解析点数据
     * @param coordinates   [[ [[111.14803936544962,41.527694],[111.15593130363298,41.528579]] ]]
     * @return  playPoints for list
     */
    public static List<LatLng> getLatLngs(String coordinates) {
        List<LatLng> data = new ArrayList<>();
        if (coordinates == null) return data;
        try {
            JSONArray jsonArray1 = JSON.parseArray(coordinates);
            for (Object o1 : jsonArray1) {
                JSONArray jsonArray2 = (JSONArray) o1;
                for (Object o2 :jsonArray2){
                    JSONArray jsonArray3 = (JSONArray) o2;
                    for (Object o3 : jsonArray3) {
                        JSONArray jsonArray4 = (JSONArray) o3;
                        LatLng latLng = new LatLng(
                                ((BigDecimal) jsonArray4.get(1)).doubleValue(),
                                ((BigDecimal) jsonArray4.get(0)).doubleValue());
                        data.add(latLng);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Deprecated
    public static List<Point> getPoints(String coordinates) {
        List<Point> points = new ArrayList<>();
        if (TextUtils.isEmpty(coordinates)) return points;
        try {
            JSONArray jsonArray1 = JSON.parseArray(coordinates);
            for (Object o1 : jsonArray1) {
                JSONArray jsonArray2 = (JSONArray) o1;
                for (Object o2 :jsonArray2){
                    JSONArray jsonArray3 = (JSONArray) o2;
                    for (Object o3 : jsonArray3) {
                        JSONArray jsonArray4 = (JSONArray) o3;
                        Point point = new Point();
                        point.x = COEFFICIENT.multiply((BigDecimal) jsonArray4.get(0)).intValue();
                        point.y = COEFFICIENT.multiply((BigDecimal) jsonArray4.get(1)).intValue();
                        points.add(point);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }


}
