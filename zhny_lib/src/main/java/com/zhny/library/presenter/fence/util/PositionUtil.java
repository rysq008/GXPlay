package com.zhny.library.presenter.fence.util;

import android.graphics.Point;

import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhengjiangrong on 2018/3/22.
 */

public class PositionUtil {

	public static final String BAIDU_LBS_TYPE = "bd09ll";

	public static final int SELECT_MARKER_LEFT = -1;
	public static final int SELECT_MARKER_RIGHT = 1;
	public static final int SELECT_MARKER_MIDDLE = 0;
	public static final int SELECT_MARKER_DEFAULT = 2;

	public static final String MARKER_VERTEX_TITLE = "VERTEX";//绘制地块的点的marker的title
	public static final String MARKER_LANDNAME_TITLE = "landname";//绘制地块的点的marker的title
	public static final String MARKER_CREATELAND_TITLE = "createwatchland";//绘制地块的点的marker的title
	public static final String CLUSTER_TYPE_WEATHERSTATION = "qixiangzhan";
	public static final String CLUSTER_TYPE_WATCHLAND = "xuntian";
	public static final String CLUSTER_TYPE_SPRINKLER = "sprinkler";
	public static final String CLUSTER_TYPE_SOILMETER = "soilmeter";
	public static final int CLUSTER_CIRCLE_RADIUS = 50;
	public static final String CLUSTER_TYPE_FARMERSHOW = "farmershow";

