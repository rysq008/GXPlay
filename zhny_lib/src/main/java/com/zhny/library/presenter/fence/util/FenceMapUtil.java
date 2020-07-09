package com.zhny.library.presenter.fence.util;

import android.content.Context;
import android.graphics.PointF;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhengjiangrong on 2017/10/25.
 */

public class FenceMapUtil {

    //地图上点击多边形时，确定当前的业务类型，可加速地图的处理
    public static final String MAP_CHECK_IN_LAND_TYPE_EDITLAND = "editland";


    /**
     * 生成32位随机码b9b21c1c4828447ebe21b353b2dd3fb2
     * @return
     */
    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

    /**
     * 坐标转换成json
     *
     * @param latLng
     * @return
     */
    public Map toJson(LatLng latLng) {
        HashMap m = new HashMap();
        m.put("latitude", latLng.latitude);
        m.put("longitude", latLng.longitude);
        return m;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    public static boolean isPointInCircle(android.graphics.Point p, android.graphics.Point o, double r) {
        double x = o.x;
        double y = o.y;
        int px = p.x;
        int py = p.y;
        return !((x - px) * (x - px) + (y - py) * (y - py) > r * r);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    //***************点积判点是否在线段上***************
    int dblcmp(double a, double b) {
        if (Math.abs(a - b) <= 1E-6) return 0;
        if (a > b) return 1;
        else return -1;
    }

    //点积
    double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    //求a点是不是在线段bc上，>0不在，=0与端点重合，<0在。
    int point_on_line(PointF a, PointF b, PointF c) {
        return dblcmp(dot(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y), 0);
    }

    //**************************************************
    double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - x2 * y1;
    }

    //ab与ac的叉积
    double ab_cross_ac(PointF a, PointF b, PointF c) {
        return cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y);
    }

    //求ab是否与cd相交，交点为p。1规范相交，0交点是一线段的端点，-1不相交。
    public int ab_cross_cd(PointF a, PointF b, PointF c, PointF d) {
        double s1, s2, s3, s4;
        int d1, d2, d3, d4;
        PointF p = new PointF();
        d1 = dblcmp(s1 = ab_cross_ac(a, b, c), 0);
        d2 = dblcmp(s2 = ab_cross_ac(a, b, d), 0);
        d3 = dblcmp(s3 = ab_cross_ac(c, d, a), 0);
        d4 = dblcmp(s4 = ab_cross_ac(c, d, b), 0);

        //如果规范相交则求交点
        if ((d1 ^ d2) == -2 && (d3 ^ d4) == -2) {
            p.x = (float) ((c.x * s2 - d.x * s1) / (s2 - s1));
            p.y = (float) ((c.y * s2 - d.y * s1) / (s2 - s1));
//            Log.e("规范相交", p.toString());
            return 1;
        }

        //如果不规范相交
        if (d1 == 0 && point_on_line(c, a, b) <= 0) {
            return 0;
        }
        if (d2 == 0 && point_on_line(d, a, b) <= 0) {
            return 0;
        }
        if (d3 == 0 && point_on_line(a, c, d) <= 0) {
            return 0;
        }
        if (d4 == 0 && point_on_line(b, c, d) <= 0) {
            return 0;
        }
        //如果不相交
        return -1;
    }


    public static double getAreaNew(List<LatLng> points) {
       return AMapUtils.calculateArea(points);
    }

    public static ArrayList<LatLng> listToArrayList(List<LatLng> latLngs) {
        ArrayList arrayList = new ArrayList();
        for(LatLng latLng:latLngs){
            arrayList.add(latLng);
        }
        return arrayList;
    }

    /**
     * 删除数组集合里最后面和第一个坐标一样的数据
     *
     * @param latLngs
     * @return
     */
    public static ArrayList<LatLng> deletePointions(ArrayList<LatLng> latLngs) {
        if (latLngs == null) return null;
        int size = latLngs.size();
        if (size < 2) return latLngs;
        LatLng firstPoint = latLngs.get(0);
        LatLng lastPoint = latLngs.get(size - 1);
        if (firstPoint != null && lastPoint != null && firstPoint.latitude == lastPoint.latitude && firstPoint.longitude == lastPoint.longitude) {
            return deletePointions(listToArrayList(latLngs.subList(0, size - 1)));
        }
        return latLngs;
    }