	public static double pi = 3.1415926535897932384626;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;
	public static final double MIN_LAT = -90;
	public static final double MAX_LAT = 90;
	public static final double MIN_LNG = -180;
	public static final double MAX_LNG = 180;

	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param longitude * @param latitude * @return
	 */
	public static PositionModel gcj_To_Gps84(double lat, double lon) {
		PositionModel gps = transform(lat, lon);
		double lontitude = lon * 2 - gps.getWgLon();
		double latitude = lat * 2 - gps.getWgLat();
		return new PositionModel(latitude, lontitude);
	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	public static PositionModel transform(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return new PositionModel(lat, lon);
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new PositionModel(mgLat, mgLon);
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}



	private static final int RANGE = 1;

	/**
	 * 计算三角形给定角的中线与垂直线的角度
	 *
	 * @param middle 顶点
	 * @param left   左边点
	 * @param right  右边点
	 * @return
	 */
	public static double getAngle(Point middle, Point left, Point right) {
		if (middle == null || left == null || right == null) {
			throw new IllegalArgumentException("给定的三个点不能为空");
		}
		Point center = new Point();
		double centerX = (middle.x + left.x + right.x) / 3.0d;
		double centerY = (middle.y + left.y + right.y) / 3.0d;
		//center.setXY(centerX, centerY);
		center.set((int) centerX, (int) centerY);
		if (Math.abs(middle.x - center.x) <= RANGE && Math.abs(middle.y - center.y) <= RANGE) {
			return getLineAngle(left, right); //如果在一条直线，传左右点
		} else {
			return getAngle(middle, center);
		}
	}

	/**
	 * 计算两点之间旋转夹角
	 *
	 * @param P1 顶点
	 * @param P0 中心点
	 * @return
	 */
	private static double getAngle(Point P1, Point P0) {
		//以下需旋转角度angle均为顺时针角度,初始角度为向上↑
		double angle = 0.0;
		if (P1.x == P0.x) {
			if (P1.y < P0.y) {
				angle = 180;
				return angle;
			}
			if (P1.y > P0.y) {
				angle = 0;
				return angle;
			}
			if (P1.y == P0.y) {
				throw new IllegalArgumentException("传入参数两个点不能为同一个点");
			}
		}
//        double a = Math.abs(Math.atan((P1.y - P0.y) / (P1.x - P0.x)) * 180 / Math.PI);
		BigDecimal P1X = new BigDecimal(P1.x);
		BigDecimal P1Y = new BigDecimal(P1.y);
		BigDecimal P0X = new BigDecimal(P0.x);
		BigDecimal P0Y = new BigDecimal(P0.y);
		double a = Math.abs(Math.atan((P1Y.subtract(P0Y)).divide(P1X.subtract(P0X), 8, BigDecimal.ROUND_HALF_UP).doubleValue()) * 180 / Math.PI);
		if (P1.x < P0.x && P1.y >= P0.y) {
			angle = 180 - (90 - a);
		}
		if (P1.x > P0.x && P1.y >= P0.y) {
			angle = 180 + (90 - a);
		}
		if (P1.x < P0.x && P1.y <= P0.y) {
			angle = 90 - a;
		}
		if (P1.x > P0.x && P1.y <= P0.y) {
			angle = 360 - (90 - a);
		}
		return angle;
	}


	/**
	 * 计算两点之间旋转夹角
	 *
	 * @param P1 左点
	 * @param P2 右点
	 * @return
	 */
	public static double getLineAngle(Point P1, Point P2) {
		//以下需旋转角度angle均为顺时针角度,初始角度为向上↑
		double angle = 0.0;
		if (P1.x == P2.x) {
			angle = 90;
			return angle;
		}
		BigDecimal P1X = new BigDecimal(P1.x);
		BigDecimal P1Y = new BigDecimal(P1.y);
		BigDecimal P2X = new BigDecimal(P2.x);
		BigDecimal P2Y = new BigDecimal(P2.y);
		double a = Math.abs(Math.atan((P1Y.subtract(P2Y)).divide(P1X.subtract(P2X), 2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 180 / Math.PI);
		if ((P1.x > P2.x && P1.y > P2.y) || (P1.x < P2.x && P1.y < P2.y)) {
			angle = -a;
		} else {
			angle = a;
		}
		return angle;
	}

	/**
	 * 获取不规则多边形重心点
	 */
	public static LatLng getCenterOfGravityPoint(List<LatLng> mPoints) {
		double area = 0.0;//多边形面积
		double gx = 0.0, gy = 0.0;// 重心的x、y
		for (int i = 1; i <= mPoints.size(); i++) {
			double iLat = mPoints.get(i % mPoints.size()).latitude;
			double iLng = mPoints.get(i % mPoints.size()).longitude;
			double nextLat = mPoints.get(i - 1).latitude;
			double nextLng = mPoints.get(i - 1).longitude;
			double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
			area += temp;
			gx += temp * (iLat + nextLat) / 3.0;
			gy += temp * (iLng + nextLng) / 3.0;
		}
		gx = gx / area;
		gy = gy / area;
		return new LatLng(gx, gy);
	}


	public static LatLng getCenterPoint(List<LatLng> points) {

		if (points.size() == 3) {
			double totalLat = 0;
			double totalLng = 0;
			for (LatLng point : points) {
				totalLat += point.latitude;
				totalLng += point.longitude;
			}
			double latitude = totalLat / points.size();
			double longitude = totalLng / points.size();
			return new LatLng(latitude, longitude);
		} else {
			double latitude = (getMinLatitude(points) + getMaxLatitude(points)) / 2;
			double longitude = (getMinLongitude(points) + getMaxLongitude(points)) / 2;
			return new LatLng(latitude, longitude);
		}
	}

	// 经度最小值
	public static double getMinLongitude(List<LatLng> mPoints) {
		double minLongitude = MAX_LNG;
		if (mPoints.size() > 0) {
			minLongitude = mPoints.get(0).longitude;
			for (LatLng latlng : mPoints) {
				// 经度最小值
				if (latlng.longitude < minLongitude)
					minLongitude = latlng.longitude;
			}
		}
		return minLongitude;
	}

	// 经度最大值
	public static double getMaxLongitude(List<LatLng> mPoints) {
		double maxLongitude = MIN_LNG;
		if (mPoints.size() > 0) {
			maxLongitude = mPoints.get(0).longitude;
			for (LatLng latlng : mPoints) {
				// 经度最大值
				if (latlng.longitude > maxLongitude)
					maxLongitude = latlng.longitude;
			}
		}
		return maxLongitude;
	}

	// 纬度最小值
	public static double getMinLatitude(List<LatLng> mPoints) {
		double minLatitude = MAX_LAT;
		if (mPoints.size() > 0) {
			minLatitude = mPoints.get(0).latitude;
			for (LatLng latlng : mPoints) {
				// 纬度最小值
				if (latlng.latitude < minLatitude)
					minLatitude = latlng.latitude;
			}
		}
		return minLatitude;
	}

	// 纬度最大值
	public static double getMaxLatitude(List<LatLng> mPoints) {
		double maxLatitude = MIN_LAT;
		if (mPoints.size() > 0) {
			maxLatitude = mPoints.get(0).latitude;
			for (LatLng latlng : mPoints) {
				// 纬度最大值
				if (latlng.latitude > maxLatitude)
					maxLatitude = latlng.latitude;
			}
		}
		return maxLatitude;
	}
}