    /**
     * 计算多边形面积
     *
     * @param points 多边形的顶点经纬度集合
     * @return double     面积
     */
    public static double getArea(List<LatLng> points) {
        List<LatLng> pts = deletePointions(listToArrayList(points));
        int count = pts.size();
        //判断数组的长度，如果是小于3的话，不构成面，无法计算面积
        if (count < 3) {
            return 0;
        }
//        return computeArea(convertToGpsBean(points));
        return getAreaNew(points);

/*
        if(points.get(0).latitude == points.get(points.size()-1).latitude && points.get(0).longitude == points.get(points.size()-1).longitude){
            count--;//如何list第一个点与最后一个点重合，去掉最后一个点，否则计算出错
        }*/

//        double totalArea = 0;// 初始化总面积
//        double LowX = 0.0;
//        double LowY = 0.0;
//        double MiddleX = 0.0;
//        double MiddleY = 0.0;
//        double HighX = 0.0;
//        double HighY = 0.0;
//        double AM = 0.0;
//        double BM = 0.0;
//        double CM = 0.0;
//        double AL = 0.0;
//        double BL = 0.0;
//        double CL = 0.0;
//        double AH = 0.0;
//        double BH = 0.0;
//        double CH = 0.0;
//        double CoefficientL = 0.0;
//        double CoefficientH = 0.0;
//        double ALtangent = 0.0;
//        double BLtangent = 0.0;
//        double CLtangent = 0.0;
//        double AHtangent = 0.0;
//        double BHtangent = 0.0;
//        double CHtangent = 0.0;
//        double ANormalLine = 0.0;
//        double BNormalLine = 0.0;
//        double CNormalLine = 0.0;
//        double OrientationValue = 0.0;
//        double AngleCos = 0.0;
//        double Sum1 = 0.0;
//        double Sum2 = 0.0;
//        double Count2 = 0;
//        double Count1 = 0;
//        double Sum = 0.0;
//        double Radius = 6378137.0;// ,WGS84椭球半径
//
//        for (int i = 0; i < count; i++) {
//            if (i == 0) {
//                LowX = pts.get(count - 1).longitude * Math.PI / 180;
//                LowY = pts.get(count - 1).latitude * Math.PI / 180;
//                MiddleX = pts.get(0).longitude * Math.PI / 180;
//                MiddleY = pts.get(0).latitude * Math.PI / 180;
//                HighX = pts.get(1).longitude * Math.PI / 180;
//                HighY = pts.get(1).latitude * Math.PI / 180;
//            } else if (i == count - 1) {
//                LowX = pts.get(count - 2).longitude * Math.PI / 180;
//                LowY = pts.get(count - 2).latitude * Math.PI / 180;
//                MiddleX = pts.get(count - 1).longitude * Math.PI / 180;
//                MiddleY = pts.get(count - 1).latitude * Math.PI / 180;
//                HighX = pts.get(0).longitude * Math.PI / 180;
//                HighY = pts.get(0).latitude * Math.PI / 180;
//            } else {
//                LowX = pts.get(i - 1).longitude * Math.PI / 180;
//                LowY = pts.get(i - 1).latitude * Math.PI / 180;
//                MiddleX = pts.get(i).longitude * Math.PI / 180;
//                MiddleY = pts.get(i).latitude * Math.PI / 180;
//                HighX = pts.get(i + 1).longitude * Math.PI / 180;
//                HighY = pts.get(i + 1).latitude * Math.PI / 180;
//            }
//            AM = Math.cos(MiddleY) * Math.cos(MiddleX);
//            BM = Math.cos(MiddleY) * Math.sin(MiddleX);
//            CM = Math.sin(MiddleY);
//            AL = Math.cos(LowY) * Math.cos(LowX);
//            BL = Math.cos(LowY) * Math.sin(LowX);
//            CL = Math.sin(LowY);
//            AH = Math.cos(HighY) * Math.cos(HighX);
//            BH = Math.cos(HighY) * Math.sin(HighX);
//            CH = Math.sin(HighY);
//            CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
//            CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);
//            ALtangent = CoefficientL * AL - AM;
//            BLtangent = CoefficientL * BL - BM;
//            CLtangent = CoefficientL * CL - CM;
//            AHtangent = CoefficientH * AH - AM;
//            BHtangent = CoefficientH * BH - BM;
//            CHtangent = CoefficientH * CH - CM;
//            AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent));
//            AngleCos = Math.acos(AngleCos);
//            ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
//            BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
//            CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;
//            if (AM != 0)
//                OrientationValue = ANormalLine / AM;
//            else if (BM != 0)
//                OrientationValue = BNormalLine / BM;
//            else
//                OrientationValue = CNormalLine / CM;
//            if (OrientationValue > 0) {
//                Sum1 += AngleCos;
//                Count1++;
//            } else {
//                Sum2 += AngleCos;
//                Count2++;
//            }
//        }
//
//        double tempSum1, tempSum2;
//        tempSum1 = Sum1 + (2 * Math.PI * Count2 - Sum2);
//        tempSum2 = (2 * Math.PI * Count1 - Sum1) + Sum2;
//        if (Sum1 > Sum2) {
//            if ((tempSum1 - (count - 2) * Math.PI) < 1)
//                Sum = tempSum1;
//            else
//                Sum = tempSum2;
//        } else {
//            if ((tempSum2 - (count - 2) * Math.PI) < 1)
//                Sum = tempSum2;
//            else
//                Sum = tempSum1;
//        }
//        totalArea = (Sum - (count - 2) * Math.PI) * Radius * Radius;
//        Log.e("Area", "多边形有" + count + "个顶点，" + count + 1 + "条边，面积是" + totalArea + "平方米，约" + totalArea / (20 / 3 * 100) + "亩");
//        if(totalArea==Double.NaN)
//            return 0;
//        return totalArea; // 返回总面积
    }

//    private static List<GPSBean> convertToGpsBean(List<LatLng> points) {
//        List<GPSBean> gps = new ArrayList<>();
//        for (LatLng point : points) {
//            gps.add(new GPSBean(point.latitude, point.longitude));
//        }
//        return gps;
//    }
//
//    private static final double MACRO_AXIS = 6378137; // 赤道圆的平均半径,单位米
//    private static final double MINOR_AXIS = 6356752; // 半短轴的长度，地球两极距离的一半  ，单位米
//
//    // 返回坐标系下的值
//    private static CoorPoint turnCoorPoint(GPSBean basePoint, GPSBean point) {
//        double a = Math.pow(MACRO_AXIS, 2.0);
//        double b = Math.pow(MINOR_AXIS, 2.0);
//        double c = Math.pow(Math.tan(basePoint.getLatitude()), 2.0);
//        double d = Math.pow(1/Math.tan(basePoint.getLatitude()),2.0);
//        //basePoint在截面的坐标
//        double x = a/Math.sqrt(a + b*c);
//        double y = b/Math.sqrt(b + a*d);
//
//        c = Math.pow(Math.tan(point.getLatitude()), 2.0);
//        d = Math.pow(1/Math.tan(point.getLatitude()), 2.0);
//
//        //point在截面的坐标
//        double m = a/Math.sqrt(a + b*c);
//        double n = b/Math.sqrt(b + a*d);
//
//        //计算截面坐标系下两点的直线距离，近似椭圆弧距离
//        double distanceY = Math.sqrt(Math.pow((m - x), 2.0) + Math.pow((n - y), 2.0));
//
//        c = Math.pow(Math.tan(basePoint.getLatitude()), 2.0);
//        x = a/Math.sqrt(a + b*c);
//        //计算坐标系的x值
//        double distanceX = x * (point.getLongtitude() - basePoint.getLongtitude());
//        CoorPoint coorPoint = new CoorPoint();
//        coorPoint.setX(distanceX);
//        coorPoint.setY(distanceY);
//        return coorPoint;
//    }
//
//    public static double computeArea(List<GPSBean> gpsBeans) {
//        double S = 0;
//        GPSBean basePoint = gpsBeans.get(0);
//        for(int i = 0; i < gpsBeans.size() - 1; i++) {
//            CoorPoint point = turnCoorPoint(basePoint, gpsBeans.get(i));
//            CoorPoint pointNext = turnCoorPoint(basePoint, gpsBeans.get(i + 1));
//            double temp = point.getX() * pointNext.getY() - pointNext.getX() * point.getY();
//            S += temp;
//        }
//        return Math.abs(S / 2.0);
//    }

    /**
     * 瓦片数据坐标转换
     */
    public static String tileXYToQuadKey(int tileX, int tileY, int levelOfDetail) {
        StringBuilder quadKey = new StringBuilder();
        for (int i = levelOfDetail; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tileX & mask) != 0) {
                digit++;
            }
            if ((tileY & mask) != 0) {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
    }
}
